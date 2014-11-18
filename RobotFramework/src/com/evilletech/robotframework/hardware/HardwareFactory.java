package com.evilletech.robotframework.hardware;

import com.evilletech.robotframework.api.Relay;

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
}
