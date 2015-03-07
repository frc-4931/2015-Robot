/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * <p>A motor that has some sort of position feedback and can be ordered to a position. A {@link MotorWithAngle}
 * needs a home position, usually implemented with a {@link Switch}, a drive mechanism, usually a {@link Motor},
 * and some position feedback, usually an {@link AngleSensor}.</p>
 * 
 * <p>IMPORTANT: The {@link AngleSensor} and {@link Motor} should be
 * configured so that positive values indicate movement away from home and negative values indicate movement
 * towards home.</p>
 * 
 * @author Nathan Brown
 */
public interface MotorWithAngle {

    /**
     * 
     * @param angle angle to test
     * @return true if at angle is in target range; returns false for anything else
     */
    public boolean isAt(double angle);
    
    /**
     * sets the angle of this motor
     * @param angle target angle of motor
     */
    public void setAngle(double angle);
    
    /**
     * Gets the angle of this {@link MotorWithAngle}.
     * @return the angle of this {@link MotorWithAngle}
     */
    public double getAngle();

    /**
     * Stops this {@link MotorWithAngle} and disables control.
     */
    public void stop();

   /**
    * If this {@link MotorWithAngle} is already at the home position drives it away and returns it. Otherwise
    * just drives to home.
    * <br>IMPORTANT: {@code awaySpeed} and {@code towardSpeed} should both be positive values. The motor should be
    * configured such that positive speeds move away from home and negative speeds move towards home.
    * @param awaySpeed how fast to drive away from home
    * @param towardSpeed how fast to drive toward home
    */
    void home(double awaySpeed, double towardSpeed);
}
