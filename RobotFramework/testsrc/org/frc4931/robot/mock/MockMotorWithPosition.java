/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.mock;

import org.frc4931.robot.component.MotorWithPosition;

/**
 * Test Motor with a position
 * @author Nathan Brown
 */
public class MockMotorWithPosition implements MotorWithPosition {
    private double speed;
    private double pos;
    
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
    public double getPosition() {
        return pos;
    }

    @Override
    public void setPosition(double pos) {
        this.pos = pos;
    }

}
