/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.subsystem;

import java.util.function.Supplier;

import org.frc4931.robot.vision.CameraServer;
import org.frc4931.robot.vision.CompositeCamera;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A subsystem used to control the vision system.
 */
public final class VisionSystem extends SubsystemBase {
    
    private final String frontCameraName;
    private final String rearCameraName;
    private final CompositeCamera cameras;

    /**
     * Create the vision system with no default command.
     * @param frontCameraName the name of the front camera; may be null if there is no front camera
     * @param rearCameraName the name of the front camera; may be null if there is no rear camera
     */
    public VisionSystem( String frontCameraName, String rearCameraName ) {
        this(frontCameraName,rearCameraName,null);
    }

    /**
     * Create the vision system with a default command supplier.
     * @param frontCameraName the name of the front camera; may be null if there is no front camera
     * @param rearCameraName the name of the front camera; may be null if there is no rear camera
     * @param defaultCommandSupplier the supplier for this subsystem's default command; may be null if there is no default command
     */
    public VisionSystem( String frontCameraName, String rearCameraName, Supplier<Command> defaultCommandSupplier) {
        super(defaultCommandSupplier);
        this.frontCameraName = frontCameraName;
        this.rearCameraName = rearCameraName;
        this.cameras = new CompositeCamera(this.frontCameraName,this.rearCameraName);
    }
    
    /**
     * Start sending images from the robot's cameras to the driver station.
     */
    @Override
    public void startup() {
        super.startup();
        if ( frontCameraName != null && rearCameraName != null ) {
            CameraServer.getInstance().startServer();
        }
    }
    
    /**
     * Stop sending images from the robot's cameras to the driver station.
     */
    @Override
    public void shutdown() {
        super.shutdown();
        if ( frontCameraName != null && rearCameraName != null ) {
            CameraServer.getInstance().stopAutomaticCapture();
            CameraServer.getInstance().startServer();
        }
    }

    /**
     * Start sending imagegs from the robot's front camera to the driver station.
     */
    public void useFrontCamera() {
        if ( frontCameraName != null ) {
            cameras.switchToCamera(frontCameraName);
        }
    }

    /**
     * Start sending imagegs from the robot's rear camera to the driver station.
     */
    public void useRearCamera() {
        if ( rearCameraName != null ) {
            cameras.switchToCamera(rearCameraName);
        }
    }
    
    /**
     * Start automatically capturing images from the camera(s).
     */
    public void startAutomaticCapture() {
        CameraServer.getInstance().startAutomaticCapture(cameras);
    }
    
    // TODO: Add methods that will do the processing of the front camera for autonomous mode. For instance, it might include a method
    // to find the center of the tote relative to the center of the camera; this might be used by a command that moves the robot left
    // or right so that the yellow tote is centered on the camera.
}
