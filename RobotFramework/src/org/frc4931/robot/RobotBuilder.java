/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.component.DriveTrain;
import org.frc4931.robot.component.LimitedMotor;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.hardware.Hardware;
import org.frc4931.robot.hardware.Hardware.Motors;
import org.frc4931.robot.hardware.Hardware.Sensors.Switches;
import org.frc4931.robot.hardware.Hardware.Solenoids;
import org.frc4931.robot.subsystem.DriveSystem;
import org.frc4931.robot.subsystem.LoaderArm;
import org.frc4931.robot.subsystem.Ramp;

/**
 * Instantiates all of the robot componets and wires them togther.
 */
public class RobotBuilder {
    public static void build(){
        Robot.Componets.drive = new DriveSystem(DriveTrain.create(
                  Motor.compose(
                          Motors.talon(Properties.LEFT_FRONT_DRIVE),
                          Motors.talon(Properties.LEFT_REAR_DRIVE)),
                  Motor.compose(
                          Motors.talon(Properties.RIGHT_FRONT_DRIVE),
                          Motors.talon(Properties.RIGHT_REAR_DRIVE))),
                  Hardware.relay(Properties.DRIVE_SHIFTER),
                  null);
        
        Robot.Componets.grabber = new LoaderArm(
                Solenoids.doubleSolenoid(
                             Properties.GRABBER_LIFTER_EXTEND,
                             Properties.GRABBER_LIFTER_RETRACT,
                             Solenoid.Direction.EXTENDING),
                Solenoids.doubleSolenoid(
                             Properties.GRABBER_CLAW_EXTEND,
                             Properties.GRABBER_CLAW_RETRACT,
                             Solenoid.Direction.EXTENDING),
                Switches.normallyClosed(Properties.GRABBER_SWITCH_CANGRAB),
                Switches.normallyClosed(Properties.GRABBER_SWITCH_DIDGRAB),
                null);
        
        Robot.Componets.ramp = new Ramp(
                Solenoids.doubleSolenoid(
                         Properties.RAMP_LIFTER_EXTEND,
                         Properties.RAMP_LIFTER_RETRACT,
                         Solenoid.Direction.RETRACTING),
                Solenoids.doubleSolenoid(
                         Properties.RAMP_STACK_LIFTER_EXTEND,
                         Properties.RAMP_STACK_LIFTER_RETRACT,
                         Solenoid.Direction.RETRACTING),
                Solenoids.doubleSolenoid(
                         Properties.RAMP_STATCK_PUSHER_EXTEND,
                         Properties.RAMP_STACK_PUSHER_RETRACT,
                         Solenoid.Direction.RETRACTING),
                new LimitedMotor(
                         Motors.talon(Properties.GUARDRAIL_MOTOR),
                         Switches.normallyClosed(Properties.GUARDRAIL_OPEN_SWITCH),
                         Switches.normallyClosed(Properties.GUARDRAIL_CLOSE_SWITCH)),
                null);
    }
    
    private static final class Properties {
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
