package org.frc4931.robot.command;

import org.frc4931.robot.Robot.Systems;
import org.junit.Test;

import edu.wpi.first.wpilibj.command.Command;

public class LowerRampTest extends AbstractCommandTest {

    @Override
    protected Command createCommand(Systems systems) {
        return new LowerRamp(systems.ramp);
    }
    
    @Test
    public void shouldLowerRampWhenRampIsRaised() {
        // Preconditions ...
        robot.rampLifterActuator().extend();
        robot.assertRampRaised();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(robot::assertRampLowered));
    }
        
    @Test
    public void shouldLowerRampWhenRampIsAlreadyLowered() {
        // Preconditions ...
        robot.rampLifterActuator().retract();
        robot.assertRampLowered();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(robot::assertRampLowered));
    }

}
