/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.component.LeadScrew;
import org.frc4931.robot.component.LeadScrew.Position;
import org.frc4931.robot.component.mock.MockMotor;
import org.frc4931.robot.component.mock.MockRelay;
import org.frc4931.robot.component.mock.MockSolenoid;
import org.frc4931.robot.component.mock.MockSwitch;

import edu.wpi.first.wpilibj.TestableRobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;

import static org.fest.assertions.Assertions.assertThat;

/**
 * A robot that can be used in the tests. This robot is currently set up to mirror the structure of the real robot, except
 * mock relays, motors, switches, and solenoids are used. This mock robot can be used to check the state of these mock components
 * at various times during the tests. It also contains various utility methods that can be used in tests to move the robot into
 * various states and to assert certain states or positions.
 * <p>
 * This robot also sets up the necessary WPILib infrastructure, including the {@link Scheduler} and {@link Timer match timer}, via
 * the {@link TestableRobotState}.
 */
public class MockRobot implements Robot.Components {
    
    static {
        TestableRobotState.beginTeleopMode();   // must be done before anything else ...
    }

    private final Robot.Systems systems;
    private final MockRelay shifter = MockRelay.withOff();
    private final MockMotor leftDrive = MockMotor.stopped();
    private final MockMotor rightDrive = MockMotor.stopped();
    private final MockMotor armLifterMotor = MockMotor.stopped();
    private final MockSwitch armLifterLowerSwitch = MockSwitch.createTriggeredSwitch();
    private final MockSwitch armLifterUpperSwitch = MockSwitch.createNotTriggeredSwitch();
    private final MockSolenoid grabber = MockSolenoid.extending();
    private final MockSwitch capturable = MockSwitch.createNotTriggeredSwitch();
    private final MockSwitch captured = MockSwitch.createNotTriggeredSwitch();
    private final MockSolenoid rampLifter = MockSolenoid.retracting();
    private final MockMotor leadScrewMotor = MockMotor.stopped();
    private final MockSwitch leadScrewLowerSwitch = MockSwitch.createTriggeredSwitch();
    private final MockSwitch leadScrewStepSwitch = MockSwitch.createNotTriggeredSwitch();
    private final MockSwitch leadScrewToteSwitch = MockSwitch.createNotTriggeredSwitch();
    private final MockSwitch leadScrewToteOnStepSwitch = MockSwitch.createNotTriggeredSwitch();
    private final MockMotor kickerMotor = MockMotor.stopped();
    private final MockSwitch kickerLowerSwitch = MockSwitch.createTriggeredSwitch();
    private final MockSwitch kickerUpperSwitch = MockSwitch.createNotTriggeredSwitch();
    private final MockMotor guardRailMotor = MockMotor.stopped();
    private final MockSwitch guardRailOpenSwitch = MockSwitch.createTriggeredSwitch();
    private final MockSwitch guardRailClosedSwitch = MockSwitch.createNotTriggeredSwitch();

    public MockRobot() {
        systems = RobotBuilder.build(this);
    }

    public Robot.Systems systems() {
        return systems;
    }

    @Override
    public MockRelay shifter() {
        return shifter;
    }

    @Override
    public MockMotor leftDriveMotor() {
        return leftDrive;
    }

    @Override
    public MockMotor rightDriveMotor() {
        return rightDrive;
    }

    @Override
    public MockMotor armLifterActuator() {
        return armLifterMotor;
    }
    
    @Override
    public MockSwitch armLifterLowerSwitch() {
        return armLifterLowerSwitch;
    }
    
    @Override
    public MockSwitch armLifterUpperSwitch() {
        return armLifterLowerSwitch;
    }

    @Override
    public MockSolenoid grabberActuator() {
        return grabber;
    }

    @Override
    public MockSwitch capturableSwitch() {
        return capturable;
    }

    @Override
    public MockSwitch capturedSwitch() {
        return captured;
    }

    @Override
    public MockSolenoid rampLifterActuator() {
        return rampLifter;
    }
    
    @Override
    public MockMotor leadScrewActuator() {
        return leadScrewMotor;
    }
    
    @Override
    public MockSwitch leadScrewLowerSwitch() {
        return leadScrewLowerSwitch;
    }
    
    @Override
    public MockSwitch leadScrewStepSwitch() {
        return leadScrewStepSwitch;
    }
    
    @Override
    public MockSwitch leadScrewToteSwitch() {
        return leadScrewToteSwitch;
    }
    
    @Override
    public MockSwitch leadScrewToteOnStepSwitch() {
        return leadScrewToteOnStepSwitch;
    }
    
