/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.utils.Operations;

/**
 * A motor is a device that can be set to operate at a speed.
 * 
 * @author Zach Anderson
 * 
 */
public interface Motor {

    public enum Direction {
        FORWARD, REVERSE, STOPPED
    }

    /**
     * Sets the speed of this {@link Motor}.
     * 
     * @param speed
     *            the new speed of this {@link Motor}, as a double, clamped to
     *            -1.0 to 1.0 inclusive
     */
    public void setSpeed(double speed);

    /**
     * Gets the current speed of this {@link Motor}.
     * 
     * @return the speed of this {@link Motor}, will be between -1.0 and 1.0
     *         inclusive
     */
    public double getSpeed();

    /**
     * Stops this {@link Motor}. Same as calling {@code setSpeed(0.0)}.
     */
    default public void stop() {
        setSpeed(0.0);
    }

    /**
     * Gets the current {@link Direction} of this {@link Motor}, can be {@code FORWARD}, {@code REVERSE}, or {@code STOPPED}.
     * 
     * @return the {@link Direction} of this {@link Motor}
     */
    default public Direction getDirection() {
        int direction = Operations.fuzzyCompare(getSpeed(), 0.0);
        if (direction < 0)
            return Direction.REVERSE;
        else if (direction > 0)
            return Direction.FORWARD;
        else
            return Direction.STOPPED;
    }

    /**
     * Create a new Motor instance that is composed of two other motors that will be controlled identically. This is useful
     * when multiple motors are controlled in the same way, such as on a {@link DriveTrain}.
     * 
     * @param motor1 the first motor, and the motor from which the speed is read; may not be null
     * @param motor2 the second motor; may not be null
     * @return the composite motor; never null
     */
    static Motor compose(Motor motor1, Motor motor2) {
        return new Motor() {
            @Override
            public double getSpeed() {
                return motor1.getSpeed();
            }

            @Override
            public void setSpeed(double speed) {
                motor1.setSpeed(speed);
                motor2.setSpeed(speed);
            }
        };
    }

    /**
     * Create a new Motor instance that is composed of three other motors that will be controlled identically. This is useful when
     * multiple motors are controlled in the same way, such as on a {@link DriveTrain}.
     * 
     * @param motor1 the first motor, and the motor from which the speed is read; may not be null
     * @param motor2 the second motor; may not be null
     * @param motor3 the third motor; may not be null
     * @return the composite motor; never null
     */
    static Motor compose(Motor motor1, Motor motor2, Motor motor3) {
        return new Motor() {
            @Override
            public double getSpeed() {
                return motor1.getSpeed();
            }

            @Override
            public void setSpeed(double speed) {
                motor1.setSpeed(speed);
                motor2.setSpeed(speed);
                motor3.setSpeed(speed);
            }
        };
    }
}
