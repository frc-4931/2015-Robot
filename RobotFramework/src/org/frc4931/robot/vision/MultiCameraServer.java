/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.vision;

/**
 * A server that will automatically capture images from one or more cameras and send the images to the SmartDashboard.
 * This class wraps the WPILib {@link CameraServer} class, which can only handle one camera at a time.
 * <p>
 * This class should not be used if vision processing is to be done on the RoboRIO. Also, if this class is used, then code should
 * not direclty use {@link CameraServer}.
 * <p>
 * This class is threadsafe.
 */
public final class MultiCameraServer {

    private static final CameraServer SERVER = CameraServer.getInstance(); // starts the server

    private static volatile CompositeCamera composite;

    /**
     * Start automatically capturing images to send to the dashboard from the named cameras. This method can safely be called
     * multiple times, although after the first time it only sets the name of the camera from which images are captured.
     *
     * @param cameraNames The names of the camera interfaces (e.g. "cam1") from which the images should be captured; may not be
     *            null
     */
    public static void startAutomaticCapture(String... cameraNames) {
        if (composite == null) {
            // Not yet started ...
            composite = new CompositeCamera(cameraNames);
            SERVER.startServer();
            SERVER.startAutomaticCapture(composite);
        } else {
            // It was already started once and shutdown, so we just need to re-start the current camera ...
            composite.startCapture();
        }
    }
    
    public static void startServer() {
        SERVER.startServer();
    }

    /**
     * Start automatically capturing images to send to the dashboard from the specified {@link Camera}s. This method can safely be
     * called multiple times, although after the first time it only sets the name of the camera from which images are captured.
     *
     * @param cameras The cameras from which the images should be captured; may not be
     *            null
     */
    public static void startAutomaticCapture(Camera... cameras) {
        if (composite == null) {
            // Not yet started ...
            if ( cameras.length == 1 && cameras[0] instanceof CompositeCamera ) {
                composite = (CompositeCamera)cameras[0];
            } else {
                composite = new CompositeCamera(cameras);
            }
            SERVER.startServer();
            SERVER.startAutomaticCapture(composite);
        }
    }

    /**
     * Change the name of the camera from which images should be captured and sent to the dashboard. This method can be called
     * one or more times after {@link #startAutomaticCapture} is called, and the capturing thread will correctly use
     * the new camera to capture the next image.
     * 
     * @param cameraName The name of the camera interface (e.g. "cam1") from which the images should be captured; may not be null
     * @return true if automatic capture will start using the named camera, or false if the camera did not change, either perhaps
     *         because
     *         the camera did not exist or because the camera was already being used
     */
    public static boolean useCamera(String cameraName) {
        return composite.switchToCamera(cameraName);
    }

    /**
     * Set the quality of the compressed image sent to the dashboard.
     *
     * @param quality The quality of the JPEG image, from 0 to 100
     */
    public static void setQuality(int quality) {
        SERVER.setQuality(quality);
    }

    /**
     * Get the quality of the compressed image sent to the dashboard.
     *
     * @return The quality, from 0 to 100
     */
    public static int getQuality() {
        return SERVER.getQuality();
    }

    /**
     * Stop automatically capturing images to send to the dashboard.
     */
    public static void stopAutomaticCapture() {
        // There is currently nothing we can do to shut down the CameraServer!
        SERVER.stopAutomaticCapture();
        if (composite != null) {
            try {
                composite.stopCapture();
            } finally {
                composite = null;
            }
        }
    }

    /**
     * Stop the server. This should only be called once, and the server is really not usable after that.
     */
    protected static void stopServer() {
        stopAutomaticCapture();
        SERVER.stopServer();
    }

}
