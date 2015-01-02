/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.subsystem;

import org.frc4931.robot.component.DriveTrain;

/**
 * 
 */
public class DriveSystem {

    private final DriveTrain driveTrain;

    public DriveSystem(DriveTrain driveTrain) {
        this.driveTrain = driveTrain;
    }

    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * 
     * @param driveSpeed The value to use for fowards/backwards
     * @param turnSpeed The value to use for the rotate right/left
     */
    public void arcade(double driveSpeed, double turnSpeed) {
        arcade(driveSpeed, turnSpeed, true);
    }

    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * 
     * @param driveSpeed The value to use for forwards/backwards
     * @param turnSpeed The value to use for the rotate right/left
     * @param squaredInputs If set, decreases the sensitivity at low speeds
     */
    public void arcade(double driveSpeed, double turnSpeed, boolean squaredInputs) {
        double leftMotorSpeed;
        double rightMotorSpeed;

        driveSpeed = limit(driveSpeed);
        turnSpeed = limit(turnSpeed);

        if (squaredInputs) {
            // square the inputs (while preserving the sign) to increase fine control while permitting full power
            if (driveSpeed >= 0.0) {
                driveSpeed = (driveSpeed * driveSpeed);
            } else {
                driveSpeed = -(driveSpeed * driveSpeed);
            }
            if (turnSpeed >= 0.0) {
                turnSpeed = (turnSpeed * turnSpeed);
            } else {
                turnSpeed = -(turnSpeed * turnSpeed);
            }
        }

        if (driveSpeed > 0.0) {
            if (turnSpeed > 0.0) {
                leftMotorSpeed = driveSpeed - turnSpeed;
                rightMotorSpeed = Math.max(driveSpeed, turnSpeed);
            } else {
                leftMotorSpeed = Math.max(driveSpeed, -turnSpeed);
                rightMotorSpeed = driveSpeed + turnSpeed;
            }
        } else {
            if (turnSpeed > 0.0) {
                leftMotorSpeed = -Math.max(-driveSpeed, turnSpeed);
                rightMotorSpeed = driveSpeed + turnSpeed;
            } else {
                leftMotorSpeed = driveSpeed - turnSpeed;
                rightMotorSpeed = -Math.max(-driveSpeed, -turnSpeed);
            }
        }

        this.driveTrain.drive(leftMotorSpeed, rightMotorSpeed);
    }

    /**
     * Provide tank steering using the stored robot configuration.
     * This function lets you directly provide joystick values from any source.
     * 
     * @param leftSpeed The value of the left stick.
     * @param rightSpeed The value of the right stick.
     * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
     */
    public void tank(double leftSpeed, double rightSpeed, boolean squaredInputs) {
        // square the inputs (while preserving the sign) to increase fine control while permitting full power
        leftSpeed = limit(leftSpeed);
        rightSpeed = limit(rightSpeed);
        if (squaredInputs) {
            if (leftSpeed >= 0.0) {
                leftSpeed = (leftSpeed * leftSpeed);
            } else {
                leftSpeed = -(leftSpeed * leftSpeed);
            }
            if (rightSpeed >= 0.0) {
                rightSpeed = (rightSpeed * rightSpeed);
            } else {
                rightSpeed = -(rightSpeed * rightSpeed);
            }
        }
        this.driveTrain.drive(leftSpeed, rightSpeed);
    }

    /**
     * Provide tank steering using the stored robot configuration.
     * This function lets you directly provide joystick values from any source.
     * 
     * @param leftSpeed The value of the left stick.
     * @param rightSpeed The value of the right stick.
     */
    public void tank(double leftSpeed, double rightSpeed) {
        this.driveTrain.drive(limit(leftSpeed), limit(rightSpeed));
    }

    public void cheesy(double driveSpeed, double turnSpeed, boolean isQuickTurn, boolean isHighGear) {

    }

    /**
     * Limit motor values to the -1.0 to +1.0 range.
     * 
     * @param num the number
     * @return the limited value
     */
    private static double limit(double num) {
        if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
    }
}
