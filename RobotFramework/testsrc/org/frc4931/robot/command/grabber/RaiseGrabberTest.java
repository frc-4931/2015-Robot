package org.frc4931.robot.command.grabber;

import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.AbstractCommandTest;
import org.junit.Test;

import edu.wpi.first.wpilibj.command.Command;

public class RaiseGrabberTest extends AbstractCommandTest {

    @Override
    protected Command createCommand(Systems systems) {
        return new RaiseGrabber(systems.grabber, 0.5);
    }
    
    @Test
    public void shouldRaiseGrabberWhenGrabberIsLowered() {
        // Preconditions ...
        robot.lowerGrabberArm(0.5);
        robot.armLifterActuator().stop();
        robot.assertGrabberLowered();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(()->robot.armLifterUpperSwitch().setTriggered(),
                                    ()->robot.armLifterLowerSwitch().setNotTriggered(),
                                    robot::assertGrabberRaised));
    }
        
    @Test
    public void shouldRaiseGrabberWhenGrabberIsRaised() {
        // Preconditions ...
        robot.armLifterActuator().stop();
        robot.armLifterUpperSwitch().setTriggered();
        robot.armLifterLowerSwitch().setNotTriggered();
        robot.assertGrabberRaised();
        // Create and execute the command multiple times ...
        repeat(10,()->runCommandAnd(()->robot.armLifterUpperSwitch().setTriggered(),
                                    robot::assertGrabberRaised));
    }

}
