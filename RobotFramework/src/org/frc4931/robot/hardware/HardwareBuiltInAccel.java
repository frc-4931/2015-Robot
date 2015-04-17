/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.Accelerometer;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;

/**
 * 
 */
public class HardwareBuiltInAccel implements Accelerometer{
    private final BuiltInAccelerometer accel;
    
    HardwareBuiltInAccel() {
        accel = new BuiltInAccelerometer();
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
