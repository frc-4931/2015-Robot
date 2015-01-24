/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.vision;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import edu.wpi.first.wpilibj.CameraServer;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A server that will automatically capture images from one or more cameras and send the images to the SmartDashboard.
 * This class wraps the WPILib {@link CameraServer} class, which can only handle one camera at a time.
 * <p>
 * This class should not be used if vision processing is to be done on the RoboRIO. Also, if this class is used, then code should
 * not use {@link CameraServer}.
 * <p>
 * This class is threadsafe.
 */
public final class MultiCameraServer {

    protected static final CameraServer SERVER = CameraServer.getInstance(); // starts the server

    protected static final AtomicReference<ImageCapture> CAPTURE = new AtomicReference<>();
    private static final Lock threadLock = new ReentrantLock();
    private static final Lock nameLock = new ReentrantLock();
    private static volatile Thread captureThread;
    private static volatile boolean captureMore = false;
    private static volatile String lastCameraName = null;

    private static interface ImageCapture {
        public void captureAndSend(Image frame);
    }

    /**
     * Start automatically capturing images to send to the dashboard from the named camera. This method can safely be called
     * multiple times, although after the first time it only sets the name of the camera from which images are captured.
     *
     * @param cameraName The name of the camera interface (e.g. "cam1") from which the images should be captured; may not be null
     */
    public static void startAutomaticCapture(String cameraName) {
        useCamera(cameraName);
        if (captureThread == null) {
            // Try to start the capture thread if it is not already running, but use a lock to make sure that multiple threads
            // don't try to call this at the same time.
            threadLock.lock();
            try {
                if (SERVER.isAutoCaptureStarted()) {
                    throw new IllegalStateException("Unable to start the auto-capture of images using " +
                            MultiCameraServer.class.getSimpleName() + " because auto-capture has already been started on the " +
                            CameraServer.class.getSimpleName() + " class.");
                } else if (captureThread == null) {
                    // not yet running, so start it ...
                    captureMore = true;
                    Thread thread = new Thread(() -> {
                        // Create an image that we'll use for a buffer ...
                            Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
                            // Then as long as we're supposed to, capture an image and send it ...
                            while (captureMore) {
                                CAPTURE.get().captureAndSend(frame);
                            }
                        });
                    thread.start();
                    captureThread = thread;
                }
            } finally {
                threadLock.unlock();
            }
        }
    }

    /**
     * Change the name of the camera from which images should be captured and sent to the dashboard. This method can be called
     * one or more times after {@link #startAutomaticCapture(String)} is called, and the capturing thread will correctly use
     * the new camera to capture the next image.
     * 
     * @param cameraName The name of the camera interface (e.g. "cam1") from which the images should be captured; may not be null
     */
    public static void useCamera(String cameraName) {
        assert cameraName != null;
        try {
            nameLock.lock();
            if ( lastCameraName.equals(cameraName) ) {
                // We're already using this camera ...
                return;
            }
            lastCameraName = cameraName;

            // Change the function to grab the image from the right camera. We use a little trick to first set a function that
            // initializes NIVision to use the given named camera, and that function (when completed) will set a new function that
            // just does the capture and sends the image to the CameraServer. This makes the `useCamera(id)` function
            CAPTURE.set(new ImageCapture() {
                @Override
                public void captureAndSend(Image frame) {
                    // The camera name changed, so configure NIVision to use the new camera name ...
                    int id = NIVision.IMAQdxOpenCamera(cameraName, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
                    NIVision.IMAQdxConfigureGrab(id);
                    NIVision.IMAQdxStartAcquisition(id);

                    // Now that NIVision is prepped for the new camera, create a new function that will do the work ...
                    ImageCapture grab = new ImageCapture() {
                        @Override
                        public void captureAndSend(Image frame) {
                            NIVision.IMAQdxGrab(id, frame, 1);
                            SERVER.setImage(frame);
                        }
                    };

                    // Replace this function so it will be called next time ...
                    CAPTURE.set(grab);

                    // But we have to call it once for this call ...
                    grab.captureAndSend(frame);
                }
            });
        } finally {
            nameLock.unlock();
        }
    }

    /**
     * Stop the automatic capture server. Generally this won't be needed, but it is avialable just in case.
     */
    public static void stopAutomaticCapture() {
        captureMore = false;
        captureThread = null;
    }
}
