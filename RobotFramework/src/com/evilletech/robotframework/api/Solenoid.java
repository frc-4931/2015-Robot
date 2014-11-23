/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package com.evilletech.robotframework.api;

/**
 * A solenoid is a device that can be extended and retracted.
 * 
 * @author Zach Anderson
 * 
 */
public interface Solenoid {

    static enum Action {
        EXTENDING, RETRACTING, OFF;
    }

    static enum Position {
        EXTENDED, RETRACTED, UNKNOWN;
    }

    /**
     * Get the current position of this solenoid.
     * 
     * @return the current position; never null
     */
    Position position();

    /**
     * Get the current action of this solenoid.
     * 
     * @return the current action; never null
     */
    Action action();

    /**
     * Extends this <code>Solenoid</code>.
     */
    void extend();

    /**
     * Retracts this <code>Solenoid</code>.
     */
    void retract();

    /**
     * Tests if this <code>Solenoid</code> is extended.
     * 
     * @return {@code true} if this solenoid is fully extended, or {@code false} otherwise
     */
    default boolean isExtended() {
        return position() == Position.EXTENDED;
    }

    /**
     * Tests if this <code>Solenoid</code> is retracted.
     * 
     * @return {@code true} if this solenoid is fully retracted, or {@code false} otherwise
     */
    default boolean isRetracted() {
        return position() == Position.RETRACTED;
    }

    /**
     * Tests if this <code>Solenoid</code> is extending.
     * 
     * @return {@code true} if this solenoid is in the process of extending but not yet fully extended, or {@code false} otherwise
     */
    default boolean isExtending() {
        return action() == Action.EXTENDING;
    }

    /**
     * Tests if this <code>Solenoid</code> is retracting.
     * 
     * @return {@code true} if this solenoid is in the process of retracting but not yet fully retracted, or {@code false} otherwise
     */
    default boolean isRetracting() {
        return action() == Action.RETRACTING;
    }
}
