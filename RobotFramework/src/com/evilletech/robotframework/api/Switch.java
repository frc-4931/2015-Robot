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
	 * Checks if this switch is triggered.
	 * 
	 * @return {@code true} if this switch is not in its resting state, or {@code false} otherwise
	 */
	boolean isTriggered();
}
