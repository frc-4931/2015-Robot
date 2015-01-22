/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import org.frc4931.robot.Robot;
import org.frc4931.robot.command.drive.DriveForwardWithDuration;
import org.frc4931.robot.component.Motor;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DriveForwardWithDurationTest extends AbstractCommandTest {
    private Command currentCommand = null;

    @Override
    protected Command createCommand(Robot.Systems systems) {
        currentCommand = new DriveForwardWithDuration(systems.drive, 0.5, 0.05);
        return currentCommand;
    }

    @Test
    public void testInitialization() {
        assertStopped();
    }

    @Test
    public void shouldRunWhileNotTimedOut() {
        repeat(10, () -> runCommandAnd(this::assertDrivingForwardIfSupposedTo));
    }

    private void assertDrivingForward() {
        assertThat(robot.leftDriveMotor().getDirection()).isEqualTo(Motor.Direction.FORWARD);
        assertThat(robot.rightDriveMotor().getDirection()).isEqualTo(Motor.Direction.FORWARD);
    }

    private void assertStopped() {
        assertThat(robot.leftDriveMotor().getDirection()).isEqualTo(Motor.Direction.STOPPED);
        assertThat(robot.rightDriveMotor().getDirection()).isEqualTo(Motor.Direction.STOPPED);
    }

    private void assertDrivingForwardIfSupposedTo() {
        if (lastCommandEnded()) {
            assertStopped();
        } else {
            assertDrivingForward();
        }
    }
}
