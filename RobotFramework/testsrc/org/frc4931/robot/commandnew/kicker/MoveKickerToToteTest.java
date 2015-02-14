/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.kicker;

import org.frc4931.robot.commandnew.CommandTester;
import org.frc4931.robot.system.Kicker.Position;
import org.frc4931.robot.system.mock.MockKicker;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class MoveKickerToToteTest {
    private MockKicker kicker;
    private CommandTester command;
    
    @Before
    public void beforeEach() {
        kicker = new MockKicker();
        command = new CommandTester(new MoveKickerToTote(kicker));
    }
    
    @Test
    public void shouldMoveFromToteStepToTote() {
        // Preconditions
        kicker.set(Position.TOTE_STEP);
        kicker.assertToteStep();
        
        command.step(0);
        kicker.assertTote();
    }
    
    @Test
    public void shouldDoNothingIfAlreadyAtTote() {
        // Preconditions
        kicker.set(Position.TOTE);
        kicker.assertTote();
        
        command.step(0);
        kicker.assertTote();
    }
    
    @Test
    public void shouldMoveFromStepToTote() {
        // Preconditions
        kicker.set(Position.STEP);
        kicker.assertStep();
        
        command.step(0);
        kicker.assertTote();
    }
    
    @Test
    public void shouldMoveFromGroundToTote() {
        // Preconditions
        kicker.set(Position.DOWN);
        kicker.assertGround();
        
        command.step(0);
        kicker.assertTote();
    }
}
