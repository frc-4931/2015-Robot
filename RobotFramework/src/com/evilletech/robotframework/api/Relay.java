package com.evilletech.robotframework.api;

/**
 * A relay is a device that can be turned on and off.
 * 
 * @author Zach Anderson
 * 
 */
public interface Relay {
	/**
	 * Turn this <code>Relay</code> on.
	 */
	public void on();

	/**
	 * Turn this <code>Relay</code> off.
	 */
	public void off();

	/**
	 * Test if this <code>Relay</code> is on.
	 * 
	 * @return <b>true</b> if this <code>Relay</code> is on; <b>false</b>
	 *         otherwise.
	 */
	public boolean isOn();

	/**
	 * Test if this <code>Relay</code> is off.
	 * 
	 * @return <b>true</b> if this <code>Relay</code> is off; <b>false</b>
	 *         otherwise.
	 */
	public boolean isOff();

	/**
	 * Tests if this <code>Relay</code> is switching on.
	 * 
	 * @return <b>true</b> if this <code>Relay</code> is in the process of
	 *         switching from off to on; <b>false</b> otherwise
	 */
	public boolean isSwitchingOn();

	/**
	 * Tests if this <code>Relay</code> is switching off.
	 * 
	 * @return <b>true</b> if this <code>Relay</code> is in the process of
	 *         switching from on to off; <b>false</b> otherwise
	 */
	public boolean isSwitchingOff();

}
