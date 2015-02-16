/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system.mock;

import org.fest.assertions.Delta;
import org.frc4931.robot.component.DriveTrain;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class MockDrive implements DriveTrain {
    private double leftSpeed;
    private double rightSpeed;
    
    @Override
    public void drive(double leftSpeed, double rightSpeed) {
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
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
