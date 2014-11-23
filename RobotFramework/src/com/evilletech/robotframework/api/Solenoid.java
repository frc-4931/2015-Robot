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
     * @return the current position; never null
     */
    Position position();

    /**
     * Get the current action of this solenoid.
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
	 * @return <b>true</b> if this <code>Solenoid</code> is fully extended;
	 *         <b>false</b> otherwise
	 */
	default boolean isExtended() {
	    return position() == Position.EXTENDED;
	}

	/**
	 * Tests if this <code>Solenoid</code> is retracted.
	 * 
	 * @return <b>true</b> if this <code>Solenoid</code> is fully retracted;
	 *         <b>false</b> otherwise
	 */
	default boolean isRetracted() {
        return position() == Position.RETRACTED;
	}

	/**
	 * Tests if this <code>Solenoid</code> is extending.
	 * 
	 * @return <b>true</b> if this <code>Solenoid</code> is in the process of
	 *         extending, but is not fully extended yet; <b>false</b> otherwise
	 */
	default boolean isExtending() {
	    return action() == Action.EXTENDING;
	}

	/**
	 * Tests if this <code>Solenoid</code> is retracting.
	 * 
	 * @return <b>true</b> if this <code>Solenoid</code> is in the process of
	 *         retracting, but is not fully retracted yet; <b>false</b>
	 *         otherwise
	 */
	default boolean isRetracting() {
	    return action() == Action.RETRACTING;
	}
}
