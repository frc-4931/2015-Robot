/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Wrapper for <code>DigitalInput</code> in WPILib. This class is used for
 * switches that are normally closed. This class must be constructed through
 * <code>HardwareFactory</code>.
 * 
 * @author Zach Anderson
 * @see Switch
 * @see Hardware
 * @see edu.wpi.first.wpilibj.DigitalInput
 */
final class HardwareNormallyClosedSwitch implements Switch {
	private final DigitalInput input;

	HardwareNormallyClosedSwitch(DigitalInput input) {
		this.input = input;
	}

	@Override
    public boolean isTriggered() {
		return !input.get();
	}
}
