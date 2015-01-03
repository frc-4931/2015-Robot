/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.robot.component.Motor.Direction;
import org.frc4931.robot.hardware.Hardware.Sensors.Switches;

/**
 * A {@link Motor} that is bounded by two {@link Switches} at the extremes of
 * it's range of motion.
 * <p>
 * {@link LimitedMotor} has three possible positions:
 * <ol>
 * <li> {@code HIGH} - the high switch is active</li>
 * <li> {@code LOW} - the low switch is active</li>
 * <li> {@code UNKNOWN} - either both switches are active or neither is</li>
 * </ol>
 * </p>
 * <p>
 * and three possible directions:
 * <ol>
 * <li> {@code FORWARD} - the underlying motor is moving to the high limit</li>
 * <li> {@code REVERSE} - the underlying motor is moving to the low limit</li>
 * <li> {@code STOPPED} - the underlying motor is not moving</li>
 * </ol>
 * </p>
 * 
 * @author Zach Anderson
 * @see Motor
 * @see Switch
 */
public final class LimitedMotor {
    public enum Position {
        HIGH, LOW, UNKNOWN
    }

    private final Motor motor;
    private final Switch highSwitch;
    private final Switch lowSwitch;

    /**
     * Constructs a {@link LimitedMotor} with the specified parameters.
     * 
     * @param motor
     *            the {@link Motor} being limited
     * @param highSwitch
     *            the {@link Switch} at the highest extreme of motion
     * @param lowSwitch
     *            the {@link Switch} at the lowest extreme of motion
     */
    public LimitedMotor(Motor motor, Switch highSwitch, Switch lowSwitch) {
        this.motor = motor;
        this.highSwitch = highSwitch;
        this.lowSwitch = lowSwitch;
    }

    /**
     * Moves this {@link LimitedMotor} towards the high limit. This method
     * should be called once per loop until the movement is completed, otherwise
     * the watchdog will stop the movement.
     * 
     * @param speed
     *            the speed to move the underlying {@link Motor} at
     */
    public void moveTowardsHigh(double speed) {
        // Motor protection
        if (!isHigh())
            motor.setSpeed(Math.abs(speed));
        else
            motor.stop();
    }

    /**
     * Moves this {@link LimitedMotor} towards the low limit. This method should
     * be called once per loop until the movement is completed, otherwise the
     * watchdog will stop the movement.
     * 
     * @param speed
     *            the speed to move the underlying {@link Motor} at
     */
    public void moveTowardsLow(double speed) {
        // Motor protection
        if (!isLow())
            motor.setSpeed(-Math.abs(speed));
        else
            motor.stop();
    }

    /**
     * Tests if this {@link LimitedMotor} is at the high limit.
     * 
     * @return {@code true} if this {@link LimitedMotor} is at the high limit;
     *         {@code false} otherwise
     */
    public boolean isHigh() {
        return highSwitch.isTriggered();
    }

    /**
     * Tests if this {@link LimitedMotor} is at the low limit.
     * 
     * @return {@code true} if this {@link LimitedMotor} is at the low limit;
     *         {@code false} otherwise
     */
    public boolean isLow() {
        return lowSwitch.isTriggered();
    }

    /**
     * Gets the direction the underlying {@link Motor} is turning. Can be
     * {@code FORWARD}, {@code REVERSE}, or {@code STOPPED}.
     * 
     * @return a {@link Direction} representing the current direction of this
     * {@link LimitedMotor}.
     */
    public Motor.Direction getDirection() {
        return motor.getDirection();
    }

    /**
     * Gets the current position of this {@link LimitedMotor}. Can be
     * {@code HIGH}, {@code LOW}, or {@code UNKNOWN}.
     * 
     * @return a {@link Position} representing the current position of this
     *         {@link LimitedMotor}
     */
    public Position getPosition() {
        boolean high = isHigh();
        boolean low = isLow();

        if (high && !low)
            return Position.HIGH;
        else if (low && !high)
            return Position.LOW;
        else
            return Position.UNKNOWN;
    }

    /**
     * Stops the underlying {@link Motor}.
     */
    public void stop() {
        motor.stop();
    }
}
