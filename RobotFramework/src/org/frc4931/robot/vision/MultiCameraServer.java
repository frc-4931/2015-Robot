/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.vision;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;

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

    private static AtomicReference<String> CAMERA_NAME = new AtomicReference<>();
    private static Thread captureThread;
    private static final Lock lock = new ReentrantLock();

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
            lock.lock();
            try {
                if (SERVER.isAutoCaptureStarted()) {
                    throw new IllegalStateException("Unable to start the auto-capture of images using " +
                                MultiCameraServer.class.getSimpleName() + " because auto-capture has already been started on the " +
                                CameraServer.class.getSimpleName() + " class.");
                } else if (captureThread == null) {
                    // not yet running, so start it ...
                    Thread thread = new Thread(MultiCameraServer::captureImageFromCamera);
                    thread.start();
                    captureThread = thread;
                }
            } finally {
                lock.unlock();
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
        CAMERA_NAME.set(cameraName);
    }

    private static void captureImageFromCamera() {
        String name = CAMERA_NAME.get();
        Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        int id = NIVision.IMAQdxOpenCamera(name, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(id);
        NIVision.IMAQdxStartAcquisition(id);

        while (true) {
            NIVision.IMAQdxGrab(id, frame, 1);
            SERVER.setImage(frame);
        }
    }
}
