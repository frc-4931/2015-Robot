/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.Motor;

import edu.wpi.first.wpilibj.SpeedController;

/**
 * Wrapper for WPILib {@link SpeedController}.
 * 
 * @author Zach Anderson
 * @see Motor
 * @see Hardware
 * @see edu.wpi.first.wpilibj.SpeedController
 */
public final class HardwareMotor implements Motor {
	private final SpeedController controller;

	HardwareMotor(SpeedController controller) {
		this.controller = controller;
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
