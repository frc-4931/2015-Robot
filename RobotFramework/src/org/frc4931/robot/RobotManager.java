/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.commandnew.Scheduler;
import org.frc4931.robot.commandnew.auto.GrabAndGoAuto;
import org.frc4931.robot.commandnew.auto.TransferTote;
import org.frc4931.robot.commandnew.grabber.ToggleGrabber;
import org.frc4931.robot.commandnew.grabber.ToggleGrabberLift;
import org.frc4931.robot.commandnew.grabber.ToggleGrabberStep;
import org.frc4931.robot.commandnew.guardrail.ToggleGuardrail;
import org.frc4931.robot.commandnew.kicker.ToggleKickerGuardrail;
import org.frc4931.robot.commandnew.kicker.ToggleKickerTransfer;
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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotManager extends IterativeRobot {
    private static final long START_TIME = System.currentTimeMillis();
    public static final int NUMBER_OF_ADC_BITS = 12;
    
    private static Robot robot;
    
    private boolean home = false;
    
    private Scheduler scheduler;
    
    public Robot get() {
        return robot;
    }

    @Override
    public void robotInit() {
        robot = RobotBuilder.buildRobot();
        
        scheduler = new Scheduler();
        Executor.getInstance().register(scheduler);
        
        SwitchListener listener = new SwitchListener();
        Logger logger = new Logger();
        
        listener.onTriggered(robot.operator.toggleClaw, ()->scheduler.add(new ToggleGrabber(robot.structure.grabber)));
        listener.onTriggered(robot.operator.toggleRails, ()->scheduler.add(new ToggleGuardrail(robot.structure.ramp.rail)));
        listener.onTriggered(robot.operator.toggleRamp, ()->scheduler.add(new ToggleRamp(robot.structure.ramp.lifter)));
        
        listener.onTriggered(robot.operator.transferTote, ()->scheduler.add(new TransferTote(robot)));
       
        listener.onTriggered(robot.operator.writeData, logger::shutdown);
        listener.onTriggered(robot.operator.writeData, ()->System.out.println("DATA SAVED"));
        
        listener.whileUntriggered(()->robot.compressor.isPressurized(), ()->robot.compressor.activate());
        listener.whileTriggered(()->robot.compressor.isPressurized(), ()->robot.compressor.deactivate());
        
        listener.onTriggered(robot.operator.resetCounter, robot.toteCounter::reset);
        listener.onTriggered(robot.operator.increaseCounter, robot.toteCounter::increase);
        
        listener.onTriggered(robot.operator.grabberToStep, ()->scheduler.add(new ToggleGrabberStep(robot.structure.grabber)));
        listener.onTriggered(robot.operator.grabberToTransfer, ()->scheduler.add(new ToggleGrabberLift(robot.structure.grabber)));

        listener.onTriggered(robot.operator.kickerToTransfer, ()->scheduler.add(new ToggleKickerTransfer(robot.structure.kickerSystem.kicker)));
        listener.onTriggered(robot.operator.kickerToGuardrail, ()->scheduler.add(new ToggleKickerGuardrail(robot.structure.kickerSystem.kicker)));
        
        Executor.getInstance().register(listener);

        logger.register("Drive Speed", ()->(short)(robot.operator.driveSpeed.read()*1000));
        logger.register("Turn Speed",  ()->(short)(robot.operator.turnSpeed.read()*1000));
        logger.register("Throttle",    ()->(short)(robot.operator.throttle.read()*1000));
        
        logger.register("Guardrail Status",     ()->robot.componets.guardrailGrabber.isExtending() ? 1 : 0);
        logger.register("Left Grabber Status",  ()->robot.componets.grabberLeftGrabber.isExtending() ? 1 : 0);
        logger.register("Right Grabber Status", ()->robot.componets.grabberRightGrabber.isExtending() ? 1 : 0);
        logger.register("Ramp Lifter Status",   ()->robot.componets.rampLifter.isExtending() ? 1 : 0);
        
        logger.register("Kicker Position", ()->(short)(Math.round(robot.componets.kickerEncoder.getAngle()*100)));
        logger.register("Kicker Current",  ()->(short)(robot.componets.kickerCurrent.getCurrent()*1000));
        
        logger.register("Kicker Speed", ()->(short)(robot.componets.kickerMotor.getSpeed()*1000));
        
        logger.register("Totes", ()->(short)robot.toteCounter.get());
        
        logger.register("Grabber Position", ()->(short)Math.round(robot.componets.grabberEncoder.getAngle()*100));
        
        logger.register("X Accel", ()->(short)(robot.componets.builtInAccel.getXacceleration()*1000));
        logger.register("Y Accel", ()->(short)(robot.componets.builtInAccel.getYacceleration()*1000));
        logger.register("Z Accel", ()->(short)(robot.componets.builtInAccel.getZacceleration()*1000));
        
        logger.startup();
        
        Executor.getInstance().start();
    }
    
    public void robotPeriodic() {
    }
    
    public void activePeriodic() {
        robotPeriodic();
        SmartDashboard.putNumber("Tote Count", robot.toteCounter.get());
        SmartDashboard.putNumber("Front Distance", robot.frontDistance.getDistance() - 6);
    }
    
    @Override
    public void disabledInit() {
        scheduler.killAll();
        robot.toteCounter.reset();
    }
    
    @Override
    public void disabledPeriodic() {
        robotPeriodic();
    }
    
    // Active code starts here
    @Override
    public void autonomousInit() {
        robot.structure.ramp.lifter.raise();
        robot.structure.ramp.rail.open();
        robot.structure.grabber.open();
        
        if(!home) {
            robot.structure.kickerSystem.kicker.home();
            robot.structure.grabber.home();
            home = true;
        }
        scheduler.add(new GrabAndGoAuto(robot.drive, robot.structure));
    }
    
    @Override
    public void autonomousPeriodic() {
        activePeriodic();
    }
    
    @Override
    public void teleopInit() {
        scheduler.killAll();
        if(!home) {
            robot.structure.kickerSystem.kicker.home();
            robot.structure.grabber.home();
            home = true;
        }
    }

    @Override
    public void teleopPeriodic() {
        activePeriodic();
        
        double driveSpeed = robot.operator.driveSpeed.read();
        double turnSpeed = robot.operator.turnSpeed.read()*-1;

        // Map throttle from [-1.0, 1.0] to [0.0, 1.0]
        double throttle = (robot.operator.throttle.read() - 1)/1.5;

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