    @Override
    public MockMotor kickerActuator() {
        return kickerMotor;
    }
    
    @Override
    public MockSwitch kickerLowerSwitch() {
        return kickerLowerSwitch;
    }
    
    @Override
    public MockSwitch kickerUpperSwitch() {
        return kickerUpperSwitch;
    }

    @Override
    public MockMotor guardRailActuator() {
        return guardRailMotor;
    }

    @Override
    public MockSwitch guardRailOpenSwitch() {
        return guardRailOpenSwitch;
    }

    @Override
    public MockSwitch guardRailClosedSwitch() {
        return guardRailClosedSwitch;
    }

    public MockRobot enableRobot() {
        TestableRobotState.enableRobot();
        return this;
    }

    public MockRobot disableRobot() {
        TestableRobotState.disableRobot();
        return this;
    }

    public MockRobot beginTeleopMode() {
        TestableRobotState.beginTeleopMode();
        return this;
    }

    public MockRobot beginAutonomousMode() {
        TestableRobotState.beginAutonomousMode();
        return this;
    }

    public MockRobot beginTestMode() {
        TestableRobotState.beginTestMode();
        return this;
    }

    public MockRobot stopDriving() {
        systems().drive.stop();
        return this;
    }

    public MockRobot lowerRamp() {
        systems().ramp.lowerRamp();
        // no switch to flip
        return this;
    }
    
    public MockRobot raiseRamp() {
        systems().ramp.raiseRamp();
        // no switch to flip
        return this;
    }

    public MockRobot moveStackTo( LeadScrew.Position desiredPosition, double speed ) {
        systems().ramp.moveStackTowards(desiredPosition, speed );
        // immediately flip the switch to the signal that this is done
        switch( desiredPosition ) {
            case LOW:
                leadScrewLowerSwitch.setTriggered();
                break;
            case STEP:
                leadScrewStepSwitch.setTriggered();
                break;
            case TOTE:
                leadScrewToteSwitch.setTriggered();
                break;
            case TOTE_ON_STEP:
                leadScrewToteOnStepSwitch.setTriggered();
                break;
            case UNKNOWN:
                break;
        }
        return this;
    }

    public MockRobot lowerGrabberArm( double speed ) {
        systems().grabber.lower(speed);
        // immediately flip the switch to the signal that this is done
        armLifterLowerSwitch.setTriggered();
        return this;
    }

    public MockRobot raiseGrabber( double speed ) {
        systems().grabber.raise(speed);
        // immediately flip the switch to the signal that this is done
        armLifterUpperSwitch.setTriggered();
        return this;
    }

    public MockRobot openGrabber() {
        systems().grabber.release();
        // no switch to flip
        return this;
    }

    public MockRobot closeGrabber() {
        systems().grabber.grab();
        // no switch to flip
        return this;
    }

    public MockRobot openGuardRail() {
        systems().ramp.openGuardRail();
        // immediately flip the switch to the signal that this is done
        guardRailOpenSwitch.setTriggered();
        return this;
    }

    public MockRobot closeGuardRail() {
        systems().ramp.closeGuardRail();
        // immediately flip the switch to the signal that this is done
        guardRailClosedSwitch.setTriggered();
        return this;
    }

    public MockRobot resetToStartingPosition() {
        return stopDriving().lowerRamp().moveStackTo(Position.LOW,1).openGuardRail().lowerGrabberArm(1).openGrabber();
    }

    public MockRobot resetToCapturedPosition() {
        return stopDriving().lowerRamp().moveStackTo(Position.LOW,1).closeGuardRail().lowerGrabberArm(1).closeGrabber();
    }

    public void assertGrabberSolenoidRetracted() {
        assertThat(grabberActuator().isRetracted()).isEqualTo(true);
        assertThat(grabberActuator().isExtended()).isEqualTo(false);
    }

    public void assertGrabberSolenoidExtended() {
        assertThat(grabberActuator().isExtended()).isEqualTo(true);
        assertThat(grabberActuator().isRetracted()).isEqualTo(false);
    }

    public void assertArmLifterRaised() {
        assertThat(armLifterUpperSwitch().isTriggered()).isEqualTo(true);
        assertThat(armLifterLowerSwitch().isTriggered()).isEqualTo(false);
    }

    public void assertArmLifterLowered() {
        assertThat(armLifterUpperSwitch().isTriggered()).isEqualTo(false);
        assertThat(armLifterLowerSwitch().isTriggered()).isEqualTo(true);
    }

}
