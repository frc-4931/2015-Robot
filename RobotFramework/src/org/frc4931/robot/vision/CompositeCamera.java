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
import java.util.function.Function;

import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * A set of named cameras. This class extends {@link Camera} (and ultimately {@link USBCamera}) and therefore can be used
 * where {@link USBCamera} is expected, but this object can {@link #switchToCamera(String) switch to} different named cameras at
 * any time.
 */
public final class CompositeCamera extends Camera {

    private final Map<String, Camera> camerasByName = new HashMap<>();
    private final AtomicReference<Camera> current = new AtomicReference<>();
    private final Lock lock = new ReentrantLock();

    /**
     * Create a new composite camera that is composed of USB cameras with the given names.
     * 
     * @param cameraNames the names of the USB cameras; may not be null and must have at least one name
     */
    public CompositeCamera(String... cameraNames) {
        assert cameraNames.length > 0;
        for (String cameraName : cameraNames) {
            if (cameraName != null) {
                Camera camera = Camera.getNamedCamera(cameraName);
                camerasByName.put(cameraName, camera);
                current.compareAndSet(null, camera); // sets the first camera
            }
        }
    }

    /**
     * Create a new composite camera that is composed of given USB camerass.
     * 
     * @param cameras the USB cameras; may not be null and must have at least one value
     */
    protected CompositeCamera(Camera... cameras) {
        assert cameras.length > 0;
        for (Camera camera : cameras) {
            if ( camera != null ) {
                camerasByName.put(camera.getName(), camera);
                current.compareAndSet(null, camera); // sets the first camera
            }
        }
    }

    /**
     * Switch to the camera with the given name. If there is no camera with the given name, this method returns without doing
     * anything.
     * 
     * @param name the name of the camera that should be used
     * @return true if the camera being used by this object has switch, or false if the camera did not change, either perhaps
     *         because
     *         the camera did not exist or because the camera was already being used
     */
    public synchronized boolean switchToCamera(String name) {
        Camera camera = camerasByName.get(name);
        if (camera != null) {
            try {
                lock.lock();
                Camera previous = current.get();
                if (previous != camera) {
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
     * Get the current camera.
     * 
     * @return the current camera; never null
     */
    public Camera getCurrentCamera() {
        return current.get();
    }

    /**
     * Get the camera with the given name.
     * 
     * @param name the name of the camera
     * @return the camera, or null if no camera exists for the specified name
     */
    public Camera getCamera(String name) {
        return camerasByName.get(name);
    }

    /**
     * Set the brightness on all of the cameras.
     * 
     * @param brightness the brightness
     */
    @Override
    public synchronized void setBrightness(int brightness) {
        onEachCamera(camera -> camera.setBrightness(brightness));
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
     * 
     * @param value the manual exposure value
     */
    @Override
    public synchronized void setExposureManual(int value) {
        onEachCamera(camera -> camera.setExposureManual(value));
    }

    /**
     * Set the frame rate on all of the cameras.
     * 
     * @param fps the frames per second
     */
    @Override
    public synchronized void setFPS(int fps) {
        onEachCamera(camera -> camera.setFPS(fps));
    }

    /**
     * Set the image size on all of the cameras.
     * 
     * @param width the width of the images
     * @param height the height of the images
     */
    @Override
    public synchronized void setSize(int width, int height) {
        onEachCamera(camera -> camera.setSize(width, height));
    }

    @Override
    public synchronized void setSize(Size size) {
        onEachCamera(camera -> camera.setSize(size));
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
     * 
     * @param value the white balance
     */
    @Override
    public synchronized void setWhiteBalanceManual(int value) {
        onEachCamera(camera -> camera.setWhiteBalanceManual(value));
    }

    /**
     * Set the brightness on the camera with the given name. This method does nothing if there is no camera with the given name.
     * 
     * @param cameraName the name of the camera; may not be null
     * @param brightness the brightness
     * @return true if the camera was changed, or false if there was no camera with the given name
     */
    public synchronized boolean setBrightness(String cameraName, int brightness) {
        return onNamedCamera(cameraName, camera -> camera.setBrightness(brightness));
    }

    /**
     * Set the auto exposure on the camera with the given name. This method does nothing if there is no camera with the given
     * name.
     * 
     * @param cameraName the name of the camera; may not be null
     * @return true if the camera was changed, or false if there was no camera with the given name
     */
    public synchronized boolean setExposureAuto(String cameraName) {
        return onNamedCamera(cameraName, USBCamera::setExposureAuto);
    }

    /**
     * Set the named camera to hold the current exposure. This method does nothing if there is no camera with the given name.
     * 
     * @param cameraName the name of the camera; may not be null
     * @return true if the camera was changed, or false if there was no camera with the given name
     */
    public synchronized boolean setExposureHoldCurrent(String cameraName) {
        return onNamedCamera(cameraName, USBCamera::setExposureHoldCurrent);
    }

    /**
     * Set the manual exposure on the camera with the given name. This method does nothing if there is no camera with the given
     * name.
     * 
     * @param cameraName the name of the camera; may not be null
     * @param value the manual exposure value
     * @return true if the camera was changed, or false if there was no camera with the given name
     */
    public synchronized boolean setExposureManual(String cameraName, int value) {
        return onNamedCamera(cameraName, camera -> camera.setExposureManual(value));
    }

    /**
     * Set the image size on the camera with the given name. This method does nothing if there is no camera with the given name.
     * 
     * @param cameraName the name of the camera; may not be null
     * @param width the width of the images
     * @param height the height of the images
     * @return true if the camera was changed, or false if there was no camera with the given name
     */
    public synchronized boolean setSize(String cameraName, int width, int height) {
        return onNamedCamera(cameraName, camera -> camera.setSize(width, height));
    }

    /**
     * Set the auto white balance on the camera with the given name. This method does nothing if there is no camera with the given
     * name.
     * 
     * @param cameraName the name of the camera; may not be null
     * @return true if the camera was changed, or false if there was no camera with the given name
     */
    public synchronized boolean setWhiteBalanceAuto(String cameraName) {
        return onNamedCamera(cameraName, USBCamera::setWhiteBalanceAuto);
    }

    /**
     * Set the named camera to hold the current white balance. This method does nothing if there is no camera with the given name.
     * 
     * @param cameraName the name of the camera; may not be null
     * @return true if the camera was changed, or false if there was no camera with the given name
     */
    public synchronized boolean setWhiteBalanceHoldCurrent(String cameraName) {
        return onNamedCamera(cameraName, USBCamera::setWhiteBalanceHoldCurrent);
    }

    /**
     * Set the manual white balance on the camera with the given name. This method does nothing if there is no camera with the
     * given name.
     * 
     * @param cameraName the name of the camera; may not be null
     * @param value the white balance
     * @return true if the camera was changed, or false if there was no camera with the given name
     */
    public synchronized boolean setWhiteBalanceManual(String cameraName, int value) {
        return onNamedCamera(cameraName, camera -> camera.setWhiteBalanceManual(value));
    }

    /**
     * Update the settings on all of the cameras.
     */
    @Override
    public synchronized void updateSettings() {
        onEachCamera(USBCamera::updateSettings);
    }

    /**
     * Update the settings on the camera with the given name. This method does nothing if there is no camera with the given name.
     * 
     * @param cameraName the name of the camera; may not be null
     * @return true if the camera was updated, or false if there was no camera with the given name
     */
    public synchronized boolean updateSettings(String cameraName) {
        return onNamedCamera(cameraName, USBCamera::updateSettings);
    }

    /**
     * Close the current camera.
     */
    @Override
    public synchronized void closeCamera() {
        onCurrentCamera(Camera::closeCamera);
    }

    /**
     * Open the current camera.
     */
    @Override
    public synchronized void openCamera() {
        onCurrentCamera(Camera::openCamera);
    }

    /**
     * Use the current camera to get an image.
     * 
     * @param image the image buffer in which the image should be set
     */
    @Override
    public synchronized void getImage(Image image) {
        onCurrentCamera(camera -> camera.getImage(image));
    }

    /**
     * Use the current camera to get the image data.
     * 
     * @param data the byte buffer in which the image should be set
     */
    @Override
    public synchronized void getImageData(ByteBuffer data) {
        onCurrentCamera(camera -> camera.getImageData(data));
    }

    /**
     * Get the brightness of the cameras.
     */
    @Override
    public synchronized int getBrightness() {
        return onCurrentCamera(Camera::getBrightness, 0);
    }

    /**
     * Start capturing images with the current camera.
     */
    @Override
    public synchronized void startCapture() {
        onCurrentCamera(Camera::startCapture);
    }

    /**
     * Stop capturing images with the current camera.
     */
    @Override
    public synchronized void stopCapture() {
        onCurrentCamera(Camera::stopCapture);
    }

    protected void onEachCamera(Consumer<Camera> consumer) {
        camerasByName.values().stream().forEach(consumer);
    }

    protected void onCurrentCamera(Consumer<Camera> consumer) {
        if (this.current == null) {
            // Still initializing this object ...
            return;
        }
        Camera camera = this.current.get();
        if (camera != null) {
            consumer.accept(camera);
        }
    }

    protected int onCurrentCamera(Function<Camera, Integer> function, int defaultValue) {
        if (this.current == null) {
            // Still initializing this object ...
            return defaultValue;
        }
        Camera camera = this.current.get();
        if (camera != null) {
            return function.apply(camera);
        }
        return defaultValue;
    }

    protected boolean onNamedCamera(String cameraName, Consumer<Camera> consumer) {
        Camera camera = camerasByName.get(cameraName);
        if (camera != null) {
            consumer.accept(camera);
            return true;
        }
        return false;
    }

}
