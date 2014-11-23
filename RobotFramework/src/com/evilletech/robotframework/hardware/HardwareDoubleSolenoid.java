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
	private final DoubleSolenoid solenoid;

    private Action action = Action.OFF;
    private Position position = Position.UNKNOWN;

	HardwareDoubleSolenoid(int extendChannel, int retractChannel) {
		solenoid = new DoubleSolenoid(extendChannel, retractChannel);
	}
	
	protected void checkState() {
	    switch ( action ) {
	        case EXTENDING:
	            if ( solenoid.get().equals(Value.kOff) ) {
	                // We're done extending ...
	                position = Position.EXTENDED;
	                action = Action.OFF;
	            } else if ( solenoid.get().equals(Value.kForward) ) {
	                // Still extending ....
                    position = Position.UNKNOWN;
	                action = Action.EXTENDING;
	            } else if ( solenoid.get().equals(Value.kReverse)) {
	                // We've stopped extending and are now retracting ...
                    position = Position.UNKNOWN;
	                action = Action.RETRACTING;
	            }
	            break;
	        case RETRACTING:
                if ( solenoid.get().equals(Value.kOff) ) {
                    // We're done retracting ...
                    position = Position.RETRACTED;
                    action = Action.OFF;
                } else if ( solenoid.get().equals(Value.kForward) ) {
                    // We've stopped retracting and are now extending ...
                    position = Position.UNKNOWN;
                    action = Action.EXTENDING;
                } else if ( solenoid.get().equals(Value.kReverse)) {
                    // Still retracting ....
                    position = Position.UNKNOWN;
                    action = Action.RETRACTING;
                }
                break;
	        case OFF:
                if ( solenoid.get().equals(Value.kOff) ) {
                    // We're still done, so don't change the position ...
                    action = Action.OFF;
                } else if ( solenoid.get().equals(Value.kForward) ) {
                    // We're now extending ...
                    position = Position.UNKNOWN;
                    action = Action.EXTENDING;
                } else if ( solenoid.get().equals(Value.kReverse)) {
                    // We're now retracting ...
                    position = Position.UNKNOWN;
                    action = Action.RETRACTING;
                }
	    }
	}

    @Override
    public Position position() {
        checkState();
        return position;
    }
    
    @Override
    public Action action() {
        checkState();
        return action;
    }

    @Override
    public void extend() {
		solenoid.set(Value.kForward);
		action = Action.EXTENDING;
		checkState();
	}

	@Override
    public void retract() {
		solenoid.set(Value.kReverse);
        action = Action.RETRACTING;
        checkState();
	}
}
