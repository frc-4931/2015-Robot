/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.ramplifter;

import org.frc4931.robot.commandnew.CommandTester;
import org.frc4931.robot.system.mock.MockLifter;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class RaiseRampTest {
    private MockLifter lifter;
    private CommandTester command;
    
    @Before
    public void beforeEach() {
        lifter = new MockLifter();
        command = new CommandTester(new RaiseRamp(lifter));
    }
    
    @Test
    public void shouldRaiseRampWhenDown() {
        // Preconditions
        lifter.lower();
        lifter.assertLowered();
        
        command.step(0);
        lifter.assertRaised();
    }
    
    @Test
    public void shouldDoNothingIfAlreadyRaised() {
        // Preconditions
        lifter.raise();
        lifter.assertRaised();
        
        command.step(0);
        lifter.assertRaised();
    }
}
