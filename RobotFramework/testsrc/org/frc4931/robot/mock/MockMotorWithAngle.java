/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.mock;

import org.frc4931.robot.component.MotorWithAngle;

/**
 * Test Motor with a position
 * @author Nathan Brown
 */
public class MockMotorWithAngle implements MotorWithAngle {
    private double speed;
    private double angle;
    private double tolerance;
    
    @Override
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public short getSpeedAsShort() {
        return 0;
    }

    @Override
    public double getAngle() {
        return angle;
    }

    @Override
    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    @Override
    public void setTolerance(double tol) {
        tolerance = tol;
    }

}
