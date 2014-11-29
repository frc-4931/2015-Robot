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
 * @see HardwareFactory
 * @see edu.wpi.first.wpilibj.SpeedController
 */
public class HardwareMotor implements Motor {
	// TODO Make this an enum?
	private static final int TALON = 1;
	private static final int JAGUAR = 2;
	private static final int VICTOR = 3;

	private final SpeedController controller;

	private HardwareMotor(int port, int type) {
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
		speed = Math.min(1.0, speed);
		speed = Math.max(-1.0, speed);
		controller.set(speed);
	}

	public double getSpeed() {
		return controller.get();
	}

	public static Motor talonMotor(int channel) {
		return new HardwareMotor(channel, TALON);
	}

	public static Motor jaguarMotor(int channel) {
		return new HardwareMotor(channel, JAGUAR);
	}

	public static Motor victorMotor(int channel) {
		return new HardwareMotor(channel, VICTOR);
	}
}
