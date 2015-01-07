/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component.mock;

import org.frc4931.robot.component.Motor;

/**
 * A implementation of {@link Motor} for testing that does not require any
 * hardware to use. This class cannot be constructed directly, use the provided
 * static methods to get instances.
 * 
 * @author Zach Anderson
 * @see Motor
 */
public final class MockMotor implements Motor {
    /**
     * Constructs a new {@link MockMotor} that is initially stopped. Same as
     * calling {@code running(0.0)}.
     * 
     * @return a {@link MockMotor} with 0 speed
     */
    public static MockMotor stopped() {
        return new MockMotor(0.0);
    }

    /**
     * Constructs a new {@link MockMotor} with some initial speed.
     * 
     * @param speed
     *            the initial speed of the new {@link MockMotor}
     * @return a {@link MockMotor} with speed {@code speed}
     */
    public static MockMotor running(double speed) {
        return new MockMotor(speed);
    }

    double speed = 0;

    private MockMotor(double speed) {
        this.speed = speed;
    }

    @Override
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

}
