/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.vision;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * A {@link USBCamera} that exposes the camera name and standard sizes used by the {@link CameraServer}.
 */
public class Camera extends USBCamera {
    
    /**
     * Some standard sizes for images as used by the Smart Dashboard.
     */
    public static enum Size {
        LARGE(0,640, 480), MEDIUM(1,320, 240), SMALL(2,160, 120);
        private int option;
        private int width;
        private int height;

        private Size(int option, int width, int height) {
            this.option = option;
            this.width = width;
            this.height = height;
        }

        /**
         * Get the code value for this size, as used by the Smart Dashboard.
         * @return the code
         */
        public int getCode() {
            return option;
        }
        
        /**
         * Get the height of the image in number of pixels.
         * @return the height in pixels
         */
        public int getHeight() {
            return height;
        }
        
        /**
         * Get the width of the image in number of pixels.
         * @return the width in pixels
         */
        public int getWidth() {
            return width;
        }
        
        /**
         * Obtain the size enum for the given option.
         * @param option the option
         * @return the size for the given option, or {@link #SMALL} if there is no enum with the given option value; never null
         */
        public static Size forOption( int option ) {
            for ( Size size : Size.values() ) {
                if ( size.option == option ) return size;
            }
            return SMALL;
        }
    }

    private static final ConcurrentMap<String,Camera> INSTANCES = new ConcurrentHashMap<>();
    private static final Lock LOCK = new ReentrantLock();

    /**
     * Obtain the camera with the given name. CAlling this method with the same camera name will always return the same {@link Camera} instance.
     * @param name the name of the camera; may not be null
     * @return the camera; never null
     */
    public static Camera getNamedCamera( String name ) {
        assert name != null;
        Camera camera = INSTANCES.get(name);
        if ( camera == null ) {
            try {
                LOCK.lock();
                camera = INSTANCES.get(name);
                if ( camera == null ) {
                    camera = new Camera(name);
                    INSTANCES.put(name,camera);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return new Camera(name);
    }

    public static Camera getDefaultCamera() {
        return getNamedCamera(USBCamera.kDefaultCameraName);
    }

    private final String name;
    
    protected Camera() {
        this(USBCamera.kDefaultCameraName);
    }

    protected Camera( String name ) {
        super(name);
        this.name = name;
    }
    
    /**
     * Get the name of the camera.
     * @return the camera name; never null
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Set the image size for the camera to one of the {@link Size predefined sizes}. This method does nothing if the
     * specified size is null.
     * 
     * @param size the predefined size
     */
    public void setSize( Size size ) {
        if ( size != null ) super.setSize(size.getWidth(), size.getHeight());
    }
}
