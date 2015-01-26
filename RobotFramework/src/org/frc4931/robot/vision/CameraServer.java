/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.vision;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.frc4931.robot.vision.Camera.Size;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.RawData;
import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * Encapsulates an Motion JPEG (M-JPEG) server that sends images to the Smart Dashboard. To receive images on the Smart Dashboard,
 * choose \"USB Camera HW\" on the dashboard.
 * <p>
 * The server must be {@link #startServer() started} when desired and can be {@link #stopServer() stopped}, although most of the
 * time robot code will not want to stop and restart the server. Instead, generally the server will be started at the beginning of
 * autonomous or teleoperated mode and will run until the end of the match.
 * <p>
 * There are two ways of providing images to the server, but these work only when the server is running. The first is manually
 * adding images via the {@link #sendImage(Image)}; this is useful for sending modified or annotated images collected from the
 * NIVisiion code (usually via our {@link Camera} or the standard {@link USBCamera}).
 * <p>
 * The second approach is to have this server {@link #startAutomaticCapture(Camera) automatically capture images} from a given
 * camera at the frame rate desired by the Smart Dashboard client. The specified camera can even be a {@link CompositeCamera},
 * which allows the robot code to automatically send images from one of several different named cameras and at any time to switch
 * between those cameras.
 * <p>
 * <h2>How this works</h2>
 * This class uses a single {@link BlockingDeque blocking double ended queue} (or "deque") to pass images between the
 * automatic capture thread (or manually sent images) and the server thread. This deque is threadsafe, and it allows eah
 * thread to efficiently block when the other is not running. For example, only when the server thread is started will
 * a small number of image buffers be added to the deque. Automatically capturing images will repeatedly remove a buffer
 * from the queue, populate it from the camera, and put it back into the queue. Then, the server thread will repeatedly
 * remove a (presumably filled buffer) from the same queue, send it to the client, and put the buffer back into the queue.
 * <p>
 * The number of buffers in the queue is an important part of this mechanism. It must be small enough not to cause a noticeable
 * lag in the image stream. On the other hand, if there are too few buffers then the server and capture threads will end up
 * waiting for each other. Currently, this class uses 3 buffers.
 * <p>
 * The capture thread attempts to capture images at approximately the same frame rate as specified by the Smart Dashboard
 * client. However, it is possible to capture at a slightly higher rate without any adverse affect. For example, if the
 * capture thread is operating significantly faster, then it might actually (re)populate all of the buffers in the deque
 * before the server thread has a chance to send even one. This is perfectly acceptable, since the server will always attempt
 * to send the image in the buffer at the front of the deque. A higher capture rate simply requires more CPU on
 * the robot without any tangible benefit for sending images to the client.
 * <p>
 * Note: This is a re-implementation of the WPILib {@link edu.wpi.first.wpilibj.CameraServer} with that dramatically reduces
 * thread contention by eliminating most locking/blocking, while using much simpler multi-threaded logic through the use of
 * the {@link BlockingDeque} rather than arrays that have to be managed correctly (and which didn't seem to behave correctly
 * in WPILib's {@link edu.wpi.first.wpilibj.CameraServer}). This class provides almost identical functionality, though this
 * class exposes a method to {@link #stopServer() stop the server} that makes possible unit testing.
 * and blocking
 */
public final class CameraServer {

    /**
     * The ratio of capture frequency to the server frequency. The latter is determined by the M-JPEG client
     * (driver station), so the capture frequency is multiplied by this factor.
     */
    private static final long CAPTURE_TO_SERVER_RATE = 1;
    private static final int CAPTURE_FRAMES_PER_SECOND_DEFAULT = 10;

    private static final int MAX_IMAGE_SIZE = 200000;
    /**
     * The maximum amount of time that the capture thread will poll for the {@link #imageQueue} for an available buffer
     * before continuing. See also {@link #CAPTURE_WAIT_TIME_UNIT} for the time unit.
     */
    private static final long CAPTURE_WAIT_TIME = 5;
    private static final TimeUnit CAPTURE_WAIT_TIME_UNIT = TimeUnit.SECONDS;
    private static final int IMAGE_QUEUE_SIZE = 3;

    public static final int SERVER_PORT = 1180;
    protected static final byte[] MAGIC_HEADER = { 0x01, 0x00, 0x00, 0x00 };
    public static final int SIZE_640x480 = 0;
    public static final int SIZE_320x240 = 1;
    public static final int SIZE_160x120 = 2;
    protected static final int HARDWARE_COMPRESSION = -1;

    private final AtomicReference<Consumer<Camera>> cameraSettings = new AtomicReference<>((camera) -> {});
    private final BlockingDeque<ImageData> imageQueue = new LinkedBlockingDeque<>(IMAGE_QUEUE_SIZE);

    private static CameraServer INSTANCE = new CameraServer();
    
    /**
     * Get the singletone camera server instance.
     * @return the camera server instance; never null
     */
    public static CameraServer getInstance() {
        return INSTANCE;
    }
    
    /**
     * Compute the time interval in milliseconds for the given frames per second.
     * 
     * @param framesPerSecond the number of frames per second
     * @return the time interval in milliseconds between frames
     */
    protected static long computePeriod(int framesPerSecond) {
        return (long) (1000 / (1.0 * framesPerSecond));
    }

    protected static final class ImageData {
        protected final ByteBuffer data = ByteBuffer.allocateDirect(MAX_IMAGE_SIZE);
        protected int offset = -1;

        protected boolean isEmpty() {
            return offset == -1;
        }
        
        protected void reset() {
            data.position(0);
            data.limit(data.capacity() - 1);
        }

        protected void copyTo(DataOutputStream output) throws IOException {
            try {
                final int imageSize = data.remaining();
                data.position(offset);
                byte[] imageArray = new byte[imageSize];
                data.get(imageArray, 0, imageSize);
                output.writeInt(imageSize);
                output.write(imageArray, offset, imageSize);
            } finally {
                // Prepare for writing again ...
                data.position(0);
            }
        }
    }

    private volatile boolean runServer = false;
    private volatile boolean runCapture = false;
    private volatile Camera camera;
    private volatile Thread serverThread;
    private volatile Thread captureThread;
    private volatile long period = computePeriod(CAPTURE_FRAMES_PER_SECOND_DEFAULT);
    private volatile int quality;
    private volatile boolean hardwareCompression = true;

    private CameraServer() {
    }

    /**
     * Start the M-JPEG server.
     * <p>
     * This method does nothing if the server is already running.
     * 
     * @see #stopServer()
     */
    public synchronized void startServer() {
        if (serverThread == null) {
            runServer = true;
            // Add empty buffers to the buffer queue. If automatic capture has already been enabled, then that thread
            // will be blocked on this queue waiting for buffers to become available. Adding buffers to the queue will
            // free the capture thread to start capturing cameras ...
            assert imageQueue.isEmpty();
            for (int i = 0; i != IMAGE_QUEUE_SIZE; ++i) {
                imageQueue.addFirst(new ImageData());
            }
            serverThread = new Thread(this::serve);
            serverThread.setName("CameraServer Send Thread");
            serverThread.setDaemon(true);
            serverThread.start();
        }
    }

    /**
     * Stop the M-JPEG server. This method does nothing if the server is not already running.
     * <p>
     * This method does stop the server, but after this method is called the server cannot be restarted and the client
     * will not be able to reconnect.
     * 
     * @see #startServer()
     */
    public synchronized void stopServer() {
        runServer = false;
        if (serverThread != null) {
            try {
                serverThread.interrupt();
            } finally {
                this.serverThread = null;
            }
        }
    }

    /**
     * Start automatically capturing images from the specified camera.
     * <p>
     * This method does nothing if {@code camera} is null or if automatic capture is already running.
     * 
     * @param camera the camera from which images should be captured
     * @see #stopAutomaticCapture()
     */
    public synchronized void startAutomaticCapture(Camera camera) {
        if (captureThread == null && camera != null) {
            runCapture = true;
            cameraSettings.get().accept(camera);
            this.camera = camera;
            captureThread = new Thread(this::capture);
            captureThread.setName("CameraServer Send Thread");
            captureThread.setDaemon(true);
            captureThread.start();
        }
    }

    /**
     * Stop automatically capturing images from the camera.
     * <p>
     * This method does nothing if automatic capture is not currently running. It is safe to call before the
     * {@link #startServer() server has been started}, though automatic capture blocks until the server is started.
     * 
     * @see #startAutomaticCapture(Camera)
     */
    public synchronized void stopAutomaticCapture() {
        runCapture = false;
        if (captureThread != null) {
            try {
                captureThread.interrupt();
            } finally {
                this.captureThread = null;
            }
        }
    }

    protected void capture() {
        Image frame = null;
        try {
            frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        } catch (UnsatisfiedLinkError err) {
            // do nothing ...
        }
        long t0 = 0;
        while (runCapture) {
            t0 = System.currentTimeMillis();
            try {
                // Grab the latest buffer from the capture queue, and block for a fixed amount of time ...
                ImageData buffer = imageQueue.pollFirst(CAPTURE_WAIT_TIME, CAPTURE_WAIT_TIME_UNIT);
                if (buffer != null) {
                    buffer.reset();

                    if (hardwareCompression) {
                        // Capture the image from the camera using this buffer ...
                        camera.getImageData(buffer.data);
                        buffer.offset = 0;
                    } else {
                        // Capture the image using the NIVision code ...
                        camera.getImage(frame);
                        copyImage(frame, buffer);
                    }
                    // Since there are never more ImageData objects than the size of either deque,
                    // this should always succeed immediately ...
                    buffer.data.position(0);
                    imageQueue.putLast(buffer);
                }

                sleep(t0, period / CAPTURE_TO_SERVER_RATE);
            } catch (InterruptedException e) {
                // Our thread was interrupted while waiting, so acknowledge and move on ...
                Thread.interrupted();
            }
        }
    }

    protected void serve() {
        try {
            ServerSocket socket = new ServerSocket();
            socket.setReuseAddress(true);
            InetSocketAddress address = new InetSocketAddress(SERVER_PORT);
            socket.bind(address);
            long t0 = 0;
            while (runServer) {
                // Establish a connection with the client ...
                Socket s = socket.accept();

                DataInputStream is = new DataInputStream(s.getInputStream());
                DataOutputStream os = new DataOutputStream(s.getOutputStream());

                // Read from the client the specifications on the images ...
                int fps = is.readInt();
                int compression = is.readInt();
                int size = is.readInt();

                if (compression != HARDWARE_COMPRESSION) {
                    DriverStation.reportError("Choose \"USB Camera HW\" on the dashboard", false);
                    s.close();
                    continue;
                }

                // Adjust the camera based upon the client's specifications ...
                setQuality(100 - compression);
                setCameraSettings((camera) -> {
                    camera.setFPS(fps);
                    camera.setSize(Size.forOption(size));
                });

                period = computePeriod(fps);
                while (runServer) {
                    t0 = System.currentTimeMillis();
                    try {
                        // Get the next image to send ...
                        ImageData imageData = imageQueue.pollFirst(CAPTURE_WAIT_TIME, CAPTURE_WAIT_TIME_UNIT);
                        if (imageData != null) {
                            try {
                                // The first few ImageData objects are empty, so we should only do something to them
                                // if they contain actual image data...
                                if (!imageData.isEmpty()) {

                                    // Write the image to the socket ...
                                    os.write(MAGIC_HEADER);
                                    imageData.copyTo(os);
                                    os.flush();
                                }
                            } catch (IOException | UnsupportedOperationException ex) {
                                reportError(ex);
                                break;
                            } finally {
                                // Only the first image data's are empty, so just put them onto the capture queue.
                                // This may actually unblock the capture thread if it has already been started.
                                // Note that this should always succeed, since there are never more ImageData objects
                                // than can fit in a single queue, and we already pulled this off of our queue ...
                                imageQueue.putLast(imageData);
                            }
                        }

                        // Wait until we should send another image ...
                        sleep(t0, period);
                    } catch (InterruptedException e) {
                        // Our thread was interrupted, so acknowledge and move on ...
                        Thread.interrupted();
                    }
                }
            }
        } catch (IOException e) {
            reportError(e);
        }
    }

    /**
     * Set the quality of the compressed image sent to the dashboard
     *
     * @param quality
     *            The quality of the JPEG image, from 0 to 100
     */
    public void setQuality(int quality) {
        this.quality = quality > 100 ? 100 : quality < 0 ? 0 : quality;
    }

    /**
     * Get the quality of the compressed image sent to the dashboard
     *
     * @return The quality, from 0 to 100
     */
    public int getQuality() {
        return quality;
    }

    protected void setCameraSettings(Consumer<Camera> settings) {
        if (settings != null) {
            // Set this for any future cameras ...
            cameraSettings.set(settings);
            // And if we have a camera already, then apply the settings ...
            Camera current = this.camera;
            if (current != null) {
                settings.accept(current);
            }
        }
    }

    /**
     * Manually change the image that is served by the MJPEG stream. This can be
     * called to pass custom annotated images to the dashboard. Note that, for
     * 640x480 video, this method could take between 40 and 50 milliseconds to
     * complete.
     *
     * This shouldn't be called if {@link #startAutomaticCapture} is called.
     *
     * @param image The IMAQ image to show on the dashboard; may not be null
     * @param destination the target where the image should be copied; may not be null
     */
    protected void copyImage(Image image, ImageData destination) {
        // Flatten the IMAQ image to a JPEG
        RawData data = NIVision.imaqFlatten(image,
                                            NIVision.FlattenType.FLATTEN_IMAGE,
                                            NIVision.CompressionType.COMPRESSION_JPEG, 10 * quality);
        ByteBuffer buffer = data.getBuffer();

        /* Find the start of the JPEG data */
        int index = 0;
        if (hardwareCompression) {
            while (index < buffer.limit() - 1) {
                if ((buffer.get(index) & 0xff) == 0xFF
                        && (buffer.get(index + 1) & 0xff) == 0xD8)
                    break;
                index++;
            }
        }

        if (buffer.limit() - index - 1 <= 2) {
            throw new VisionException("data size of flattened image is less than 2. Try another camera! ");
        }

        buffer.position(index);
        destination.data.put(buffer);
        destination.offset = index;
    }

    /**
     * Manually send the image that is served by the running MJPEG stream. This can be
     * called to pass custom annotated images to the dashboard. Note that, for
     * 640x480 video, this method could take between 40 and 50 milliseconds to
     * complete.
     *
     * This method should only be called after the server is {@link #startServer() started} but before {
     * {@link #startAutomaticCapture(Camera) automatic capture is started}.
     * This method does nothing if the server is not already {@link #startServer() started}.
     *
     * @param image The IMAQ image to show on the dashboard; may not be null
     */
    public void sendImage(Image image) {
        // Attempt to get a image data buffer, but do not block or wait: if there is no image, the server is not running
        // and there is nothing do ...
        ImageData buffer = imageQueue.pollFirst();
        if (buffer != null) {
            copyImage(image, buffer);
        }
    }

    protected static void reportError(Throwable t) {
        try {
            DriverStation.reportError(t.getMessage(), true);
        } catch (UnsatisfiedLinkError err) {
            // Not running on the robot ...
            System.err.println(t.getMessage());
        }
    }

    protected static void sleep(long startTime, long maxIntervalInMillis) throws InterruptedException {
        long dt = System.currentTimeMillis() - startTime;
        if (dt < maxIntervalInMillis) {
            Thread.sleep(maxIntervalInMillis - dt);
        }
    }
}
