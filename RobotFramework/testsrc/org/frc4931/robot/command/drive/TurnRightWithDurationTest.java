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

/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.drive;

import org.frc4931.robot.RobotManager;
import org.frc4931.robot.command.AbstractDriveSystemCommandTest;
import org.junit.Test;

import edu.wpi.first.wpilibj.command.Command;

@Deprecated
public class TurnRightWithDurationTest extends AbstractDriveSystemCommandTest {

    @Override
    protected Command createCommand(RobotManager.Systems systems) {
        return new TurnRightWithDuration(systems.drive, -0.5, 0.05);
    }

    @Test
    public void testInitialization() {
        assertStopped();
    }

    @Test
    public void shouldRunWhileNotTimedOut() {
        repeat(10, () -> runCommandAnd(this::assertTurningRightIfSupposedTo));
    }
}
