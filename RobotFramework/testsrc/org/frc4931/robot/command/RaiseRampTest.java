package org.frc4931.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.ramplifter.RaiseRamp;
import org.junit.Test;

public class RaiseRampTest extends AbstractCommandTest {

    @Override
    protected Command createCommand(Systems systems) {
        return new RaiseRamp(systems.ramp.rampLift);
    }
    
    @Test
    public void shouldRaiseRampWhenRampIsLowered() {
        // Preconditions ...
        robot.rampLifterActuator().retract();
        robot.assertRampLowered();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(robot::assertRampRaised));
    }
        
    @Test
    public void shouldRaiseRampWhenRampIsAlreadyRaised() {
        // Preconditions ...
        robot.rampLifterActuator().extend();
        robot.assertRampRaised();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(robot::assertRampRaised));
    }

}
