/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.commandnew.Scheduler;
import org.frc4931.robot.commandnew.grabber.ToggleGrabber;
import org.frc4931.robot.commandnew.grabber.ToggleGrabberLift;
import org.frc4931.robot.commandnew.guardrail.ToggleGuardrail;
import org.frc4931.robot.commandnew.kicker.MoveKickerToGround;
import org.frc4931.robot.commandnew.kicker.MoveKickerToGuardrail;
import org.frc4931.robot.commandnew.kicker.MoveKickerToTransfer;
import org.frc4931.robot.commandnew.ramplifter.ToggleRamp;
import org.frc4931.robot.component.DataStream;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.component.Relay;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Switch;
import org.frc4931.robot.subsystem.DriveSystem;
import org.frc4931.robot.subsystem.LoaderArm;
import org.frc4931.robot.subsystem.Ramp;
import org.frc4931.robot.subsystem.StackIndicatorLight;
import org.frc4931.robot.subsystem.VisionSystem;
import org.frc4931.robot.system.Robot;
import org.frc4931.robot.system.RobotBuilder;
import org.frc4931.utils.Lifecycle;

import edu.wpi.first.wpilibj.IterativeRobot;

public class RobotManager extends IterativeRobot {
    private static final long START_TIME = System.currentTimeMillis();
    public static final int NUMBER_OF_ADC_BITS = 12;
    
    private static Robot robot;

    public Robot get() {
        return robot;
    }

    @Override
    public void robotInit() {
        robot = RobotBuilder.buildRobot();
        
        SwitchListener.getInstance().onTriggered(robot.operator.toggleLift, ()->Scheduler.getInstance().add(new ToggleGrabberLift(robot.structure.grabber)));
        SwitchListener.getInstance().onTriggered(robot.operator.toggleClaw, ()->Scheduler.getInstance().add(new ToggleGrabber(robot.structure.grabber)));
        SwitchListener.getInstance().onTriggered(robot.operator.toggleRails, ()->Scheduler.getInstance().add(new ToggleGuardrail(robot.structure.ramp.rail)));
        SwitchListener.getInstance().onTriggered(robot.operator.toggleRamp, ()->Scheduler.getInstance().add(new ToggleRamp(robot.structure.ramp.lifter)));
        
        SwitchListener.getInstance().onTriggered(robot.operator.kickerToGround, ()->Scheduler.getInstance().add(new MoveKickerToGround(robot.structure.kickerSystem.kicker)));
        SwitchListener.getInstance().onTriggered(robot.operator.kickerToTransfer, ()->Scheduler.getInstance().add(new MoveKickerToTransfer(robot.structure.kickerSystem.kicker)));
        SwitchListener.getInstance().onTriggered(robot.operator.kickerToGuardrail, ()->Scheduler.getInstance().add(new MoveKickerToGuardrail(robot.structure.kickerSystem.kicker)));
       
        SwitchListener.getInstance().onTriggered(robot.operator.writeData, Logger.getInstance()::shutdown);
        SwitchListener.getInstance().onTriggered(robot.operator.writeData, ()->System.out.println("DATA SAVED"));
        
        SwitchListener.getInstance().start();

        Logger.getInstance().register("Drive Speed", ()->(int)(robot.operator.driveSpeed.read()*1000));
        Logger.getInstance().register("Turn Speed",  ()->(int)(robot.operator.turnSpeed.read()*1000));
        Logger.getInstance().register("Throttle",    ()->(int)(robot.operator.throttle.read()*1000));
        
        Logger.getInstance().register("Guardrail Status",     ()->robot.componets.guardrailGrabber.isExtending() ? 1 : 0);
        Logger.getInstance().register("Left Grabber Status",  ()->robot.componets.grabberLeftGrabber.isExtending() ? 1 : 0);
        Logger.getInstance().register("Right Grabber Status", ()->robot.componets.grabberRightGrabber.isExtending() ? 1 : 0);
        Logger.getInstance().register("Ramp Lifter Status",   ()->robot.componets.rampLifter.isExtending() ? 1 : 0);
        
        Logger.getInstance().register("Kicker Position", ()->(int)(robot.componets.kickerMotor.getAngleSensor().getAngle()*1000));
        Logger.getInstance().register("Kicker Current",  ()->(int)(robot.componets.kickerMotor.getCurrentSensor().getCurrent()*1000));
        
        Logger.getInstance().register("Grabber Position", ()->(int)(robot.componets.grabberEncoder.getAngle()*1000));
        
        Logger.getInstance().register("X Accel", ()->(int)(robot.componets.builtInAccel.getXacceleration()*1000));
        Logger.getInstance().register("Y Accel", ()->(int)(robot.componets.builtInAccel.getYacceleration()*1000));
        Logger.getInstance().register("Z Accel", ()->(int)(robot.componets.builtInAccel.getZacceleration()*1000));
        
        Logger.getInstance().startup();
    }
    
