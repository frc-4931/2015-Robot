/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.DistanceSensor;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * Wraps an {@link AnalogChannel} and provides the conversion to convert voltage
 * to distance for the NAME_OF_ULTRASONIC_SENSOR_FROM_2014.
 * 
 * @author Zach Anderson
 *
 */
// TODO Update class name to reflect name of specific sensor
final class HardwareAnalogUltrasonic implements DistanceSensor {
    private static final double VOLTS_TO_INCHES = 1000.0 / 9.8;
    private final AnalogChannel sensor;

    HardwareAnalogUltrasonic(int channel) {
        sensor = new AnalogChannel(channel);
    }

    @Override
    public double getDistance() {
        return sensor.getVoltage() * VOLTS_TO_INCHES;
    }
}
