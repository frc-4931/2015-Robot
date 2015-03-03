/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.component.DataStream;
import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.mock.MockDataStream;
import org.frc4931.robot.mock.MockMotor;
import org.frc4931.robot.mock.MockMotorWithAngle;
import org.frc4931.robot.mock.MockRelay;
import org.frc4931.robot.mock.MockSolenoid;
import org.frc4931.robot.mock.MockSwitch;
import org.frc4931.robot.subsystem.Kicker.Position;

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
@Deprecated
public class MockRobot implements RobotManager.Components {
    
    static {
        TestableRobotState.beginTeleopMode();   // must be done before anything else ...
    }

    private final RobotManager.Systems systems;
    private final MockRelay shifter = MockRelay.withOff();
    private final MockMotor leftDrive = MockMotor.stopped();
    // Right motor is physically oriented in the opposite direction...
    private final MockMotor rightDrive = MockMotor.stopped().invert();
    private final MockMotor armLifterMotor = MockMotor.stopped();
    private final MockSwitch armLifterLowerSwitch = MockSwitch.createTriggeredSwitch();
    private final MockSwitch armLifterUpperSwitch = MockSwitch.createNotTriggeredSwitch();
    private final MockSolenoid grabber = MockSolenoid.extending();
    private final MockSwitch capturable = MockSwitch.createNotTriggeredSwitch();
    private final MockSwitch captured = MockSwitch.createNotTriggeredSwitch();
    private final MockMotorWithAngle kickerMotor = new MockMotorWithAngle();
    private final MockSolenoid rampLifter = MockSolenoid.retracting();
    private final MockMotor guardRailMotor = MockMotor.stopped();
    private final MockSwitch guardRailOpenSwitch = MockSwitch.createTriggeredSwitch();
    private final MockSwitch guardRailClosedSwitch = MockSwitch.createNotTriggeredSwitch();
    private final String frontCameraName = null;
    private final String rearCameraName = null;
    private final DataStream rioDuinoDataStream = new MockDataStream(1024, 1024, 1024);

    public MockRobot() {
        systems = RobotBuilder.build(this);
    }

    public RobotManager.Systems systems() {
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
        return armLifterUpperSwitch;
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
    public MotorWithAngle kickerMotor() {
        return kickerMotor;
    }

    @Override
    public MockSolenoid rampLifterActuator() {
        return rampLifter;
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
    
    @Override
    public String frontCameraName() {
        return frontCameraName;
    }
    
    @Override
    public String rearCameraName() {
        return rearCameraName;
    }

    @Override
    public DataStream rioDuinoDataStream() {
        return rioDuinoDataStream;
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
        systems().ramp.rampLift.lower();
        // no switch to flip
        return this;
    }
    
    public MockRobot raiseRamp() {
        systems().ramp.rampLift.raise();
        // no switch to flip
        return this;
    }

    public MockRobot lowerGrabberArm( double speed ) {
        systems().grabber.lower(speed);
        // immediately flip the switch to the signal that this is done
        armLifterLowerSwitch.setTriggered();
        armLifterUpperSwitch.setNotTriggered();
        return this;
    }

    public MockRobot raiseGrabber( double speed ) {
        systems().grabber.raise(speed);
        // immediately flip the switch to the signal that this is done
        armLifterUpperSwitch.setTriggered();
        armLifterLowerSwitch.setNotTriggered();
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
        systems().ramp.guardrail.open();
        // immediately flip the switch to the signal that this is done
        guardRailOpenSwitch.setTriggered();
        guardRailClosedSwitch.setNotTriggered();
        return this;
    }

    public MockRobot closeGuardRail() {
        systems().ramp.guardrail.close();
        // immediately flip the switch to the signal that this is done
        guardRailClosedSwitch.setTriggered();
        guardRailOpenSwitch.setNotTriggered();
        return this;
    }

    public MockRobot resetToStartingPosition() {
        return stopDriving().lowerRamp().openGuardRail().lowerGrabberArm(1).openGrabber();
    }

    public MockRobot resetToCapturedPosition() {
        return stopDriving().lowerRamp().closeGuardRail().lowerGrabberArm(1).closeGrabber();
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
    
    public void assertRampRaised() {
        // no switch to tell us that ramp is raised
    }

    public void assertRampLowered() {
        // no switch to tell us that ramp is lowered
    }

    public void assertGuardRailOpen() {
        assertThat(guardRailOpenSwitch().isTriggered()).isEqualTo(true);
        assertThat(guardRailClosedSwitch().isTriggered()).isEqualTo(false);
    }

    public void assertGuardRailClosed() {
        assertThat(guardRailClosedSwitch().isTriggered()).isEqualTo(true);
        assertThat(guardRailOpenSwitch().isTriggered()).isEqualTo(false);
    }
    
    public void assertKickerDown() {
        assertThat(kickerMotor.isAt(Position.DOWN.getAngle())).isEqualTo(true);
    }
    
    public void assertKickerStep() {
        assertThat(kickerMotor.isAt(Position.STEP.getAngle())).isEqualTo(true);
    }
    
    public void assertKickerTote() {
        assertThat(kickerMotor.isAt(Position.TOTE.getAngle())).isEqualTo(true);
    }
    
    public void assertKickerToteStep() {
        assertThat(kickerMotor.isAt(Position.TOTE_STEP.getAngle())).isEqualTo(true);
    }
    
    public void assertGrabberRaised() {
        assertThat(armLifterUpperSwitch().isTriggered()).isEqualTo(true);
        assertThat(armLifterLowerSwitch().isTriggered()).isEqualTo(false);
    }

    public void assertGrabberLowered() {
        assertThat(armLifterUpperSwitch().isTriggered()).isEqualTo(false);
        assertThat(armLifterLowerSwitch().isTriggered()).isEqualTo(true);
    }
}
