/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.RobotManager.Systems;
import org.frc4931.robot.component.DataStream;
import org.frc4931.robot.component.DriveTrain;
import org.frc4931.robot.component.LimitedMotor;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.component.RIODuino;
import org.frc4931.robot.component.Relay;
import org.frc4931.robot.component.SerialDataStream;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Switch;
import org.frc4931.robot.hardware.Hardware;
import org.frc4931.robot.hardware.Hardware.Motors;
import org.frc4931.robot.hardware.Hardware.Sensors.Switches;
import org.frc4931.robot.hardware.Hardware.Solenoids;
import org.frc4931.robot.hardware.HardwareTalonSRX;
import org.frc4931.robot.subsystem.DriveSystem;
import org.frc4931.robot.subsystem.Guardrail;
import org.frc4931.robot.subsystem.Kicker;
import org.frc4931.robot.subsystem.LoaderArm;
import org.frc4931.robot.subsystem.Ramp;
import org.frc4931.robot.subsystem.RampLifter;
import org.frc4931.robot.subsystem.StackIndicatorLight;
import org.frc4931.robot.subsystem.VisionSystem;

import edu.wpi.first.wpilibj.SerialPort;

/**
 * Instantiates all of the robot components and returns them in an aggregate class.
 */
@Deprecated
public class RobotBuilder {
    
    /**
     * Build the {@link Systems robot systems} given the supplied set of robot components.
     * @param components the components of the robot; may not be null
     * @return a new Systems instance; never null
     */
    public static Systems build( RobotManager.Components components ) {
        DriveSystem driveSystem = buildDriveSystem(components);
        LoaderArm arm = buildLoaderArm(components);
        Ramp ramp = buildRamp(components);
        VisionSystem vision = buildVision(components);
        StackIndicatorLight stackIndicator = buildStackIndicator(components);
        return new Systems(driveSystem, arm, ramp, vision, stackIndicator);
    }

    private static StackIndicatorLight buildStackIndicator(RobotManager.Components components) {
        return new StackIndicatorLight(new RIODuino(components.rioDuinoDataStream()));
    }

    /**
     * Build an instance of the {@link DriveSystem} subsystem given the supplied set of robot components.
     * @param components the components of the robot; may not be null
     * @return a new drive system instance; never null
     */
    public static DriveSystem buildDriveSystem(RobotManager.Components components) {
        return new DriveSystem(DriveTrain.create(components.leftDriveMotor(),
                                                 components.rightDriveMotor()),
                               components.shifter());
    }

    /**
     * Build an instance of the {@link LoaderArm} subsystem given the supplied set of robot components.
     * @param components the components of the robot; may not be null
     * @return a new loader arm instance; never null
     */
    public static LoaderArm buildLoaderArm(RobotManager.Components components) {
        LimitedMotor armMotor = new LimitedMotor(components.armLifterActuator(),
                                                 components.armLifterUpperSwitch(),
                                                 components.armLifterLowerSwitch());
        return new LoaderArm(armMotor,
                             components.grabberActuator(),
                             components.capturableSwitch(),
                             components.capturedSwitch());
    }

    /**
     * Build an instance of the {@link Ramp} subsystem given the supplied set of robot components.
     * @param components the components of the robot; may not be null
     * @return a new ramp instance; never null
     */
    public static Ramp buildRamp(RobotManager.Components components) {
        RampLifter lifter = new RampLifter(components.rampLifterActuator());
        Guardrail rail = new Guardrail(new LimitedMotor(components.guardRailActuator(),
                                        components.guardRailOpenSwitch(),
                                        components.guardRailClosedSwitch()));
        Kicker kicker = new Kicker(components.kickerMotor());
        return new Ramp(rail, lifter, kicker);
    }
    
    /**
     * Build an instance of the {@link LoaderArm} subsystem given the supplied set of robot components.
     * @param components the components of the robot; may not be null
     * @return a new loader arm instance; never null
     */
    public static VisionSystem buildVision(RobotManager.Components components) {
        return new VisionSystem(components.frontCameraName(), components.rearCameraName());
    }
    /**
     * Get the operator interface that will be used for the robot.
     * @return the default operator interface; never null
     */
//    public static OperatorInterface operatorInterface() {
//        Gamepad gamepad = new LogitechGamepadF310(Properties.JOYSTICK);
//        return new OperatorInterface(gamepad);
//    }

