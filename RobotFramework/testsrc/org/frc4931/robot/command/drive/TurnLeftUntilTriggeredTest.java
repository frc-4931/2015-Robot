/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */

/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.drive;

import org.frc4931.robot.RobotManager;
import org.frc4931.robot.command.AbstractDriveSystemStoppableCommandTest;
import org.junit.Test;

import edu.wpi.first.wpilibj.command.Command;

@Deprecated
public class TurnLeftUntilTriggeredTest extends AbstractDriveSystemStoppableCommandTest {

    @Override
    protected Command createCommand(RobotManager.Systems systems) {
        return new TurnLeftUntilTriggered(systems.drive, 0.5f, stopDrivingSwitch);
    }

    @Test
    public void shouldRunWhileSwitchNotTriggered() {
        repeat(10, () -> runCommandAnd(this::assertTurningLeftIfSupposedTo, stopDrivingSwitch::setTriggered));
        assertStopped();
    }

    @Test
    public void shouldNotRunWhileSwitchTriggered() {
        stopDrivingSwitch.setTriggered();
        repeat(10, () -> runCommandAnd(this::assertStopped));
        assertStopped();
    }

    @Test
    public void shouldRunNormallyIfAlreadyDriving() {
        robot.systems().drive.arcade(1.0, 0.0);
        repeat(10, () -> runCommandAnd(this::assertTurningLeftIfSupposedTo, stopDrivingSwitch::setTriggered));
    }
}
