/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.Accelerometer;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.I2C;

/**
 * Wrapper for the WPILib {@link ADXL345_I2C}.
 * 
 * @author Zach Anderson
 */
final class HardwareAccelerometer implements Accelerometer{
    private ADXL345_I2C accel;
    
    public HardwareAccelerometer(int range) {
        if(range==2)
            accel = new ADXL345_I2C(I2C.Port.kOnboard, edu.wpi.first.wpilibj.interfaces.Accelerometer.Range.k2G);
        else if(range==4)
            accel = new ADXL345_I2C(I2C.Port.kOnboard, edu.wpi.first.wpilibj.interfaces.Accelerometer.Range.k4G);
        else if(range==8)
            accel = new ADXL345_I2C(I2C.Port.kOnboard, edu.wpi.first.wpilibj.interfaces.Accelerometer.Range.k8G);
        else{
            assert range == 16;
            accel = new ADXL345_I2C(I2C.Port.kOnboard, edu.wpi.first.wpilibj.interfaces.Accelerometer.Range.k16G);
        }
    }
    
    @Override
    public double getXacceleration() {
        return accel.getX();
    }

    @Override
    public double getYacceleration() {
        return accel.getY();
    }

    @Override
    public double getZacceleration() {
        return accel.getZ();
    }
}
