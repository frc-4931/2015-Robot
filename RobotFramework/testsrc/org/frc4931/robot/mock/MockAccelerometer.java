/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.mock;

import org.frc4931.robot.component.Accelerometer;

/**
 * A test implementation of {@link Accelerometer} that does not
 * require any hardware to use.
 * 
 * @author Zach Anderson
 */
public final class MockAccelerometer implements Accelerometer{
    private double xAccel;
    private double yAccel;
    private double zAccel;
    
    public void setAcceleration(double x, double y, double z){
        xAccel = x;
        yAccel = y;
        zAccel = z;
    }
    
    @Override
    public double getXacceleration() {
        return xAccel;
    }
    
    @Override
    public double getYacceleration() {
        return yAccel;
    }
    
    @Override
    public double getZacceleration() {
        return zAccel;
    }
}
