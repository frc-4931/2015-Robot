package org.frc4931.robot.command.guardrail;

import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.AbstractCommandTest;
import org.junit.Test;

import edu.wpi.first.wpilibj.command.Command;

@Deprecated
public class CloseGuardrailTest extends AbstractCommandTest {

    @Override
    protected Command createCommand(Systems systems) {
        return new CloseGuardrail(systems.ramp.guardrail);
    }
    
    @Test
    public void shouldCloseGuardrailWhenGuardrailIsOpen() {
        // Preconditions ...
        robot.guardRailActuator().stop();
        robot.assertGuardRailOpen();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(()->robot.guardRailClosedSwitch().setTriggered(),
                                    ()->robot.guardRailOpenSwitch().setNotTriggered(),
                                    robot::assertGuardRailClosed));
    }
        
    @Test
    public void shouldCloseGuardrailWhenguardrailIsClosed() {
        // Preconditions ...
        robot.closeGuardRail();
        robot.guardRailActuator().stop();
        robot.assertGuardRailClosed();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(()->robot.guardRailClosedSwitch().setTriggered(),
                                    robot::assertGuardRailClosed));
    }

}
