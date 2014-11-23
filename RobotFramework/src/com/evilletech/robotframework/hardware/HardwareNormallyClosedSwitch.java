package com.evilletech.robotframework.hardware;

import com.evilletech.robotframework.api.Switch;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Wrapper for <code>DigitalInput</code> in WPILib. This class is used for
 * switches that are normally closed. This class must be constructed through
 * <code>HardwareFactory</code>.
 * 
 * @author Zach Anderson
 * @see Switch
 * @see HardwareFactory
 * @see edu.wpi.first.wpilibj.DigitalInput
 */
class HardwareNormallyClosedSwitch implements Switch {
	private final DigitalInput input;

	HardwareNormallyClosedSwitch(int channel) {
		input = new DigitalInput(channel);
	}

	@Override
    public boolean isTriggered() {
		return !input.get();
	}
}