    public void activePeriodic() {
        Scheduler.getInstance().step(time());
    }
    
    @Override
    public void disabledInit() {
        Scheduler.getInstance().killAll();
    }
    
    // Active code starts here
    @Override
    public void autonomousInit() {
        
    }
    
    @Override
    public void autonomousPeriodic() {
        activePeriodic();
    }
    
    @Override
    public void teleopInit() {
        robot.structure.ramp.lifter.lower();
        robot.structure.ramp.rail.open();
        robot.structure.grabber.open();
        
        robot.structure.kickerSystem.kicker.home();
        robot.structure.grabber.home();
    }

    @Override
    public void teleopPeriodic() {
        activePeriodic();
        
        double driveSpeed = robot.operator.driveSpeed.read();
        double turnSpeed = robot.operator.turnSpeed.read()*-1;

        // Map throttle from [-1.0, 1.0] to [0.0, 1.0]
        double throttle = (robot.operator.throttle.read() - 1)/2.0;

        robot.drive.arcade(driveSpeed * throttle, turnSpeed * throttle);
    }
        
    public static long time() {
        return System.currentTimeMillis() - START_TIME;
    }

    @Deprecated
    public static final class Systems implements Lifecycle {
        public final DriveSystem drive;
        public final LoaderArm grabber;
        public final Ramp ramp;
        public final VisionSystem vision;
        public final StackIndicatorLight stackIndicator;

        public Systems(DriveSystem drive, LoaderArm arm, Ramp ramp, VisionSystem vision, StackIndicatorLight stackIndicator) {
            this.drive = drive;
            this.grabber = arm;
            this.ramp = ramp;
            this.vision = vision;
            this.stackIndicator = stackIndicator;
        }

        @Override
        public void startup() {
            drive.startup();
            grabber.startup();
            ramp.startup();
            vision.startup();
            stackIndicator.startup();
        }

        @Override
        public void shutdown() {
            try {
                drive.shutdown();
            } finally {
                try {
                    grabber.shutdown();
                } finally {
                    try {
                        ramp.shutdown();
                    } finally {
                        try {
                            vision.shutdown();
                        } finally {
                            stackIndicator.shutdown();
                        }
                    }
                }
            }
        }
    }

    @Deprecated
    public static interface Components {
        Relay shifter();

        Motor leftDriveMotor();

        Motor rightDriveMotor();
        Motor armLifterActuator();
        Switch armLifterLowerSwitch();
        Switch armLifterUpperSwitch();
        Solenoid grabberActuator();

        Switch capturableSwitch();

        Switch capturedSwitch();

        MotorWithAngle kickerMotor();

        Solenoid rampLifterActuator();

        Motor guardRailActuator();

        Switch guardRailOpenSwitch();

        Switch guardRailClosedSwitch();

        String frontCameraName();

        String rearCameraName();

        DataStream rioDuinoDataStream();
    }
}
