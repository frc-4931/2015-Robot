package org.frc4931.robot.command.grabber;

import edu.wpi.first.wpilibj.command.Command;
import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.AbstractCommandTest;
import org.junit.Test;

public class LowerGrabberTest extends AbstractCommandTest {

    @Override
    protected Command createCommand(Systems systems) {
        return new LowerGrabber(systems.grabber, 0.5);
    }
    
    @Test
    public void shouldLowerGrabberWhenGrabberIsRaised() {
        // Preconditions ...
        robot.raiseGrabber(0.5);
        robot.armLifterActuator().stop();
        robot.assertGrabberRaised();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(()->robot.armLifterLowerSwitch().setTriggered(),
                                    ()->robot.armLifterUpperSwitch().setNotTriggered(),
                                    robot::assertGrabberLowered));
    }
        
    @Test
    public void shouldLowerGrabberWhenGrabberIsLowered() {
        // Preconditions ...
        robot.armLifterActuator().stop();
        robot.armLifterLowerSwitch().setTriggered();
        robot.armLifterUpperSwitch().setNotTriggered();
        robot.assertGrabberLowered();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(()->robot.armLifterLowerSwitch().setTriggered(),
                                    robot::assertGrabberLowered));
    }

}
