package com.evilletech.robotframework.hardware;

import com.evilletech.robotframework.api.Relay;
import com.evilletech.robotframework.api.Solenoid;
import com.evilletech.robotframework.api.Switch;

/**
 * The factory for all devices that directly interface with robot hardware.
 * <code>HardwareFactory</code> provides factory methods for all physical
 * hardware components.
 * 
 * @author Zach Anderson
 * @see Relay
 */
public class HardwareFactory {
	/**
	 * Creates a relay on the specified channel.
	 * 
	 * @param channel
	 *            the channel the relay is connected to
	 * @return a relay on the specified channel
	 */
	public static Relay newHardwareRelay(int channel) {
		return new HardwareRelay(channel);
	}

	/**
	 * Creates a generic normally closed switch on the specified channel.
	 * 
	 * @param channel
	 *            the channel the switch is connected to
	 * @return a switch on the specified channel
	 */
	public static Switch newNCSwitch(int channel) {
		return new HardwareNCSwitch(channel);
	}

	/**
	 * Creates a generic normally open switch on the specified channel.
	 * 
	 * @param channel
	 *            the channel the switch is connected to
	 * @return a switch on the specified channel
	 */
	public static Switch newNOSwitch(int channel) {
		return new HardwareNOSwitch(channel);
	}

	/**
	 * Creates a <code>Solenoid</code> on the specified channels
	 * 
	 * @param extendChannel
	 *            the channel that extends the solenoid
	 * @param retractChannel
	 *            the channel that retracts the solenoid
	 * @return a <code>Solenoid</code> on the specified channels
	 */
	public static Solenoid newDoubleSolenoid(int extendChannel,
			int retractChannel) {
		return new HardwareDoubleSolenoid(extendChannel, retractChannel);
	}
}
