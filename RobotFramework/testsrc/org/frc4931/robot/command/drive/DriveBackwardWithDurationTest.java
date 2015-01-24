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

import edu.wpi.first.wpilibj.command.Command;
import org.frc4931.robot.Robot;
import org.frc4931.robot.command.AbstractDriveSystemCommandTest;
import org.junit.Test;

public class DriveBackwardWithDurationTest extends AbstractDriveSystemCommandTest {

    @Override
    protected Command createCommand(Robot.Systems systems) {
        return new DriveBackwardWithDuration(systems.drive, 0.5, 0.05);
    }

    @Test
    public void testInitialization() {
        assertStopped();
    }

    @Test
    public void shouldRunWhileNotTimedOut() {
        repeat(10, () -> runCommandAnd(this::assertDrivingBackwardIfSupposedTo));
    }
}
