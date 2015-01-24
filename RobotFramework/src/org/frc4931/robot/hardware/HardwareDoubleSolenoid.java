/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import org.frc4931.robot.component.Solenoid;

/**
 * Wrapper for WPILib <code>DoubleSolenoid</code>. This class should be
 * constructed through <code>HardwareFactory</code>.
 * 
 * @author Zach Anderson
 * @see Solenoid
 * @see Hardware
 * @see edu.wpi.first.wpilibj.DoubleSolenoid
 */
final class HardwareDoubleSolenoid implements Solenoid {
	private final DoubleSolenoid solenoid;

    private Direction direction;

    HardwareDoubleSolenoid(DoubleSolenoid solenoid, Direction initialDirection ) {
        assert solenoid != null;
        assert initialDirection != null;
        this.solenoid = solenoid;
        this.direction = initialDirection;
        checkState();
    }
    
	protected void checkState() {
	    if ( solenoid.get() == Value.kForward ) {
	        direction = Direction.EXTENDING;
	    } else if ( solenoid.get() == Value.kReverse ) {
	        direction = Direction.RETRACTING;
	    }
	}

    @Override
    public Direction getDirection() {
        checkState();
        return direction;
    }

    @Override
    public void extend() {
		solenoid.set(Value.kForward);
		direction = Direction.EXTENDING;
		checkState();
	}

	@Override
    public void retract() {
		solenoid.set(Value.kReverse);
		direction = Direction.RETRACTING;
        checkState();
	}
	
	@Override
	public String toString() {
	    return "direction = " + direction;
	}
}
