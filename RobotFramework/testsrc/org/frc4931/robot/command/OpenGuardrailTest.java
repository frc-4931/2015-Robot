package org.frc4931.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.guardrail.OpenGuardrail;
import org.junit.Test;

public class OpenGuardrailTest extends AbstractCommandTest {

    @Override
    protected Command createCommand(Systems systems) {
        return new OpenGuardrail(systems.ramp.guardrail);
    }
    
    @Test
    public void shouldOpenGuardrailWhenGuardrailIsClosed() {
        // Preconditions ...
        robot.closeGuardRail();
        robot.guardRailActuator().stop();
        robot.assertGuardRailClosed();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(()->robot.guardRailOpenSwitch().setTriggered(),
                                    ()->robot.guardRailClosedSwitch().setNotTriggered(),
                                    robot::assertGuardRailOpen));
    }
        
    @Test
    public void shouldOpenGuardrailWhenguardrailIsOpen() {
        // Preconditions ...
        robot.openGuardRail();
        robot.guardRailActuator().stop();
        robot.assertGuardRailOpen();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(()->robot.guardRailOpenSwitch().setTriggered(),
                                    robot::assertGuardRailOpen));
    }

}
