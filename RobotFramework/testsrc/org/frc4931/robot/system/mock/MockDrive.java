/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system.mock;

import org.fest.assertions.Delta;
import org.frc4931.robot.system.Drive;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class MockDrive implements Drive {
    private double leftSpeed;
    private double rightSpeed;
    
    @Override
    public void tank(double leftSpeed, double rightSpeed, boolean squaredInputs) {
        if(squaredInputs) {
            leftSpeed = leftSpeed * leftSpeed * Math.signum(leftSpeed);
            rightSpeed = rightSpeed * rightSpeed * Math.signum(rightSpeed);
        }
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
    }

    @Override
    public void cheesy(double throttle, double wheel, boolean isQuickTurn) {
        throw new NotImplementedException();
    }

    public void assertDrivingForward() {
        assertThat(leftSpeed).isGreaterThan(0);
        assertThat(rightSpeed).isGreaterThan(0);
    }
    
    public void assertDrivingBackwards() {
        assertThat(leftSpeed).isLessThan(0);
        assertThat(rightSpeed).isLessThan(0);
    }
    
    public void assertNotTranslating() {
        assertThat(leftSpeed+rightSpeed).isEqualTo(0, Delta.delta(0.01));
    }
    
    public void assertTurningLeft() {
        assertThat(rightSpeed).isGreaterThan(leftSpeed);
    }
    
    public void assertTurningRight() {
        assertThat(leftSpeed).isGreaterThan(rightSpeed);
    }
    
    public void assertNotTurning() {
        assertThat(leftSpeed).isEqualTo(rightSpeed, Delta.delta(0.01));
    }
    
    public void assertStopped() {
        assertThat(leftSpeed).isEqualTo(0, Delta.delta(0.01));
        assertThat(rightSpeed).isEqualTo(0, Delta.delta(0.01));
    }
}
