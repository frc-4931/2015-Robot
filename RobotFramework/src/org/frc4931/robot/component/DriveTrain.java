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
@FunctionalInterface
public interface DriveTrain {
    /**
     * Set the motor voltages for the left and right sides of the drive train.
     * 
     * @param leftMotor left motor voltage
     * @param rightMotor right motor voltage
     */
    void drive(double leftMotor, double rightMotor);

    /**
     * Create a new drive train with motors on the left and motors on the right. Note that either motor can be a
     * {@link Motor#compose(Motor, Motor) composite of several motors}.
     * 
     * @param left left motor; may not be null
     * @param right right motor; may not be null
     * @return the new drive train: never null
     * @see Motor
     * @see Motor#compose(Motor, Motor)
     * @see Motor#compose(Motor, Motor, Motor)
     */
    static DriveTrain create(Motor left, Motor right) {
        assert left != null;
        assert right != null;
        return (leftMotor,rightMotor)->{
            left.setSpeed(leftMotor);
            right.setSpeed(rightMotor);
            
        };
    }
}
