package org.frc4931.robot.command.ramplifter;

import edu.wpi.first.wpilibj.command.Command;
import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.AbstractCommandTest;
import org.junit.Test;

public class LowerRampTest extends AbstractCommandTest {

    @Override
    protected Command createCommand(Systems systems) {
        return new LowerRamp(systems.ramp.rampLift);
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
