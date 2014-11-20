package com.evilletech.robotframework.mockhardware;

import com.evilletech.robotframework.api.Solenoid;

/**
 * A test implementation of <code>Solenoid</code> that can be used without any
 * hardware. This class should be instantiated through <code>MockFactory</code>.
 * 
 * @author Zach Anderson
 * @see Solenoid
 * @see MockFactory
 */
class MockSolenoid implements Solenoid {
	static final int RETRACTED = 0;
	static final int EXTENDED = 1;
	static final int UNKNOWN = -1;

	private int state;

	MockSolenoid(int initialState) {
		state = initialState;
	}

	public void extend() {
		state = EXTENDED;
	}

	public void retract() {
		state = RETRACTED;
	}

	public boolean isExtended() {
		return state == EXTENDED;
	}

	public boolean isRetracted() {
		return state == RETRACTED;
	}

	public boolean isExtending() {
		return false;
	}

	public boolean isRetracting() {
		return false;
	}

}
