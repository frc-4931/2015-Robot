package com.evilletech.robotframework.mockhardware;

import com.evilletech.robotframework.api.Switch;

/**
 * A test implementation of <code>Switch</code> that can be used without any
 * hardware. This class must be constructed through <code>MockFactory</code>.
 * This class provides methods to set the value as well as get it, as it does
 * not interface with actual hardware.
 * 
 * @author Zach Anderson
 * @see Switch
 * @see MockFactory
 */
class MockSwitch implements Switch {
	private boolean triggered;

	MockSwitch(boolean initialState) {
		triggered = initialState;
	}

	public boolean isTriggered() {
		return triggered;
	}

	public void setTriggered() {
		triggered = true;
	}

	public void setNotTriggered() {
		triggered = false;
	}
}
