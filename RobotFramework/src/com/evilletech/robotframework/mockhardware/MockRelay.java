package com.evilletech.robotframework.mockhardware;

import com.evilletech.robotframework.api.Relay;

/**
 * A test implementation of <code>Relay</code> that does not require any
 * hardware to use. This class cannot be constructed directly, use
 * <code>MockFactory</code> to get instances of it.
 * 
 * @author Zach Anderson
 * @see Relay
 * @see MockFactory
 */
class MockRelay implements Relay {
	static final int ON = 1;
	static final int OFF = 0;
	static final int UNKNOWN = -1;

	int state;

	MockRelay(int initalState) {
		state = initalState;
	}

	public void on() {
		state = ON;
	}

	public void off() {
		state = OFF;
	}

	public boolean isOn() {
		return state == ON;
	}

	public boolean isOff() {
		return state == OFF;
	}

	// TODO implement a delay
	public boolean isSwitchingOn() {
		// Mock state changes are instant
		return false;
	}

	// TODO implement a delay
	public boolean isSwitchingOff() {
		// Mock state changes are instant
		return false;
	}

}
