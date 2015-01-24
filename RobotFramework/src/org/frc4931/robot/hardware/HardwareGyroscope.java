/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import edu.wpi.first.wpilibj.Gyro;
import org.frc4931.robot.component.Gyroscope;

/**
 * Wrapper for WPILib {@link Gyro}. Should be constructed by
 * {@link Hardware.Sensors#gyroscope(int)}.
 * 
 * @author Zach Anderson
 * @see Gyroscope
 * @see Hardware
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
    public double getRawAngle() {
        return gyroscope.getAngle();
    }

    @Override
    public double getRate() {
        return gyroscope.getRate();
    }
}
