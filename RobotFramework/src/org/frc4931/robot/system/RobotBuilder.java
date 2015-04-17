/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.component.Accelerometer;
import org.frc4931.robot.component.AngleSensor;
import org.frc4931.robot.component.Counter;
import org.frc4931.robot.component.CurrentSensor;
import org.frc4931.robot.component.DistanceSensor;
import org.frc4931.robot.component.DriveTrain;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Relay;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Solenoid.Direction;
import org.frc4931.robot.component.Switch;
import org.frc4931.robot.composites.CompositeCompressor;
import org.frc4931.robot.composites.CompositeGrabber;
import org.frc4931.robot.composites.CompositeGuardrail;
import org.frc4931.robot.composites.CompositeKicker;
import org.frc4931.robot.composites.CompositeLifter;
import org.frc4931.robot.controller.GrabberControlProfile;
import org.frc4931.robot.controller.KickerControlProfile;
import org.frc4931.robot.driver.LogitechAttack3;
import org.frc4931.robot.driver.LogitechAttack3D;
import org.frc4931.robot.driver.OperatorInterface;
import org.frc4931.robot.hardware.Hardware;
import org.frc4931.robot.hardware.Hardware.Motors;
import org.frc4931.robot.hardware.Hardware.Sensors;
import org.frc4931.robot.hardware.Hardware.Sensors.Accelerometers;
import org.frc4931.robot.hardware.Hardware.Sensors.Switches;
import org.frc4931.robot.hardware.Hardware.Solenoids;
import org.frc4931.robot.hardware.HardwareTalonSRX;
import org.frc4931.robot.hardware.PIDMotorWithAngle;

import edu.wpi.first.wpilibj.SerialPort;

/**
 * 
 */
public class RobotBuilder {
    public static Robot buildRobot() {
        Componets componets = new Componets();
        
     // Build the power distro panel
        PowerPanel powerPanel = componets.powerPanel;
        
        // Build the drive
        DriveInterpreter drive = new DriveInterpreter(DriveTrain.create(
                                  Motor.compose(componets.leftFront,
                                                componets.leftRear),
                                  Motor.invert(Motor.compose(
                                      componets.rightFront,
                                      componets.rightRear))));
        
        // Build the structure
        
        // Build the grabber
        Grabber grabber = new CompositeGrabber(new PIDMotorWithAngle(
                             componets.grabberLifter, ()->powerPanel.getCurrent(Properties.GRABBER_LIFTER_CURRENT),
                             componets.grabberEncoder, componets.grabberHome,
                             Properties.GRABBER_MAX_CURRENT, Properties.GRABBER_MAX_ANGLE_DEGREES,
                             new GrabberControlProfile(Properties.GRABBER_MAX_RAISE_SPEED, Properties.GRABBER_MAX_LOWER_SPEED,
                                                       0.1, Properties.GRABBER_TOLERANCE)),
                             componets.grabberLeftGrabber, componets.grabberRightGrabber);
                
        Counter toteCounter = new Counter(5);
        
        // Build the Kicker
        Kicker kicker = new CompositeKicker(new PIDMotorWithAngle(
                           componets.kickerMotor, componets.kickerCurrent,
                           componets.kickerEncoder, componets.kickerHome,
                           Properties.KICKER_MAX_CURRENT, Properties.KICKER_MAX_ANGLE_DEGREES,
                           new KickerControlProfile(Properties.KICKER_TOLERANCE, Properties.KICKER_MOVE_SPEEDS,
                                                Properties.KICKER_HOLD_SPEEDS, toteCounter::get)));
        
        Switch canCapture      = componets.kickerSwitch;
        KickerSwitchSystem kss = new KickerSwitchSystem(kicker, canCapture);
        
        // Build the ramp
        Lifter    lifter = new CompositeLifter(componets.rampLifter);
        Guardrail rail   = new CompositeGuardrail(componets.guardrailGrabber);
        Ramp      ramp   = new Ramp(lifter, rail);
        
        Superstructure structure = new Superstructure(grabber, kss, ramp);
        
        // Build the accelerometer
        Accelerometer accel = componets.builtInAccel;
        
        Compressor compressor = new CompositeCompressor(componets.compressorRelay, componets.pressureSwitch);
        
        OperatorInterface operator = new OperatorInterface(new LogitechAttack3D(Properties.JOYSTICK), new LogitechAttack3(Properties.CODRIVER));
        
        DistanceSensor frontDistance = componets.frontDistance;
        
        return new Robot(drive, accel, structure, powerPanel, operator, compressor, toteCounter, frontDistance, componets);
    }
    
    public static final class Componets {
        public Componets() { }

        // Drive
        public final Motor leftFront  = Motors.talon(Properties.LEFT_FRONT_DRIVE);
        public final Motor leftRear   = Motors.talon(Properties.LEFT_REAR_DRIVE);
        public final Motor rightFront = Motors.talon(Properties.RIGHT_FRONT_DRIVE);
        public final Motor rightRear  = Motors.talon(Properties.RIGHT_REAR_DRIVE);

        // Grabber
        public final Motor       grabberLifter  = Motor.invert(Motors.victor(Properties.GRABBER_LIFTER_MOTOR));
        public final Switch      grabberHome    = Switches.normallyClosed(Properties.GRABBER_LIFTER_HOME);
        public final AngleSensor grabberEncoder = AngleSensor.invert(Sensors.encoder(Properties.GRABBER_ENCODER_A, Properties.GRABBER_ENCODER_B, Properties.GRABBER_ENCODER_PPD));
    
