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
public class MoveGrabberToTransferTest {
    private MockGrabber grabber;
    private CommandTester command;
    
    @Before
    public void beforeEach() {
        grabber = new MockGrabber();
        command = new CommandTester(new MoveGrabberToTransfer(grabber));
    }
    
    @Test
    public void shouldMoveFromGroundToTransfer() {
        //Preconditions
        grabber.set(Position.DOWN);
        grabber.assertGrabberLowered();
        
        command.step(0);
        grabber.assertGrabberRaised();
    }
    
    @Test
    public void shouldDoNothingIfAtTransfer() {
        //Preconditions
        grabber.set(Position.TRANSFER);
        grabber.assertGrabberRaised();
        
        command.step(0);
        grabber.assertGrabberRaised();
    }
}
