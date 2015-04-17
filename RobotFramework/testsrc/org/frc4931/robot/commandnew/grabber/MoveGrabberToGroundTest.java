/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.grabber;

import org.frc4931.robot.commandnew.CommandTester;
import org.frc4931.robot.system.Grabber.Position;
import org.frc4931.robot.system.mock.MockGrabber;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class MoveGrabberToGroundTest {
    private MockGrabber grabber;
    private CommandTester command;
    
    @Before
    public void beforeEach() {
        grabber = new MockGrabber();
        command = new CommandTester(new MoveGrabberToGround(grabber));
    }
    
    @Test
    public void shouldMoveFromTransferToGround() {
        //Preconditions
        grabber.set(Position.TRANSFER);
        grabber.assertGrabberRaised();
        
        command.step(0);
        grabber.assertGrabberLowered();
    }
    
    @Test
    public void shouldDoNothingIfAtGround() {
        //Preconditions
        grabber.set(Position.DOWN);
        grabber.assertGrabberLowered();
        
        command.step(0);
        grabber.assertGrabberLowered();
    }
}
