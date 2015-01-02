/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * A representation of a drive train with motors on the left and right.
 */
public interface DriveTrain {
    /**
     * Set the motor voltages for the left and right sides of the drive train.
     * @param leftMotor left motor voltage
     * @param rightMotor right motor voltage
     */
    void drive( double leftMotor, double rightMotor);
    
}
