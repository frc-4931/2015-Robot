/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.guardrail;

import org.frc4931.robot.commandnew.CommandTester;
import org.frc4931.robot.system.mock.MockGuardrail;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class OpenGuardrailTest {
    private MockGuardrail rail;
    private CommandTester command;
    
    @Before
    public void beforeEach() {
        rail = new MockGuardrail();
        command = new CommandTester(new OpenGuardrail(rail));
    }
    
    @Test
    public void shouldOpenGuardrailsWhenClosed() {
        //Preconditions
        rail.close();
        rail.assertClosed();
        
        command.step(0);
        rail.assertOpen();
    }
    
    @Test
    public void shouldDoNothingIfAlreadyOpen() {
        //Preconditions
        rail.open();
        rail.assertOpen();
        
        command.step(0);
        rail.assertOpen();
    }
}
