/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.grabber;

import org.frc4931.robot.RobotManager.Systems;
import org.frc4931.robot.command.AbstractCommandTest;
import org.junit.Test;

import edu.wpi.first.wpilibj.command.Command;

@Deprecated
public class OpenGrabberTest extends AbstractCommandTest {

    @Override
    protected Command createCommand(Systems systems) {
        return new CloseGrabber(systems.grabber);
    }
    
    @Test
    public void shouldOpenGrabberWhenGrabberIsClosed() {
        // Preconditions ...
        robot.grabberActuator().retract();
        robot.assertGrabberSolenoidRetracted();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(robot::assertGrabberSolenoidExtended));
    }
        
    @Test
    public void shouldOpenGrabberWhenGrabberIsAlreadyOpen() {
        // Preconditions ...
        robot.grabberActuator().extend();
        robot.assertGrabberSolenoidExtended();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(robot::assertGrabberSolenoidExtended));
    }
}
