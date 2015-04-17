/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.grabber;

import org.frc4931.robot.commandnew.CommandTester;
import org.frc4931.robot.system.mock.MockGrabber;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class CloseGrabberTest {
    private CommandTester command;
    private MockGrabber grabber;
    
    @Before
    public void beforeEach() {
        grabber = new MockGrabber();
        command = new CommandTester(new CloseGrabber(grabber));
    }
    
    @Test
    public void shouldCloseGrabberWhenOpen() {
        //Preconditions
        grabber.open();
        grabber.assertGrabberOpen();
        
        command.step(0);
        grabber.assertGrabberClosed();
    }
    
    @Test
    public void shouldDoNothingWhenAlreadyClosed() {
        // Preconditions
        grabber.close();
        grabber.assertGrabberClosed();
        
        command.step(0);
        grabber.assertGrabberClosed();
    }
}
