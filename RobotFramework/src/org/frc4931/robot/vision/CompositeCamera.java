/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.vision;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * A set of named USB cameras. This class extends {@link USBCamera} and therefore can be used where USBCamera is expected,
 * but this object can {@link #switchToCamera(String) switch to} different named cameras at any time.
 */
public class CompositeCamera extends USBCamera {

    private final Map<String, USBCamera> cameras = new HashMap<>();
    private final AtomicReference<USBCamera> current = new AtomicReference<>();
    private final Lock lock = new ReentrantLock();

    /**
     * Create a new composite camera that is composed of USB cameras with the given names.
     * @param cameraNames the names of the USB cameras; may not be null and must have at least one name
     */
    public CompositeCamera(String... cameraNames) {
        assert cameraNames.length > 0;
        for (String cameraName : cameraNames) {
            USBCamera camera = new USBCamera(cameraName);
            cameras.put(cameraName, camera);
            current.compareAndSet(null, camera);    // sets the first camera
        }
    }

    /**
     * Switch to the camera with the given name. If there is no camera with the given name, this method returns without doing anything.
     * @param name the name of the camera that should be used
     * @return true if the camera being used by this object has switch, or false if the camera did not change, either perhaps because
     * the camera did not exist or because the camera was already being used
     */
    public synchronized boolean switchToCamera(String name) {
        USBCamera camera = cameras.get(name);
        if (camera != null) {
            try {
                lock.lock();
                USBCamera previous = current.get();
                if ( previous != camera ) {
                    // We're using a different camera, so prepare to capture images ...
                    camera.startCapture();
                    try {
                        // Use the new camera ...
                        current.set(camera);
                    } finally {
                        // Stop capturing with the old camera ...
                        previous.stopCapture();
                    }
                    return true;
                }
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
    
    /**
     * Set the brightness on all of the cameras.
     * @param brightness the brightness
     */
    @Override
    public synchronized void setBrightness(int brightness) {
        onEachCamera(camera->camera.setBrightness(brightness));
    }

    /**
     * Set the auto exposure on all of the cameras.
     */
    @Override
    public synchronized void setExposureAuto() {
        onEachCamera(USBCamera::setExposureAuto);
    }
    
    /**
     * Set all the cameras to hold the current exposure.
     */
    @Override
    public synchronized void setExposureHoldCurrent() {
        onEachCamera(USBCamera::setExposureHoldCurrent);
    }
    
    /**
     * Set the manual exposure on all of the cameras.
     * @param value the manual exposure value
     */
    @Override
    public synchronized void setExposureManual(int value) {
        onEachCamera(camera->camera.setExposureManual(value));
    }
    
    /**
     * Set the frame rate on all of the cameras.
     * @param fps the frames per second
     */
    @Override
    public synchronized void setFPS(int fps) {
        onEachCamera(camera->camera.setFPS(fps));
    }
    
    /**
     * Set the image size on all of the cameras.
     * @param width the width of the images
     * @param height the height of the images
     */
    @Override
    public synchronized void setSize(int width, int height) {
        onEachCamera(camera->camera.setSize(width,height));
    }
    
    /**
     * Set the auto white balance on all of the cameras.
     */
    @Override
    public synchronized void setWhiteBalanceAuto() {
        onEachCamera(USBCamera::setWhiteBalanceAuto);
    }
    
    /**
     * Set all the cameras to hold the current white balance.
     */
    @Override
    public synchronized void setWhiteBalanceHoldCurrent() {
        onEachCamera(USBCamera::setWhiteBalanceHoldCurrent);
    }
    
    /**
     * Set the manual white balance on all of the cameras.
     * @param value the white balance
     */
    @Override
    public synchronized void setWhiteBalanceManual(int value) {
        onEachCamera(camera->camera.setWhiteBalanceManual(value));
    }
    
    /**
     * Update the settings on all of the cameras.
     */
    @Override
    public synchronized void updateSettings() {
        onEachCamera(USBCamera::updateSettings);
    }

    /**
     * Close the current camera.
     */
    @Override
    public synchronized void closeCamera() {
        onCurrentCamera(USBCamera::closeCamera);
    }
    
    /**
     * Open the current camera.
     */
    @Override
    public synchronized void openCamera() {
        onCurrentCamera(USBCamera::openCamera);
    }
    
    /**
     * Use the current camera to get an image.
     * @param image the image buffer in which the image should be set
     */
    @Override
    public synchronized void getImage(Image image) {
        current.get().getImage(image);
    }
    
    /**
     * Use the current camera to get the image data.
     * @param data the byte buffer in which the image should be set
     */
    @Override
    public synchronized void getImageData(ByteBuffer data) {
        current.get().getImageData(data);
    }
    
    /**
     * Get the brightness of the cameras.
     */
    @Override
    public synchronized int getBrightness() {
        return current.get().getBrightness();
    }
    
    /**
     * Start capturing images with the current camera.
     */
    @Override
    public synchronized void startCapture() {
        onCurrentCamera(USBCamera::startCapture);
    }
    
    /**
     * Stop capturing images with the current camera.
     */
    @Override
    public synchronized void stopCapture() {
        onCurrentCamera(USBCamera::stopCapture);
    }

    protected void onEachCamera( Consumer<USBCamera> runnable ) {
        cameras.values().stream().forEach(runnable);
    }

    protected void onCurrentCamera( Consumer<USBCamera> runnable ) {
        cameras.values().stream().forEach(runnable);
    }
}
