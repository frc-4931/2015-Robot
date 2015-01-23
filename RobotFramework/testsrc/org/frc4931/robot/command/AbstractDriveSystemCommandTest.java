/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.component.Motor;
import org.junit.Before;

import static org.fest.assertions.Assertions.assertThat;

public abstract class AbstractDriveSystemCommandTest extends AbstractCommandTest {

    @Before
    @Override
    public void beforeEach() {
    }

    protected void assertTurningRightIfSupposedTo() {
        if (lastCommandEnded()) {
            assertStopped();
        } else {
            assertTurningRight();
        }
    }

    protected void assertTurningLeftIfSupposedTo() {
        if (lastCommandEnded()) {
            assertStopped();
        } else {
            assertTurningLeft();
        }
    }

    protected void assertDrivingForwardIfSupposedTo() {
        if (lastCommandEnded()) {
            assertStopped();
        } else {
            assertDrivingForward();
        }
    }

    protected void assertDrivingBackwardIfSupposedTo() {
        if (lastCommandEnded()) {
            assertStopped();
        } else {
            assertDrivingBackward();
        }
    }

    protected void assertTurningRight() {
        assertThat(robot.leftDriveMotor().getDirection()).isEqualTo(Motor.Direction.FORWARD);
        assertThat(robot.rightDriveMotor().getDirection()).isEqualTo(Motor.Direction.REVERSE);
    }

    protected void assertTurningLeft() {
        assertThat(robot.leftDriveMotor().getDirection()).isEqualTo(Motor.Direction.REVERSE);
        assertThat(robot.rightDriveMotor().getDirection()).isEqualTo(Motor.Direction.FORWARD);
    }

    protected void assertDrivingBackward() {
        assertThat(robot.leftDriveMotor().getDirection()).isEqualTo(Motor.Direction.REVERSE);
        assertThat(robot.rightDriveMotor().getDirection()).isEqualTo(Motor.Direction.REVERSE);
    }

    protected void assertDrivingForward() {
        assertThat(robot.leftDriveMotor().getDirection()).isEqualTo(Motor.Direction.FORWARD);
        assertThat(robot.rightDriveMotor().getDirection()).isEqualTo(Motor.Direction.FORWARD);
    }

    protected void assertStopped() {
        assertThat(robot.leftDriveMotor().getDirection()).isEqualTo(Motor.Direction.STOPPED);
        assertThat(robot.rightDriveMotor().getDirection()).isEqualTo(Motor.Direction.STOPPED);
    }
}
