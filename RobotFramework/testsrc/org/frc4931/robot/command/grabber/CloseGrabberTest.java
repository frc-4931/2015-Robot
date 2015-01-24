/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.grabber;

import edu.wpi.first.wpilibj.command.Command;
import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.AbstractCommandTest;
import org.junit.Test;

public class CloseGrabberTest extends AbstractCommandTest {

    @Override
    protected Command createCommand(Systems systems) {
        return new CloseGrabber(systems.grabber);
    }
    
    @Test
    public void shouldCloseGrabberWhenGrabberIsOpen() {
        // Preconditions ...
        robot.grabberActuator().extend();
        robot.assertGrabberSolenoidExtended();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(robot::assertGrabberSolenoidRetracted));
    }
        
    @Test
    public void shouldCloseGrabberWhenGrabberIsAlreadyClosed() {
        // Preconditions ...
        robot.grabberActuator().retract();
        robot.assertGrabberSolenoidRetracted();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(robot::assertGrabberSolenoidRetracted));
    }
}
