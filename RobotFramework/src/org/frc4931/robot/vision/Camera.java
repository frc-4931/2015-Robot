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
 * A {@link USBCamera} that exposes the camera name.
 */
public class Camera extends USBCamera {
    
    private static final ConcurrentMap<String,Camera> INSTANCES = new ConcurrentHashMap<>();
    private static final Lock LOCK = new ReentrantLock();

    /**
     * Obtain the camera with the given name. CAlling this method with the same camera name will always return the same {@link Camera} instance.
     * @param name the name of the camera; may not be null
     * @return the camera; never null
     */
    public static Camera getCamera( String name ) {
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
        return getCamera(USBCamera.kDefaultCameraName);
    }

    private final String name;
    
    protected Camera() {
        this(USBCamera.kDefaultCameraName);
    }

    protected Camera( String name ) {
        super(name);
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
