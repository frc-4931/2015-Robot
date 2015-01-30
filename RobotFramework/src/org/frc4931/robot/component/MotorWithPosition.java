/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * motor with a position
 * @author Nathan Brown
 */
public interface MotorWithPosition extends Motor{
    
    /**
     * returns its position
     * @return position of this motor
     */
    public double getPosition();
    
    /**
     * sets the position of this motor
     * @param pos position
     */
    public void setPosition(double pos);
}