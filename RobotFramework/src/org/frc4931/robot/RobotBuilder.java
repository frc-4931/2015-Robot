/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.component.DriveTrain;
import org.frc4931.robot.component.LimitedMotor;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Relay;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Switch;
import org.frc4931.robot.driver.LogitechAttack3D;
import org.frc4931.robot.driver.OperatorInterface;
import org.frc4931.robot.hardware.Hardware;
import org.frc4931.robot.hardware.Hardware.Motors;
import org.frc4931.robot.hardware.Hardware.Sensors.Switches;
import org.frc4931.robot.hardware.Hardware.Solenoids;
import org.frc4931.robot.subsystem.DriveSystem;
import org.frc4931.robot.subsystem.LoaderArm;
import org.frc4931.robot.subsystem.Ramp;

/**
 * Instantiates all of the robot components and returns them in an aggregate class.
 */
public class RobotBuilder {
    
    /**
     * Build the {@link Systems robot systems} given the supplied set of robot components.
     * @param components the components on the robot; may not be null
     * @return a new Systems instance; never null
     */
    public static Systems build( Robot.Components components ) {
        DriveSystem driveSystem = buildDriveSystem(components);
        LoaderArm arm = buildLoaderArm(components);
        Ramp ramp = buildRamp(components);
        OperatorInterface operatorInterface = operatorInterface();
        return new Systems(operatorInterface,driveSystem, arm, ramp);
    }

    /**
     * Build an instance of the {@link DriveSystem} subsystem given the supplied set of robot components.
     * @param components the components on the robot; may not be null
     * @return a new drive system instance; never null
     */
    public static DriveSystem buildDriveSystem(Robot.Components components) {
        return new DriveSystem(DriveTrain.create(components.leftDriveMotor(),
                                                 components.rightDriveMotor()),
                               components.shifter());
    }

    /**
     * Build an instance of the {@link LoaderArm} subsystem given the supplied set of robot components.
     * @param components the components on the robot; may not be null
     * @return a new loader arm instance; never null
     */
    public static LoaderArm buildLoaderArm(Robot.Components components) {
        return new LoaderArm(components.armLifterActuator(),
                             components.grabberActuator(),
                             components.capturableSwitch(),
                             components.capturedSwitch());
    }

    /**
     * Build an instance of the {@link Ramp} subsystem given the supplied set of robot components.
     * @param components the components on the robot; may not be null
     * @return a new ramp instance; never null
     */
    public static Ramp buildRamp(Robot.Components components) {
        LimitedMotor guardRailMotor = new LimitedMotor(components.guardRailActuator(),
                                                  components.guardRailOpenSwitch(),
                                                  components.guardRailClosedSwitch());
        return new Ramp(components.rampLifterActuator(),
                        components.stackLifterActuator(),
                        components.kickerActuator(),
                        guardRailMotor);
    }
    
    /**
     * Get the operator interface that will be used for the robot.
     * @return the default operator interface; never null
     */
    public static OperatorInterface operatorInterface() {
        return new OperatorInterface( new LogitechAttack3D(Properties.JOYSTICK) );
    }

