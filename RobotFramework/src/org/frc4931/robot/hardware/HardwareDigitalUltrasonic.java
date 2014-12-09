/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.DistanceSensor;

import edu.wpi.first.wpilibj.Ultrasonic;

/**
 * Wrapper for WPILib {@link Ultrasonic}. Should be constructed by
 * {@link Hardware.Sensors.Distance#digitalUltrasonic(int, int)}.
 * 
 * <p>
 * This type of ultrasonic sensor is controlled by two digital channels. To find
 * the distance the ping channel is set to high, triggering the sensor to send
 * out a short burst of ultrasonic sound. When the sensor detects the burst come
 * back, the echo channel becomes high. By finding the time between these two
 * events we can estimate the distance.
 * </p>
 * 
 * @author Zach Anderson
 * @see DistanceSensor
 * @see Hardwear
 * @see edu.wpi.first.wpilibj.Ultrasonic
 */
final class HardwareDigitalUltrasonic implements DistanceSensor {
	private final Ultrasonic ultrasonic;

	HardwareDigitalUltrasonic(int pingChannel, int echoChannel) {
		ultrasonic = new Ultrasonic(pingChannel, echoChannel);
		ultrasonic.setAutomaticMode(true);
	}

	@Override
	public double getDistance() {
		return ultrasonic.getRangeInches();
	}
}
