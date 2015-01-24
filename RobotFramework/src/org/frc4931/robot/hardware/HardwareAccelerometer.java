/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.ADXL345_I2C.AllAxes;
import edu.wpi.first.wpilibj.I2C;
import org.frc4931.robot.component.Accelerometer;
import org.frc4931.utils.Triple;

/**
 * Wrapper for the WPILib {@link ADXL345_I2C}.
 * 
 * @author Zach Anderson
 */
final class HardwareAccelerometer implements Accelerometer{
    private ADXL345_I2C accel;
    
    private long lastUpdate = 0;
    
    private double xAccel;
    private double yAccel;
    private double zAccel;
    
    private double xVel;
    private double yVel;
    private double zVel;
    
    private double xDisp;
    private double yDisp;
    private double zDisp;
    
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
    public Triple getAcceleration() {
        return new Triple(xAccel,yAccel,zAccel);
    }

    @Override
    public Triple getVelocity() {
        return new Triple(xVel,yVel,zVel);
    }

    @Override
    public Triple getDisplacement() {
        return new Triple(xDisp,yDisp,zDisp);
    }

    @Override
    public void update() {
        long now = System.nanoTime();
        long delta = now-lastUpdate;
        
        double d = delta/1.0e-9;
        
        AllAxes a = accel.getAccelerations();
        
        xAccel = a.XAxis;
        yAccel = a.YAxis;
        zAccel = a.ZAxis;
        
        xVel += xAccel*d;
        yVel += yAccel*d;
        zVel += zAccel*d;
        
        xDisp += xVel*d;
        yDisp += yVel*d;
        zDisp += zVel*d;
    }
}
