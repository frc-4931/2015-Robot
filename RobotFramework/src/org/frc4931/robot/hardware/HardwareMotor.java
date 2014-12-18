/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.Motor;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

/**
 * Wrapper for WPILib {@link SpeedController}.
 * 
 * @author Zach Anderson
 * @see Motor
 * @see Hardware
 * @see edu.wpi.first.wpilibj.SpeedController
 */
public final class HardwareMotor implements Motor {
	// TODO Make this an enum?
	static final int TALON = 1;
	static final int JAGUAR = 2;
	static final int VICTOR = 3;

	private final SpeedController controller;

	HardwareMotor(int port, int type) {
		if (type == TALON)
			controller = new Talon(port);
		else if (type == JAGUAR)
			controller = new Jaguar(port);
		else if (type == VICTOR)
			controller = new Victor(port);
		else
			controller = null;
	}

	public void setSpeed(double speed) {
		controller.set(validateSpeed(speed));
	}

	/**
	 * Tests if the specified speed is within the motor range. If not clamps the
	 * specified value to [-1.0, 1.0].
	 * 
	 * @param speed
	 *            the value to clamp
	 * @return the clamped value
	 */
	// TODO Implement error logging
	private double validateSpeed(double speed) {
		speed = Math.min(1.0, speed);
		speed = Math.max(-1.0, speed);
		return speed;
	}

	public double getSpeed() {
		return controller.get();
	}
}
