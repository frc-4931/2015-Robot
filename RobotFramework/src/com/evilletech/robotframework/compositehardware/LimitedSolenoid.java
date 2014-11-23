package com.evilletech.robotframework.compositehardware;

import com.evilletech.robotframework.api.Solenoid;
import com.evilletech.robotframework.api.Switch;

/**
 * A <code>Solenoid</code> with physical sensors for position awareness. This
 * class is a composition of one <code>Solenoid</code> and two
 * <code>Switch</code>es.
 * 
 * @author Zach Anderson
 * 
 */
public class LimitedSolenoid implements Solenoid {
	private static final int RETRACTED = 0;
	private static final int EXTENDED = 1;

	private final Solenoid solenoid;
	private final Switch retractSwitch;
	private final Switch extendSwitch;

	private int logicalState = RETRACTED;

	public LimitedSolenoid(Solenoid solenoid, Switch retractSwitch,
			Switch extendSwitch) {
		this.solenoid = solenoid;
		this.retractSwitch = retractSwitch;
		this.extendSwitch = extendSwitch;
	}

	public void extend() {
		solenoid.extend();
		logicalState = EXTENDED;
	}

	public void retract() {
		solenoid.retract();
		logicalState = RETRACTED;
	}

	public boolean isExtended() {
		return extendSwitch.isTriggered();
	}

	public boolean isRetracted() {
		return retractSwitch.isTriggered();
	}

	public boolean isExtending() {
		return logicalState == EXTENDED && !isExtended();
	}

	public boolean isRetracting() {
		return logicalState == RETRACTED && !isRetracted();
	}
}
