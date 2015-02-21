/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.component.Accelerometer;
import org.frc4931.robot.component.AngleSensor;
import org.frc4931.robot.component.DriveTrain;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Solenoid.Direction;
import org.frc4931.robot.component.Switch;
import org.frc4931.robot.composites.CompositeGrabber;
import org.frc4931.robot.composites.CompositeGuardrail;
import org.frc4931.robot.composites.CompositeKicker;
import org.frc4931.robot.composites.CompositeLifter;
import org.frc4931.robot.hardware.Hardware.Motors;
import org.frc4931.robot.hardware.Hardware.Sensors;
import org.frc4931.robot.hardware.Hardware.Sensors.Accelerometers;
import org.frc4931.robot.hardware.Hardware.Sensors.Switches;
import org.frc4931.robot.hardware.Hardware.Solenoids;
import org.frc4931.robot.hardware.PIDMotorWithAngle;

import edu.wpi.first.wpilibj.SerialPort;

/**
 * 
 */
public class RobotBuilder {
    public RobotSystems buildRobot() {
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
                             Properties.GRABBER_TOLERANCE, Properties.GRABBER_MAX_CURRENT),
                             componets.grabberLeftGrabber, componets.grabberRightGrabber);
                
        // Build the Kicker
        Kicker kicker          = new CompositeKicker(componets.kickerMotor);
        Switch canCapture      = componets.kickerSwitch;
        KickerSwitchSystem kss = new KickerSwitchSystem(kicker, canCapture);
        
        // Build the ramp
        Lifter    lifter = new CompositeLifter(componets.rampLifter);
        Guardrail rail   = new CompositeGuardrail(componets.guardrailGrabber);
        Ramp      ramp   = new Ramp(lifter, rail);
        
        Superstructure structure = new Superstructure(grabber, kss, ramp);
        
        // Build the accelerometer
        Accelerometer accel = componets.builtInAccel;
        
        return new RobotSystems(drive, accel, structure, powerPanel);
    }
    
    private static final class Componets {
        public Componets() { }

        // Drive
        public final Motor leftFront  = Motors.talon(Properties.LEFT_FRONT_DRIVE);
        public final Motor leftRear   = Motors.talon(Properties.LEFT_REAR_DRIVE);
        public final Motor rightFront = Motors.talon(Properties.RIGHT_FRONT_DRIVE);
        public final Motor rightRear  = Motors.talon(Properties.RIGHT_REAR_DRIVE);

        // Grabber
        public final Motor       grabberLifter  = Motors.victor(Properties.GRABBER_LIFTER_MOTOR);
        public final Switch      grabberHome    = Switches.normallyClosed(Properties.GRABBER_LIFTER_HOME);
        public final AngleSensor grabberEncoder = Sensors.encoder(Properties.GRABBER_ENCODER_A, Properties.GRABBER_ENCODER_B, Properties.GRABBER_ENCODER_DPP);
    
        public final Solenoid grabberLeftGrabber  = Solenoids.doubleSolenoid(Properties.GRABBER_LEFT_SOLENOID_EXTEND, Properties.GRABBER_LEFT_SOLENOID_RETRACT, Direction.RETRACTING);
        public final Solenoid grabberRightGrabber = Solenoids.doubleSolenoid(Properties.GRABBER_RIGHT_SOLENOID_EXTEND, Properties.GRABBER_RIGHT_SOLENOID_RETRACT, Direction.RETRACTING);

        // Ramp
        public final Solenoid rampLifter        = Solenoids.doubleSolenoid(Properties.RAMP_SOLENOID_EXTEND, Properties.RAMP_SOLENOID_RETRACT, Direction.EXTENDING);
        public final Solenoid guardrailGrabber  = Solenoids.doubleSolenoid(Properties.GUARDRAIL_SOLENOID_EXTEND, Properties.GUARDRAIL_SOLENOID_RETRACT, Direction.RETRACTING);
        
        public final MotorWithAngle kickerMotor  = Motors.talonSRX(Properties.KICKER_MOTOR_CAN_ID, Switches.normallyClosed(Properties.KICKER_HOME),
                                                                     Properties.KICKER_TOLERANCE, Properties.KICKER_ENCODER_DPP, Properties.KICKER_MAX_CURRENT);
        public final Switch         kickerSwitch = Switches.normallyClosed(Properties.CAN_GRAB);
        
        public final Accelerometer builtInAccel = Accelerometers.builtIn();
        
        public final PowerPanel powerPanel = Sensors.powerPanel();
    }
    
    private static final class Properties {
        /*-------CONSTANTS------*/
        private static final double GRABBER_ENCODER_DPP = 3.6861;
        private static final double GRABBER_MAX_CURRENT = 4;
        private static final double GRABBER_TOLERANCE = 5;
        private static final double KICKER_ENCODER_DPP = 2.8444;
        private static final double KICKER_MAX_CURRENT = 4;
        private static final double KICKER_TOLERANCE = 5;
        
        /*-------JOYSTICK------*/
        private static final int JOYSTICK = 0;

        /*-------MOTORS------*/
        private static final int LEFT_FRONT_DRIVE = 0;
        private static final int LEFT_REAR_DRIVE = 1;
        private static final int RIGHT_FRONT_DRIVE = 2;
        private static final int RIGHT_REAR_DRIVE = 3;
        private static final int GRABBER_LIFTER_MOTOR = 4;
        
        /*-------POWER PANEL------*/
        private static final int GRABBER_LIFTER_CURRENT = 2;
        private static final int KICKER_CURRENT = 3;
       
        /*-------SOLENOIDS------*/
        private static final int RAMP_SOLENOID_EXTEND = 0;
        private static final int RAMP_SOLENOID_RETRACT = 1;
        private static final int GUARDRAIL_SOLENOID_EXTEND = 4;
        private static final int GUARDRAIL_SOLENOID_RETRACT = 5;
        private static final int GRABBER_LEFT_SOLENOID_EXTEND = 2;
        private static final int GRABBER_LEFT_SOLENOID_RETRACT = 3;

        private static final int GRABBER_RIGHT_SOLENOID_EXTEND = 6;
        private static final int GRABBER_RIGHT_SOLENOID_RETRACT = 7;
        
        /*-------DIO------*/
        private static final int GRABBER_LIFTER_HOME = 2;
        private static final int GRABBER_ENCODER_A = 3;
        private static final int GRABBER_ENCODER_B = 4;
        private static final int CAN_GRAB = 5;
        private static final int KICKER_HOME = 6;

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
