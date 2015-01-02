/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component.mock;

import org.frc4931.robot.component.Accelerometer;
import org.frc4931.utils.Triple;

/**
 * A test implementation of {@link Accelerometer} that does not
 * require any hardware to use.
 * 
 * @author Zach Anderson
 */
public class MockAccelerometer implements Accelerometer{
    private double xAccel;
    private double yAccel;
    private double zAccel;
    
    private double xVel;
    private double yVel;
    private double zVel;
    
    private double xDisp;
    private double yDisp;
    private double zDisp;
    
    public void setAcceleration(double x, double y, double z){
        xAccel = x;
        yAccel = y;
        zAccel = z;
    }
    public void setVelocity(double x, double y, double z){
        xVel = x;
        yVel = y;
        zVel = z;
    }
    public void setDisplacement(double x, double y, double z){
        xDisp = x;
        yDisp = y;
        zDisp = z;
    }
    @Override
    public Triple getAcceleration() {
        return new Triple(xAccel, yAccel, zAccel);
    }

    @Override
    public Triple getVelocity() {
        return new Triple(xVel, yVel, zVel);
    }

    @Override
    public Triple getDisplacement() {
        return new Triple(xDisp, yDisp, zDisp);
    }

    @Override
    public void update() { }
}