    /**
     * Get the robot components for the actual hardware.
     * @return the hardware components of the robot; never null
     */
    public static Robot.Components components() {
        // Create the drive system ...
        Motor leftDriveMotor = Motor.compose(Motors.talon(Properties.LEFT_FRONT_DRIVE),
                                             Motors.talon(Properties.LEFT_REAR_DRIVE));
        Motor rightDriveMotor = Motor.compose(Motors.talon(Properties.RIGHT_FRONT_DRIVE),
                                              Motors.talon(Properties.RIGHT_REAR_DRIVE));
        Relay shifter = Hardware.relay(Properties.DRIVE_SHIFTER);

        // Create the loader arm subsystem ...
        Solenoid grabberLift = Solenoids.doubleSolenoid(Properties.GRABBER_LIFTER_EXTEND,
                                                        Properties.GRABBER_LIFTER_RETRACT,
                                                        Solenoid.Direction.EXTENDING);
        Solenoid grabberClaw = Solenoids.doubleSolenoid(Properties.GRABBER_CLAW_EXTEND,
                                                        Properties.GRABBER_CLAW_RETRACT,
                                                        Solenoid.Direction.EXTENDING);
        Switch canGrab = Switches.normallyClosed(Properties.GRABBER_SWITCH_CANGRAB);
        Switch didGrab = Switches.normallyClosed(Properties.GRABBER_SWITCH_DIDGRAB);

        // Create the ramp subsystem ...
        Solenoid rampLifter = Solenoids.doubleSolenoid(Properties.RAMP_LIFTER_EXTEND,
                                                       Properties.RAMP_LIFTER_RETRACT,
                                                       Solenoid.Direction.RETRACTING);
        Solenoid stackLifter = Solenoids.doubleSolenoid(
                                                        Properties.RAMP_STACK_LIFTER_EXTEND,
                                                        Properties.RAMP_STACK_LIFTER_RETRACT,
                                                        Solenoid.Direction.RETRACTING);
        Solenoid kicker = Solenoids.doubleSolenoid(
                                                   Properties.RAMP_STATCK_PUSHER_EXTEND,
                                                   Properties.RAMP_STACK_PUSHER_RETRACT,
                                                   Solenoid.Direction.RETRACTING);
        Motor guardRailMotor = Motors.talon(Properties.GUARDRAIL_MOTOR);
        Switch guardRailOpenSwitch = Switches.normallyClosed(Properties.GUARDRAIL_OPEN_SWITCH);
        Switch guardRailClosedSwitch = Switches.normallyClosed(Properties.GUARDRAIL_CLOSE_SWITCH);

        return new Robot.Components() {

            @Override
            public Relay shifter() {
                return shifter;
            }

            @Override
            public Motor leftDriveMotor() {
                return leftDriveMotor;
            }

            @Override
            public Motor rightDriveMotor() {
                return rightDriveMotor;
            }

            @Override
            public Solenoid armLifterActuator() {
                return grabberLift;
            }

            @Override
            public Solenoid grabberActuator() {
                return grabberClaw;
            }

            @Override
            public Switch capturableSwitch() {
                return canGrab;
            }

            @Override
            public Switch capturedSwitch() {
                return didGrab;
            }

            @Override
            public Solenoid rampLifterActuator() {
                return rampLifter;
            }

            @Override
            public Solenoid stackLifterActuator() {
                return stackLifter;
            }

            @Override
            public Solenoid kickerActuator() {
                return kicker;
            }

            @Override
            public Motor guardRailActuator() {
                return guardRailMotor;
            }

            @Override
            public Switch guardRailOpenSwitch() {
                return guardRailOpenSwitch;
            }

            @Override
            public Switch guardRailClosedSwitch() {
                return guardRailClosedSwitch;
            }
            
        };
    }
    
    private static final class Properties {
        // TODO: Make sure these are all correct before running on the hardware!!
        
        /*-------JOYSTICK------*/
        private static final int JOYSTICK = 0;

        /*-------DRIVE SYSTEM------*/
        private static final int LEFT_FRONT_DRIVE = 0;
        private static final int LEFT_REAR_DRIVE = 1;
        private static final int RIGHT_FRONT_DRIVE = 2;
        private static final int RIGHT_REAR_DRIVE = 3;

        private static final int DRIVE_SHIFTER = 0;

        /*-------GRABBER------*/
        private static final int GRABBER_LIFTER_EXTEND = 0;
        private static final int GRABBER_LIFTER_RETRACT = 1;

        private static final int GRABBER_CLAW_EXTEND = 2;
        private static final int GRABBER_CLAW_RETRACT = 3;

        private static final int GRABBER_SWITCH_CANGRAB = 4;
        private static final int GRABBER_SWITCH_DIDGRAB = 5;

        /*-------RAMP------*/
        private static final int RAMP_LIFTER_EXTEND = 4;
        private static final int RAMP_LIFTER_RETRACT = 5;

        private static final int RAMP_STACK_LIFTER_EXTEND = 6;
        private static final int RAMP_STACK_LIFTER_RETRACT = 7;

        private static final int RAMP_STATCK_PUSHER_EXTEND = 8;
        private static final int RAMP_STACK_PUSHER_RETRACT = 9;

        private static final int GUARDRAIL_MOTOR = 4;
        private static final int GUARDRAIL_OPEN_SWITCH = 0;
        private static final int GUARDRAIL_CLOSE_SWITCH = 1;

    }
}
