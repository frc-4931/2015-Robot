/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */

/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.subsystem;

import java.util.function.Supplier;

import org.frc4931.robot.component.DriveTrain;
import org.frc4931.robot.component.Relay;
import org.frc4931.robot.component.Relay.State;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A subsystem that can be used to control a {@link DriveTrain} using arcade, tank, or cheesy methods.
 */
public final class DriveSystem extends SubsystemBase {
    
    private static final double SENSITIVITY_HIGH = 0.75;
    private static final double SENSITIVITY_LOW = 0.75;
    private static final double HALF_PI = Math.PI / 2.0;
    private static final double MAXIMUM_VOLTAGE = 1.0;
    private static final double MINIMUM_USABLE_VOLTAGE = 0.02;

    private final DriveTrain driveTrain;
    private final Relay highGear;

    private double quickStopAccumulator = 0.0;
    private double oldWheel = 0.0;

    /**
     * Creates a new DriveSystem subsystem that uses the supplied drive train and optional shifter. Assumes no default command.
     * @param driveTrain the drive train for the robot; may not be null
     * @param shifter the optional shifter used to put the transmission into high gear; may be null
     */
    public DriveSystem(DriveTrain driveTrain, Relay shifter) {
        this(driveTrain, shifter, null);
    }
    
    /**
     * Creates a new DriveSystem subsystem that uses the supplied drive train and optional shifter.
     * @param driveTrain the drive train for the robot; may not be null
     * @param shifter the optional shifter used to put the transmission into high gear; may be null
     * @param defaultCommandSupplier the supplier for this subsystem's default command; may be null if there is no default command
     */
    public DriveSystem(DriveTrain driveTrain, Relay shifter, Supplier<Command> defaultCommandSupplier ) {
        super(defaultCommandSupplier);
        this.driveTrain = driveTrain;
        this.highGear = shifter != null ? shifter : Relay.fixed(State.OFF);
    }
    
    /**
     * Shift the drive train into high gear. This method does nothing if the robot has no shifter.
     */
    public void highGear() {
        this.highGear.on();
    }
    
    /**
     * Shift the drive train into low gear. This method does nothing if the robot has no shifter.
     */
    public void lowGear() {
        this.highGear.off();
    }

    /**
     * Stop the drive train. This sets the left and right motor speeds to 0, and shifting to low gear.
     */
    public void stop() {
        this.driveTrain.drive(0.0, 0.0);
        this.highGear.off();
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
     * @param squaredInputs if set, decreases the sensitivity at low speeds
     */
    public void arcade(double driveSpeed, double turnSpeed, boolean squaredInputs) {
        double leftMotorSpeed;
        double rightMotorSpeed;

        driveSpeed = limit(MAXIMUM_VOLTAGE, driveSpeed, MINIMUM_USABLE_VOLTAGE);
        turnSpeed = limit(MAXIMUM_VOLTAGE, turnSpeed, MINIMUM_USABLE_VOLTAGE);

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
        leftSpeed = limit(MAXIMUM_VOLTAGE, leftSpeed, MINIMUM_USABLE_VOLTAGE);
        rightSpeed = limit(MAXIMUM_VOLTAGE, rightSpeed, MINIMUM_USABLE_VOLTAGE);
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
        leftSpeed = limit(MAXIMUM_VOLTAGE, leftSpeed, MINIMUM_USABLE_VOLTAGE);
        rightSpeed = limit(MAXIMUM_VOLTAGE, rightSpeed, MINIMUM_USABLE_VOLTAGE);
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
     * @param isQuickTurn true if the quick-turn button is pressed
     * @see <a href="https://github.com/Team254/FRC-2014/blob/master/src/com/team254/frc2014/CheesyDriveHelper.java">Team 254 Cheesy Drive logic</a>
     */
    public void cheesy(double throttle, double wheel, boolean isQuickTurn) {

        wheel = limit(MAXIMUM_VOLTAGE, wheel, MINIMUM_USABLE_VOLTAGE);
        throttle = limit(MAXIMUM_VOLTAGE, throttle, MINIMUM_USABLE_VOLTAGE);

        double negInertia = wheel - oldWheel;
        oldWheel = wheel;

        double wheelNonLinearity = 0.6;
        final boolean isHighGear = highGear.isOn();
        if (isHighGear) {
            wheelNonLinearity = 0.6;
            // Apply a sin function that's scaled to make it feel better.
            wheel = dampen(wheel, wheelNonLinearity);
            wheel = dampen(wheel, wheelNonLinearity);
        } else {
            wheelNonLinearity = 0.5;
            // Apply a sin function that's scaled to make it feel better.
            wheel = dampen(wheel, wheelNonLinearity);
            wheel = dampen(wheel, wheelNonLinearity);
            wheel = dampen(wheel, wheelNonLinearity);
        }

        double leftPwm, rightPwm, overPower;
        double sensitivity;

        double angularPower;
        double linearPower;

        // Negative inertia!
        double negInertiaAccumulator = 0.0;
        double negInertiaScalar;
        if (isHighGear) {
            sensitivity = SENSITIVITY_HIGH;
            negInertiaScalar = 5.0;
        } else {
            sensitivity = SENSITIVITY_LOW;
            if (wheel * negInertia > 0) {
                negInertiaScalar = 2.5;
            } else {
                if (Math.abs(wheel) > 0.65) {
                    negInertiaScalar = 5.0;
                } else {
                    negInertiaScalar = 3.0;
                }
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
                quickStopAccumulator = (1 - alpha) * quickStopAccumulator + alpha * limit(1.0, wheel, 0.0) * 5;
            }
            overPower = 1.0;
            if (isHighGear) {
                sensitivity = 1.0;
            } else {
                sensitivity = 1.0;
            }
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

    /**
     * Limit motor values to the -1.0 to +1.0 range, ensuring that it is above the specified minimum value.
     * 
     * @param maximum the maximum allowed value; must be positive or equal to zero
     * @param num the input value
     * @param minimumReadable the minimum value below which 0.0 is used; must be positive or equal to zero
     * @return the limited output value
     */
    private static double limit(double maximum, double num, double minimumReadable) {
        assert maximum >= 0.0;
        if (num > maximum) {
            return 1.0;
        }
        if (Math.abs(num) < maximum) {
            return -1.0;
        }
        return Math.abs(num) > minimumReadable ? num : 0.0;
    }
}
