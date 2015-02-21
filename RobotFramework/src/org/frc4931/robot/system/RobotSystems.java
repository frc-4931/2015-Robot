/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.component.Accelerometer;

/**
 * 
 */
public class RobotSystems {
    public final DriveInterpreter drive;
    public final Accelerometer accelerometer;
    public final Superstructure structure;
    public final PowerPanel powerPanel;
    
    public RobotSystems(DriveInterpreter drive, Accelerometer accelerometer, Superstructure structure, PowerPanel powerPanel) {
        this.drive = drive;
        this.accelerometer = accelerometer;
        this.structure = structure;
        this.powerPanel = powerPanel;
    }
}
