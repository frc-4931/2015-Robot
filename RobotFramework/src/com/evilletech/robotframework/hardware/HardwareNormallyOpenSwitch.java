/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package com.evilletech.robotframework.hardware;

import com.evilletech.robotframework.api.Switch;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Wrapper for <code>DigitalInput</code> in WPILib. This class is used for
 * switches that are normally open. This class must be constructed through
 * <code>HardwareFactory</code>.
 * 
 * @author Zach Anderson
 * @see Switch
 * @see Hardware
 * @see edu.wpi.first.wpilibj.DigitalInput
 */
final class HardwareNormallyOpenSwitch implements Switch {
	private final DigitalInput input;

	HardwareNormallyOpenSwitch(DigitalInput input) {
	    this.input = input;
	}

	@Override
    public boolean isTriggered() {
		return input.get();
	}
}
