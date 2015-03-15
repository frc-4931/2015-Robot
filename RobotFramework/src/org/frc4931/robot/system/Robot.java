/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.component.Accelerometer;
import org.frc4931.robot.component.Counter;
import org.frc4931.robot.driver.OperatorInterface;
import org.frc4931.robot.system.RobotBuilder.Componets;


/**
 * 
 */
public class Robot {
    public final DriveInterpreter drive;
    public final Accelerometer accelerometer;
    public final Superstructure structure;
    public final PowerPanel powerPanel;
    public final OperatorInterface operator;
    public final Compressor compressor;
    public final Componets componets;
    public final Counter toteCounter;
    
    public Robot(DriveInterpreter drive, Accelerometer accelerometer, Superstructure structure, PowerPanel powerPanel, OperatorInterface operator, Compressor compressor, Counter toteCounter, Componets componets) {
        this.drive = drive;
        this.accelerometer = accelerometer;
        this.structure = structure;
        this.powerPanel = powerPanel;
        this.operator = operator;
        this.compressor = compressor;
        this.componets = componets;
        this.toteCounter = toteCounter;
    }
}
