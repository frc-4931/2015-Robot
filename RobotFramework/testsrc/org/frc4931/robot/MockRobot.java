/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

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
    private final MockSolenoid armLifter = MockSolenoid.extending();
    private final MockSolenoid grabber = MockSolenoid.extending();
    private final MockSwitch capturable = MockSwitch.notTriggered();
    private final MockSwitch captured = MockSwitch.notTriggered();
    private final MockSolenoid rampLifter = MockSolenoid.retracting();
    private final MockSolenoid stackLifter = MockSolenoid.retracting();
    private final MockSolenoid kicker = MockSolenoid.retracting();
    private final MockMotor guardRailMotor = MockMotor.stopped();
    private final MockSwitch guardRailOpenSwitch = MockSwitch.triggered();
    private final MockSwitch guardRailClosedSwitch = MockSwitch.notTriggered();

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
    public MockSolenoid armLifterActuator() {
        return armLifter;
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
    public MockSolenoid stackLifterActuator() {
        return stackLifter;
    }

    @Override
    public MockSolenoid kickerActuator() {
        return kicker;
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
        return this;
    }

    public MockRobot lowerStack() {
        systems().ramp.lowerStack();
        return this;
    }

    public MockRobot lowerGrabber() {
        systems().grabber.lower();
        return this;
    }

    public MockRobot raiseGrabber() {
        systems().grabber.raise();
        return this;
    }

    public MockRobot openGrabber() {
        systems().grabber.release();
        return this;
    }

    public MockRobot closeGrabber() {
        systems().grabber.grab();
        return this;
    }

    public MockRobot openGuardRail() {
        systems().ramp.openGuardRail();
        return this;
    }

    public MockRobot closeGuardRail() {
        systems().ramp.closeGuardRail();
        return this;
    }

    public MockRobot resetToStartingPosition() {
        return stopDriving().lowerRamp().lowerStack().openGuardRail().lowerGrabber().openGrabber();
    }

    public MockRobot resetToCapturedPosition() {
        return stopDriving().lowerRamp().lowerStack().closeGuardRail().lowerGrabber().closeGrabber();
    }

    public void assertGrabberSolenoidRetracted() {
        assertThat(grabberActuator().isRetracted()).isEqualTo(true);
        assertThat(grabberActuator().isExtended()).isEqualTo(false);
    }

    public void assertGrabberSolenoidExtended() {
        assertThat(grabberActuator().isExtended()).isEqualTo(true);
        assertThat(grabberActuator().isRetracted()).isEqualTo(false);
    }

    public void assertArmLifterSolenoidRetracted() {
        assertThat(armLifterActuator().isRetracted()).isEqualTo(true);
        assertThat(armLifterActuator().isExtended()).isEqualTo(false);
    }

    public void assertArmLifterExtended() {
        assertThat(armLifterActuator().isExtended()).isEqualTo(true);
        assertThat(armLifterActuator().isRetracted()).isEqualTo(false);
    }

}
