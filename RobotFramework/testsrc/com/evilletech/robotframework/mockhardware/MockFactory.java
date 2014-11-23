package com.evilletech.robotframework.mockhardware;

import com.evilletech.robotframework.api.Relay;
import com.evilletech.robotframework.api.Switch;

/**
 * The factory for all mock hardware devices. A mock hardware device allows an
 * element of the robot to be simulated independent of the rest of the hardware
 * that would be required. <code>MockFactory</code> provides a factory method
 * for every initial state of all mock hardware devices.
 * 
 * @author Zach Anderson
 * @see Relay
 */
public class MockFactory {
	/**
	 * Creates a {@link MockRelay} initialized to {@link MockRelay#ON} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Relay</code> that is initially on
	 */
	public static MockRelay newOnRelay() {
		return new MockRelay(MockRelay.ON);
	}

	/**
	 * Creates a {@link MockRelay} initialized to {@link MockRelay#OFF} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Relay</code> that is initially off
	 */
	public static MockRelay newOffRelay() {
		return new MockRelay(MockRelay.OFF);
	}

	/**
	 * Creates a {@link MockRelay} initialized to {@link MockRelay#UNKNOWN} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Relay</code> that is initially neither off or on
	 */
	public static MockRelay newUnknownRelay() {
		return new MockRelay(MockRelay.UNKNOWN);
	}

	/**
	 * Creates a {@link MockSwitch} initialized as {@link Switch#isTriggered() triggered} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Switch</code> that is initially triggered
	 */
	public static MockSwitch newTriggeredSwitch() {
		return new MockSwitch(true);
	}

	/**
	 * Creates a {@link MockSwitch} initialized as {@link Switch#isTriggered() untriggered} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Switch</code> that is initially untriggered
	 */
	public static MockSwitch newUntriggeredSwitch() {
		return new MockSwitch(false);
	}
}
