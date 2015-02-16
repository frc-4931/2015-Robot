/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.commandnew.Scheduler.Requireable;
import org.frc4931.robot.component.DriveTrain;
import org.frc4931.utils.Operations;

/**
 * 
 */
public class DriveInterpreter implements Requireable {
    private static final double MAX_SPEED = 1.0;
    private static final double MIN_SPEED = 0.0;
    private static final double SENSITIVITY = 0.75;
    private static final double HALF_PI = Math.PI / 2.0;

    private final DriveTrain driveTrain;

    private double quickStopAccumulator = 0.0;
    private double oldWheel = 0.0;

    /**
     * Creates a new {@link DriveInterpreter} that wraps the specified drive train.
     * @param driveTrain the drive train for the robot; may not be null
     */
    public DriveInterpreter(DriveTrain driveTrain) {
        this.driveTrain = driveTrain;
    }
    
    /**
     * Stop the drive train. This sets the left and right motor speeds to 0.
     */
    public void stop() {
        this.driveTrain.drive(0.0, 0.0);
    }

    /**
     * Arcade drive implements single stick driving.
     * This function lets you directly provide joystick values from any source.
     * 
     * @param driveSpeed the value to use for forwards/backwards; must be -1 to 1, inclusive
     * @param turnSpeed the value to use for the rotate right/left; must be -1 to 1, inclusive
     */
    public void arcade(double driveSpeed, double turnSpeed) {
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
    public void arcade(double driveSpeed, double turnSpeed, boolean squaredInputs) {
        double leftMotorSpeed;
        double rightMotorSpeed;

        driveSpeed = Operations.limit(MAX_SPEED, driveSpeed, MIN_SPEED);
        turnSpeed = Operations.limit(MAX_SPEED, turnSpeed, MIN_SPEED);

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
     * @param leftSpeed The value of the left stick; must be -1 to 1, inclusive
     * @param rightSpeed The value of the right stick; must be -1 to 1, inclusive
     * @param squaredInputs Setting this parameter to true decreases the sensitivity at lower speeds
     */
    public void tank(double leftSpeed, double rightSpeed, boolean squaredInputs) {
        // square the inputs (while preserving the sign) to increase fine control while permitting full power
        leftSpeed = Operations.limit(MAX_SPEED, leftSpeed, MIN_SPEED);
        rightSpeed = Operations.limit(MAX_SPEED, rightSpeed, MIN_SPEED);
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
     * @param leftSpeed The value of the left stick; must be -1 to 1, inclusive
     * @param rightSpeed The value of the right stick; must be -1 to 1, inclusive
     */
    public void tank(double leftSpeed, double rightSpeed) {
        leftSpeed = Operations.limit(MAX_SPEED, leftSpeed, MIN_SPEED);
        rightSpeed = Operations.limit(MAX_SPEED, rightSpeed, MIN_SPEED);
        this.driveTrain.drive(leftSpeed, rightSpeed);
    }

    /**
     * Used in the {@link #cheesy(double, double, boolean) cheesy drive} logic, this function dampens the supplied input
     * at low-to-mid input values.
     * @param wheel the input value of the steering wheel; must be -1 to 1, inclusive
     * @param wheelNonLinearity the non-linearity factor
     * @return the dampened output
     */
    private static double dampen(double wheel, double wheelNonLinearity) {
        double factor = HALF_PI * wheelNonLinearity;
        return Math.sin(factor * wheel) / Math.sin(factor);
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
    public void cheesy(double throttle, double wheel, boolean isQuickTurn) {

        wheel = Operations.limit(MAX_SPEED, wheel, MIN_SPEED);
        throttle = Operations.limit(MAX_SPEED, throttle, MIN_SPEED);

        double negInertia = wheel - oldWheel;
        oldWheel = wheel;

        double wheelNonLinearity = 0.6;
        wheelNonLinearity = 0.5;
        // Apply a sin function that's scaled to make it feel better.
        wheel = dampen(wheel, wheelNonLinearity);
        wheel = dampen(wheel, wheelNonLinearity);
        wheel = dampen(wheel, wheelNonLinearity);

        double leftPwm, rightPwm, overPower;
        double sensitivity;

        double angularPower;
        double linearPower;

        // Negative inertia!
        double negInertiaAccumulator = 0.0;
        double negInertiaScalar;
        sensitivity = SENSITIVITY;
        if (wheel * negInertia > 0) {
            negInertiaScalar = 2.5;
        } else {
            if (Math.abs(wheel) > 0.65) {
                negInertiaScalar = 5.0;
            } else {
                negInertiaScalar = 3.0;
            }
        }
        double negInertiaPower = negInertia * negInertiaScalar;
        negInertiaAccumulator += negInertiaPower;

        wheel = wheel + negInertiaAccumulator;
        if (negInertiaAccumulator > 1) {
            negInertiaAccumulator -= 1;
        } else if (negInertiaAccumulator < -1) {
            negInertiaAccumulator += 1;
        } else {
            negInertiaAccumulator = 0;
        }
        linearPower = throttle;

        // Quickturn!
        if (isQuickTurn) {
            if (Math.abs(linearPower) < 0.2) {
                double alpha = 0.1;
                quickStopAccumulator = (1 - alpha) * quickStopAccumulator + alpha * Operations.limit(1.0, wheel, 0.0) * 5;
            }
            overPower = 1.0;
            sensitivity = 1.0;
            angularPower = wheel;
        } else {
            overPower = 0.0;
            angularPower = Math.abs(throttle) * wheel * sensitivity - quickStopAccumulator;
            if (quickStopAccumulator > 1) {
                quickStopAccumulator -= 1;
            } else if (quickStopAccumulator < -1) {
                quickStopAccumulator += 1;
            } else {
                quickStopAccumulator = 0.0;
            }
        }

        rightPwm = leftPwm = linearPower;
        leftPwm += angularPower;
        rightPwm -= angularPower;

        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0);
            leftPwm = 1.0;
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0);
            rightPwm = 1.0;
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm);
            leftPwm = -1.0;
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm);
            rightPwm = -1.0;
        }
        driveTrain.drive(leftPwm, rightPwm);
    }
}
