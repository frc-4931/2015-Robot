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
 */
public class MockSwitch implements Switch {

	/**
	 * Creates a {@link MockSwitch} initialized as {@link Switch#isTriggered() untriggered} that does not interface
	 * with any hardware components, but still behaves as if it was.
	 * 
	 * @return the mock switch
	 */
	public static MockSwitch notTriggered() {
		return new MockSwitch(false);
	}

	/**
	 * Creates a {@link MockSwitch} initialized as {@link Switch#isTriggered() triggered} that does not interface
	 * with any hardware components, but still behaves as if it was.
	 * 
	 * @return the mock switch
	 */
	public static MockSwitch triggered() {
		return new MockSwitch(true);
	}

	
	private boolean triggered;

	MockSwitch(boolean initialState) {
		triggered = initialState;
	}

	@Override
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
