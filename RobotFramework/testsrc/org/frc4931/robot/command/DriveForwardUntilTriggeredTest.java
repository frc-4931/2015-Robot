/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import org.frc4931.robot.Robot;
import org.frc4931.robot.command.drive.DriveForwardUntilTriggered;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.mock.MockSwitch;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DriveForwardUntilTriggeredTest extends AbstractCommandTest {
    private MockSwitch swtch;

    @Override
    protected Command createCommand(Robot.Systems systems) {
        return new DriveForwardUntilTriggered(systems.drive, 0.5f, swtch);
    }

    @Before
    public void beforeEach() {
        swtch = MockSwitch.createNotTriggeredSwitch();
    }

    @Test
    public void testInitialization() {
        assertSwitchNotTriggered();
        assertStopped();
    }

    @Test
    public void shouldRunWhileSwitchNotTriggered() {
        repeat(10, () -> runCommandAnd(this::assertDrivingForwardIfSupposedTo, swtch::setTriggered));
        assertStopped();
    }

    @Test
    public void shouldNotRunWhileSwitchTriggered() {
        swtch.setTriggered();
        repeat(10, () -> runCommandAnd(this::assertStopped));
        assertStopped();
    }

    @Test
    public void shouldRunNormallyIfAlreadyDriving() {
        robot.systems().drive.arcade(1.0, 0.0);
        repeat(10, () -> runCommandAnd(this::assertDrivingForwardIfSupposedTo, swtch::setTriggered));
    }

    public void assertDrivingForwardIfSupposedTo() {
        if (swtch.isTriggered()) {
            assertStopped();
        } else {
            assertDrivingForward();
        }
    }

    public void assertDrivingForward() {
        assertThat(robot.leftDriveMotor().getDirection()).isEqualTo(Motor.Direction.FORWARD);
        assertThat(robot.rightDriveMotor().getDirection()).isEqualTo(Motor.Direction.FORWARD);
    }

    public void assertStopped() {
        assertThat(robot.leftDriveMotor().getDirection()).isEqualTo(Motor.Direction.STOPPED);
        assertThat(robot.rightDriveMotor().getDirection()).isEqualTo(Motor.Direction.STOPPED);
    }

    private void assertSwitchTriggered() {
        assertThat(swtch.isTriggered()).isTrue();
    }

    private void assertSwitchNotTriggered() {
        assertThat(swtch.isTriggered()).isFalse();
    }
}
