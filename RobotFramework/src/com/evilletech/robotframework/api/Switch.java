package com.evilletech.robotframework.api;

/**
 * A switch is any readable device that has am active state when it is triggered
 * and an inactive state when it isn't.
 * 
 * @author Zach Anderson
 * 
 */
public interface Switch {
	/**
	 * Tests if this <code>Switch</code> is triggered.
	 * 
	 * @return <b>true</b> if this <code>Switch</code> is not in its resting
	 *         state; <b>false</b> otherwise
	 */
	public boolean isTriggered();
}