        public final Solenoid grabberLeftGrabber  = Solenoids.doubleSolenoid(Properties.GRABBER_LEFT_SOLENOID_EXTEND, Properties.GRABBER_LEFT_SOLENOID_RETRACT, Direction.RETRACTING);
        public final Solenoid grabberRightGrabber = Solenoids.doubleSolenoid(Properties.GRABBER_RIGHT_SOLENOID_EXTEND, Properties.GRABBER_RIGHT_SOLENOID_RETRACT, Direction.RETRACTING);

        // Ramp
        public final Solenoid rampLifter        = Solenoids.doubleSolenoid(Properties.RAMP_SOLENOID_EXTEND, Properties.RAMP_SOLENOID_RETRACT, Direction.EXTENDING);
        public final Solenoid guardrailGrabber  = Solenoids.doubleSolenoid(Properties.GUARDRAIL_SOLENOID_EXTEND, Properties.GUARDRAIL_SOLENOID_RETRACT, Direction.RETRACTING);
        
        private final HardwareTalonSRX kickerTalon  = Motors.talonSRX(Properties.KICKER_MOTOR_CAN_ID,
                                                                       Properties.KICKER_ENCODER_PPD);
        public final Motor         kickerMotor   = kickerTalon.getMotor();
        public final Switch        kickerHome    = kickerTalon.getHomeSwitch();
        public final CurrentSensor kickerCurrent = kickerTalon.getCurrentSensor();
        public final AngleSensor   kickerEncoder = kickerTalon.getAngleSensor();
        
        public final Switch        kickerSwitch  = Switches.normallyClosed(Properties.CAN_GRAB);
        
        public final Switch pressureSwitch = Switches.normallyOpen(Properties.PRESSURE_SWITCH);
        public final Relay compressorRelay = Hardware.relay(Properties.COMPRESSOR_RELAY);
        
        public final Accelerometer builtInAccel = Accelerometers.builtIn();
        
        public final DistanceSensor frontDistance = Sensors.Distance.analogUltrasonic(Properties.FRONT_ULTRASONIC);
        
        public final PowerPanel powerPanel = Sensors.powerPanel();
    }
    
    private static final class Properties {
        /*-------CONSTANTS------*/
        private static final double GRABBER_ENCODER_PPD = 0.263;
        private static final double GRABBER_MAX_CURRENT = 100;
        private static final double GRABBER_TOLERANCE = 7;
        
        private static final double GRABBER_MAX_RAISE_SPEED = 0.75;
        private static final double GRABBER_MAX_LOWER_SPEED = 0.25;
        private static final double GRABBER_MAX_ANGLE_DEGREES = 90;
        
        private static final double[] KICKER_MOVE_SPEEDS =
                new double[] {0.5, 0.6, 0.75, 0.8, 0.85, 0.9};
                //new double[] {0.2, 0.2441, 0.3701, 0.4173, 0.4516, 0.5478};
        private static final double[] KICKER_HOLD_SPEEDS =
                new double[] {0.0, 0.3, 0.35, 0.65, 0.8, 0.85};
        
        private static final double KICKER_MAX_RAISE_SPEED = 0.75;
        private static final double KICKER_MAX_LOWER_SPEED = 0.3;
        private static final double KICKER_MAX_ANGLE_DEGREES = 160;
        
        private static final double KICKER_ENCODER_PPD = 1.422;
        private static final double KICKER_MAX_CURRENT = 40;
        private static final double KICKER_TOLERANCE = 30;
        
        /*-------JOYSTICK------*/
        private static final int JOYSTICK = 0;
        private static final int CODRIVER = 1;

        /*-------PWM------*/
        private static final int LEFT_FRONT_DRIVE = 0;
        private static final int LEFT_REAR_DRIVE = 1;
        private static final int RIGHT_FRONT_DRIVE = 2;
        private static final int RIGHT_REAR_DRIVE = 3;
        private static final int GRABBER_LIFTER_MOTOR = 4;
        
        /*-------RELAY------*/
        private static final int COMPRESSOR_RELAY = 0;
        
        /*-------POWER PANEL------*/
        private static final int GRABBER_LIFTER_CURRENT = 2;
        
        /*-------ANALOG------*/
        private static final int FRONT_ULTRASONIC = 0;
       
        /*-------SOLENOIDS------*/
        private static final int RAMP_SOLENOID_EXTEND = 2;
        private static final int RAMP_SOLENOID_RETRACT = 6;
        private static final int GUARDRAIL_SOLENOID_EXTEND = 3;
        private static final int GUARDRAIL_SOLENOID_RETRACT = 7;
        private static final int GRABBER_LEFT_SOLENOID_EXTEND = 0;
        private static final int GRABBER_LEFT_SOLENOID_RETRACT = 4;

        private static final int GRABBER_RIGHT_SOLENOID_EXTEND = 1;
        private static final int GRABBER_RIGHT_SOLENOID_RETRACT = 5;
        
        /*-------DIO------*/
        private static final int GRABBER_ENCODER_A = 0;
        private static final int GRABBER_ENCODER_B = 1;
        private static final int GRABBER_LIFTER_HOME = 2;
        private static final int PRESSURE_SWITCH = 3;
        private static final int CAN_GRAB = 5;

        /*-------CAN------*/
        private static final int KICKER_MOTOR_CAN_ID = 0;
        
        /*-------VISION-------*/
        private static final String FRONT_CAMERA_NAME = null;
        private static final String REAR_CAMERA_NAME = null;

        /*-------RIODUINO-------*/
        private static final int RIODUINO_SERIAL_BAUD = 9600;
        private static final SerialPort.Port RIODUINO_SERIAL_PORT = SerialPort.Port.kMXP;
    }
}
