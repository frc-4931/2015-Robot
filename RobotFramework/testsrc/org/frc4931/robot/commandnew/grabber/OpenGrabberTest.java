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
public class OpenGrabberTest {
    private CommandTester command;
    private MockGrabber grabber;
    
    @Before
    public void beforeEach() {
        grabber = new MockGrabber();
        command = new CommandTester(new OpenGrabber(grabber));
    }
    
    @Test
    public void shouldOpenGrabberWhenClosed() {
        //Preconditions
        grabber.close();
        grabber.assertGrabberClosed();
        
        command.step(0);
        grabber.assertGrabberOpen();
    }
    
    @Test
    public void shouldDoNothingWhenAlreadyOpen() {
        // Preconditions
        grabber.open();
        grabber.assertGrabberOpen();
        
        command.step(0);
        grabber.assertGrabberOpen();
    }
}
