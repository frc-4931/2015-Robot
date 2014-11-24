/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * A solenoid is a device that can be extended and retracted.
 * 
 * @author Zach Anderson
 * 
 */
public interface Solenoid {

    static enum Direction {
        EXTENDING, RETRACTING;
    }

    /**
     * Get the current action of this solenoid.
     * 
     * @return the current action; never null
     */
    Direction getDirection();

    /**
     * Extends this <code>Solenoid</code>.
     */
    void extend();

    /**
     * Retracts this <code>Solenoid</code>.
     */
    void retract();

    /**
     * Tests if this <code>Solenoid</code> is extending.
     * 
     * @return {@code true} if this solenoid is in the process of extending but not yet fully extended, or {@code false} otherwise
     */
    default boolean isExtending() {
        return getDirection() == Direction.EXTENDING;
    }

    /**
     * Tests if this <code>Solenoid</code> is retracting.
     * 
     * @return {@code true} if this solenoid is in the process of retracting but not yet fully retracted, or {@code false} otherwise
     */
    default boolean isRetracting() {
        return getDirection() == Direction.RETRACTING;
    }
}
