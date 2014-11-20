package com.evilletech.robotframework.api;

/**
 * A solenoid is a device that can be extended and retracted.
 * 
 * @author Zach Anderson
 * 
 */
public interface Solenoid {
	/**
	 * Extends this <code>Solenoid</code>.
	 */
	public void extend();

	/**
	 * Retracts this <code>Solenoid</code>.
	 */
	public void retract();

	/**
	 * Tests if this <code>Solenoid</code> is extended.
	 * 
	 * @return <b>true</b> if this <code>Solenoid</code> is fully extended;
	 *         <b>false</b> otherwise
	 */
	public boolean isExtended();

	/**
	 * Tests if this <code>Solenoid</code> is retracted.
	 * 
	 * @return <b>true</b> if this <code>Solenoid</code> is fully retracted;
	 *         <b>false</b> otherwise
	 */
	public boolean isRetracted();

	/**
	 * Tests if this <code>Solenoid</code> is extending.
	 * 
	 * @return <b>true</b> if this <code>Solenoid</code> is in the process of
	 *         extending, but is not fully extended yet; <b>false</b> otherwise
	 */
	public boolean isExtending();

	/**
	 * Tests if this <code>Solenoid</code> is retracting.
	 * 
	 * @return <b>true</b> if this <code>Solenoid</code> is in the process of
	 *         retracting, but is not fully retracted yet; <b>false</b>
	 *         otherwise
	 */
	public boolean isRetracting();
}
