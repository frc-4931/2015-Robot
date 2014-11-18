package com.evilletech.robotframework.hardware;

import com.evilletech.robotframework.api.Relay;

import edu.wpi.first.wpilibj.Relay.Value;

/**
 * Wrapper for the WPILib <code>Relay</code>. This class cannot be constructed
 * directly, use <code>HardwareFactory</code> to get instances of it.
 * 
 * @author Zach Anderson
 * @see Relay
 * @see HardwareFactory
 * @see edu.wpi.first.wpilibj.Relay
 */
public class HardwareRelay implements Relay {
	private static final int ON = 1;
	private static final int OFF = 0;

	private final edu.wpi.first.wpilibj.Relay relay;

	private int logicalState;

	HardwareRelay(int channel) {
		relay = new edu.wpi.first.wpilibj.Relay(channel);
	}

	public void on() {
		relay.set(Value.kOn);
		logicalState = ON;
	}

	public void off() {
		relay.set(Value.kOff);
		logicalState = OFF;
	}

	public boolean isOn() {
		return relay.get() == Value.kOn;
	}

	public boolean isOff() {
		return relay.get() == Value.kOn;
	}

	public boolean isSwitchingOn() {
		// If we have been set to on and we are not on yet
		return logicalState == ON && !isOn();
	}

	public boolean isSwitchingOff() {
		// If we have been set to off and we are not off yet
		return logicalState == OFF && !isOff();
	}

}
