package com.evilletech.robotframework.mockhardware;

import com.evilletech.robotframework.api.Relay;
import com.evilletech.robotframework.api.Solenoid;
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

	/**
	 * Creates a {@link MockSwitch} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Switch</code> that is initially triggered
	 */
	public static Switch newTriggeredSwitch() {
		return new MockSwitch(true);
	}

	/**
	 * Creates a {@link MockSwitch} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Switch</code> that is initially untriggered
	 */
	public static Switch newUntriggeredSwitch() {
		return new MockSwitch(false);
	}

	/**
	 * Creates a {@link Solenoid} that does not interface with any hardware
	 * components, but still behaves as if it did.
	 * 
	 * @return a <code>Relay</code> that is initially extended
	 */
	public static Solenoid newExtendedSolenoid() {
		return new MockSolenoid(MockSolenoid.EXTENDED);
	}

	/**
	 * Creates a {@link Solenoid} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Solenoid</code> that is initially retracted
	 */
	public static Solenoid newRetractedSolenoid() {
		return new MockSolenoid(MockSolenoid.RETRACTED);
	}

	/**
	 * Creates a {@link Solenoid} that does not interface with any hardware
	 * components, but still behaves as if it was.
	 * 
	 * @return a <code>Solenoid</code> that is initially neither extended or
	 *         retracted
	 */
	public static Solenoid newUnknownSolenoid() {
		return new MockSolenoid(MockSolenoid.UNKNOWN);
	}
}
