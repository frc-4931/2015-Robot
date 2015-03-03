package org.frc4931.robot.command.guardrail;

import org.frc4931.robot.RobotManager.Systems;
import org.frc4931.robot.command.AbstractCommandTest;
import org.junit.Test;

import edu.wpi.first.wpilibj.command.Command;

@Deprecated
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
