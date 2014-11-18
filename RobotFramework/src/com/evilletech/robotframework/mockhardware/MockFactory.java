package com.evilletech.robotframework.mockhardware;

import com.evilletech.robotframework.api.Relay;

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
	 * Creates a {@link Relay} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Relay</code> that is initially on
	 */
	public static Relay newOnRelay() {
		return new MockRelay(MockRelay.ON);
	}

	/**
	 * Creates a {@link Relay} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Relay</code> that is initially off
	 */
	public static Relay newOffRelay() {
		return new MockRelay(MockRelay.OFF);
	}

	/**
	 * Creates a {@link Relay} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Relay</code> that is initially neither off or on
	 */
	public static Relay newUnknownRelay() {
		return new MockRelay(MockRelay.UNKNOWN);
	}
}
