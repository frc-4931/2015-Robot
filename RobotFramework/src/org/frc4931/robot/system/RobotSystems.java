/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.component.Accelerometer;
import org.frc4931.robot.subsystem.DriveSystem;

/**
 * 
 */
public class RobotSystems {
    public final DriveSystem drive;
    public final Accelerometer accelerometer;
    public final Superstructure structure;
    
    public RobotSystems(DriveSystem drive, Accelerometer accelerometer, Superstructure structure) {
        this.drive = drive;
        this.accelerometer = accelerometer;
        this.structure = structure;
    }
}
