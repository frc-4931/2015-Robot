/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.utils.Operations;


/**
 * motor with a position
 * @author Nathan Brown
 */
public interface MotorWithAngle extends Motor, AngleSensor{

    /**
     * gets the tolerance of this motor with position
     * @return tolerance of the motor
     */
    public double getTolerance();
    
    /**
     * sets the tolerance for the motor
     * @param tol is the new tolerance  for this motor
     */
    public void setTolerance(double tol);
    /**
     * 
     * @param angle angle to test
     * @return true if at angle is in target range; returns false for anything else
     */
    public default boolean isAt(double angle) {
        return Operations.fuzzyCompare(getAngle(), angle, getTolerance()) == 0;
    }
    
    /**
     * sets the angle of this motor
     * @param angle target angle of motor
     */
    public void setAngle(double angle);
}