    /**
     * Get the robot components for the actual hardware.
     * @return the hardware components of the robot; never null
     */
    public static RobotManager.Components components() {
        // Create the drive system ...
        Motor leftDriveMotor = Motor.compose(Motors.talon(Properties.LEFT_FRONT_DRIVE),
                                             Motors.talon(Properties.LEFT_REAR_DRIVE));
        // Right motor is physically oriented in the opposite direction...
        Motor rightDriveMotor = Motor.invert(
                Motor.compose(Motors.talon(Properties.RIGHT_FRONT_DRIVE),
                        Motors.talon(Properties.RIGHT_REAR_DRIVE)));
        Relay shifter = Hardware.relay(Properties.DRIVE_SHIFTER);

        // Create the loader arm subsystem ...
        Motor grabberLifterMotor = Motors.talon(Properties.GRABBER_LIFTER_MOTOR);
        Switch grabberLifterLowerSwitch = Switches.normallyClosed(Properties.GRABBER_LIFTER_LOWER_SWITCH);
        Switch grabberLifterUpperSwitch = Switches.normallyClosed(Properties.GRABBER_LIFTER_UPPER_SWITCH);
        Solenoid grabberClaw = Solenoids.doubleSolenoid(Properties.GRABBER_CLAW_EXTEND,
                                                        Properties.GRABBER_CLAW_RETRACT,
                                                        Solenoid.Direction.EXTENDING);
        HardwareTalonSRX talon = Motors.talonSRX(Properties.KICKER_MOTOR, Properties.KICKER_PPD);
        MotorWithAngle kickerMotor = null; //new PIDMotorWithAngle(talon.getMotor(), talon.getCurrentSensor(), talon.getAngleSensor(),
                                           //                talon.getHomeSwitch(), Properties.KICKER_TOLERANCE, Properties.KICKER_MAX_CURRENT,
                                           //                 1, 0, 0, 0.5, 0.5, 180);
        Switch canGrab = Switches.normallyClosed(Properties.GRABBER_SWITCH_CANGRAB);
        Switch didGrab = Switches.normallyClosed(Properties.GRABBER_SWITCH_DIDGRAB);

        // Create the ramp subsystem ...
        Solenoid rampLifter = Solenoids.doubleSolenoid(Properties.RAMP_LIFTER_EXTEND,
                                                       Properties.RAMP_LIFTER_RETRACT,
                                                       Solenoid.Direction.RETRACTING);
        
        Motor guardRailMotor = Motors.talon(Properties.GUARDRAIL_MOTOR);
        Switch guardRailOpenSwitch = Switches.normallyClosed(Properties.GUARDRAIL_OPEN_SWITCH);
        Switch guardRailClosedSwitch = Switches.normallyClosed(Properties.GUARDRAIL_CLOSE_SWITCH);
        
        String frontCameraName = Properties.FRONT_CAMERA_NAME;
        String rearCameraName = Properties.REAR_CAMERA_NAME;

        DataStream rioDuinoDataStream = new SerialDataStream(new SerialPort(Properties.RIODUINO_SERIAL_BAUD,
                Properties.RIODUINO_SERIAL_PORT));

        return new RobotManager.Components() {

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
            public Motor armLifterActuator() {
                return grabberLifterMotor;
            }
            
            @Override
            public Switch armLifterLowerSwitch() {
                return grabberLifterLowerSwitch;
            }
            
            @Override
            public Switch armLifterUpperSwitch() {
                return grabberLifterUpperSwitch;
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
            public MotorWithAngle kickerMotor() {
                return kickerMotor;
            }

            @Override
            public Solenoid rampLifterActuator() {
                return rampLifter;
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
        };
    }
    
    private static final class Properties {
        // TODO: Make sure these are all correct before running on the hardware!!

        /*-------CONSTANT------*/
        private static final double KICKER_TOLERANCE = 5;
        private static final double KICKER_PPD = 2.7;
        private static final double KICKER_MAX_CURRENT = 5;
        
        /*-------JOYSTICK------*/
        private static final int JOYSTICK = 0;

        /*-------DRIVE SYSTEM------*/
        private static final int LEFT_FRONT_DRIVE = 0;
        private static final int LEFT_REAR_DRIVE = 1;
        private static final int RIGHT_FRONT_DRIVE = 2;
        private static final int RIGHT_REAR_DRIVE = 3;

        private static final int DRIVE_SHIFTER = 0;

        /*-------GRABBER------*/
        private static final int GRABBER_LIFTER_MOTOR = 4;
        private static final int GRABBER_LIFTER_UPPER_SWITCH = 0;
        private static final int GRABBER_LIFTER_LOWER_SWITCH = 1;

        private static final int GRABBER_CLAW_EXTEND = 2;
        private static final int GRABBER_CLAW_RETRACT = 3;

        private static final int GRABBER_SWITCH_CANGRAB = 4;
        private static final int GRABBER_SWITCH_DIDGRAB = 5;

        /*-------RAMP------*/
        private static final int RAMP_LIFTER_EXTEND = 4;
        private static final int RAMP_LIFTER_RETRACT = 5;

        private static final int GUARDRAIL_MOTOR = 7;
        private static final int GUARDRAIL_OPEN_SWITCH = 8;
        private static final int GUARDRAIL_CLOSE_SWITCH = 9;
        
        private static final int KICKER_MOTOR = 0;
        private static final int KICKER_HOME = 6;
        
        /*-------VISION-------*/
        private static final String FRONT_CAMERA_NAME = null;
        private static final String REAR_CAMERA_NAME = null;

        /*-------RIODUINO-------*/
        private static final int RIODUINO_SERIAL_BAUD = 9600;
        private static final SerialPort.Port RIODUINO_SERIAL_PORT = SerialPort.Port.kMXP;
    }
}
