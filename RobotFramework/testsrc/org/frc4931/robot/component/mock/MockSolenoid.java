/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component.mock;

import org.frc4931.robot.component.Solenoid;

/**
 * A test implementation of <code>Solenoid</code> that can be used without any
 * hardware. This class should be instantiated through <code>MockFactory</code>.
 * 
 * @author Zach Anderson
 * @see Solenoid
 */
public final class MockSolenoid implements Solenoid {

    /**
     * Create an extending solenoid.
     * 
     * @return the solenoid
     */
    public static MockSolenoid extending() {
        return new MockSolenoid(Direction.EXTENDING);
    }

    /**
     * Create a retracting solenoid.
     * 
     * @return the solenoid
     */
    public static MockSolenoid retracting() {
        return new MockSolenoid(Direction.RETRACTING);
    }

    private Direction direction;

    private MockSolenoid(Direction action) {
        this.direction = action;
    }

    @Override
    public void extend() {
        direction = Direction.EXTENDING;
    }

    @Override
    public void retract() {
        direction = Direction.RETRACTING;
    }
    
    @Override
    public Direction getDirection() {
        return direction;
    }
    
    @Override
    public String toString() {
        return direction.toString();
    }
}
