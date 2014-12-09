/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.Gyroscope;

import edu.wpi.first.wpilibj.Gyro;

/**
 * Wrapper for WPILib {@link Gyro}. Should be constructed by
 * {@link Hardware#gyroscope(int)}.
 * 
 * @author Zach Anderson
 * @see Gyroscope
 * @see Hardwear
 * @see edu.wpi.first.wpilibj.Gyro
 */
final class HardwareGyroscope implements Gyroscope {
	private final Gyro gyroscope;
	private double zeroPoint = 0;

	HardwareGyroscope(int channel) {
		gyroscope = new Gyro(channel);
	}

	@Override
	public double getAngle() {
		return gyroscope.getAngle() - zeroPoint;
	}

	@Override
	public void reset() {
		zeroPoint = gyroscope.getAngle();
	}

	@Override
	public double getTrueDisplacement() {
		return gyroscope.getAngle();
	}

	@Override
	public double getVelocity() {
		return gyroscope.getRate();
	}
}
