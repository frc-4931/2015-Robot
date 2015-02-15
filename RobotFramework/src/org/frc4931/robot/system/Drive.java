/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.commandnew.Scheduler.Requireable;

/**
 * 
 */
public interface Drive extends Requireable{

    /**
     * Stop the drive train. This sets the left and right motor speeds to 0, and shifting to low gear.
     */
    public default void stop() {
        tank(0, 0, false);
    }

    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * 
     * @param driveSpeed the value to use for forwards/backwards; must be -1 to 1, inclusive
     * @param turnSpeed the value to use for the rotate right/left; must be -1 to 1, inclusive
     */
    public default void arcade(double driveSpeed, double turnSpeed) {
        arcade(driveSpeed, turnSpeed, true);
    }

    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * 
     * @param driveSpeed the value to use for forwards/backwards; must be -1 to 1, inclusive
     * @param turnSpeed the value to use for the rotate right/left; must be -1 to 1, inclusive
     *                  Negative values turn right; positive values turn left.
     * @param squaredInputs if set, decreases the sensitivity at low speeds
     */
    public default void arcade(double driveSpeed, double turnSpeed, boolean squaredInputs) {
        if (squaredInputs) {
            driveSpeed = driveSpeed * driveSpeed * Math.signum(driveSpeed);
            turnSpeed = turnSpeed * turnSpeed * Math.signum(turnSpeed);
        }
        
        double leftSpeed;
        double rightSpeed;
        
        if (driveSpeed > 0.0) {
            if (turnSpeed > 0.0) {
                leftSpeed = driveSpeed - turnSpeed;
                rightSpeed = Math.max(driveSpeed, turnSpeed);
            } else {
                leftSpeed = Math.max(driveSpeed, -turnSpeed);
                rightSpeed = driveSpeed + turnSpeed;
            }
        } else {
            if (turnSpeed > 0.0) {
                leftSpeed = -Math.max(-driveSpeed, turnSpeed);
                rightSpeed = driveSpeed + turnSpeed;
            } else {
                leftSpeed = driveSpeed - turnSpeed;
                rightSpeed = -Math.max(-driveSpeed, -turnSpeed);
            }
        }
        tank(leftSpeed, rightSpeed, false);
    }

    /**
     * Provide tank steering using the stored robot configuration.
     * This function lets you directly provide joystick values from any source.
     * 
     * @param leftSpeed The value of the left stick; must be -1 to 1, inclusive
     * @param rightSpeed The value of the right stick; must be -1 to 1, inclusive
     * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
     */
    public void tank(double leftSpeed, double rightSpeed, boolean squaredInputs);

    /**
     * Provide tank steering using the stored robot configuration.
     * This function lets you directly provide joystick values from any source.
     * 
     * @param leftSpeed The value of the left stick; must be -1 to 1, inclusive
     * @param rightSpeed The value of the right stick; must be -1 to 1, inclusive
     */
    public default void tank(double leftSpeed, double rightSpeed) {
        tank(leftSpeed, rightSpeed, true);
    }

    /**
     * Provide "cheezy drive" steering using a steering wheel and throttle.
     * This function lets you directly provide joystick values from any source.
     * 
     * @param throttle the value of the throttle; must be -1 to 1, inclusive
     * @param wheel the value of the steering wheel; must be -1 to 1, inclusive
     *              Negative values turn right; positive values turn left.
     * @param isQuickTurn true if the quick-turn button is pressed
     * @see <a href="https://github.com/Team254/FRC-2014/blob/master/src/com/team254/frc2014/CheesyDriveHelper.java">Team 254 Cheesy Drive logic</a>
     */
    public void cheesy(double throttle, double wheel, boolean isQuickTurn);

}
