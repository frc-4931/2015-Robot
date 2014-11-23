package com.evilletech.robotframework.hardware;

import com.evilletech.robotframework.api.Solenoid;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Wrapper for WPILib <code>DoubleSolenoid</code>. This class should be
 * constructed through <code>HardwareFactory</code>.
 * 
 * @author Zach Anderson
 * @see Solenoid
 * @see HardwareFactory
 * @see edu.wpi.first.wpilibj.DoubleSolenoid
 */
class HardwareDoubleSolenoid implements Solenoid {
	private static final int RETRACTED = 0;
	private static final int EXTENDED = 1;
	private final DoubleSolenoid solenoid;

	private int logicalState = RETRACTED;

	HardwareDoubleSolenoid(int extendChannel, int retractChannel) {
		solenoid = new DoubleSolenoid(extendChannel, retractChannel);
	}

	public void extend() {
		solenoid.set(Value.kForward);
		logicalState = EXTENDED;
	}

	public void retract() {
		solenoid.set(Value.kReverse);
		logicalState = RETRACTED;
	}

	public boolean isExtended() {
		return solenoid.get().equals(Value.kForward);
	}

	public boolean isRetracted() {
		return solenoid.get().equals(Value.kReverse);
	}

	public boolean isExtending() {
		return logicalState == EXTENDED && !isExtended();
	}

	public boolean isRetracting() {
		return logicalState == RETRACTED && !isRetracted();
	}

}
