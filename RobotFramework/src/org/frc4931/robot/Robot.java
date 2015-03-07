/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.component.DataStream;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.component.Relay;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Switch;
import org.frc4931.robot.driver.OperatorInterface;
import org.frc4931.robot.subsystem.DriveSystem;
import org.frc4931.robot.subsystem.LoaderArm;
import org.frc4931.robot.subsystem.Ramp;
import org.frc4931.robot.subsystem.StackIndicatorLight;
import org.frc4931.robot.subsystem.VisionSystem;
import org.frc4931.utils.Lifecycle;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Robot extends IterativeRobot {
    public static final int NUMBER_OF_ADC_BITS = 12;
    private static Robot instance;
    private static long startTime = System.nanoTime();
    private static long startTimeInMillis = System.currentTimeMillis();
    private Systems systems;
    private OperatorInterface operatorInterface;

    public Systems getSubsystems() {
        return systems;
    }

    @Override
    public void robotInit() {
        instance = this;
        SwitchListener.getInstance().start();
        Components components = RobotBuilder.components();
        systems = RobotBuilder.build(components);
        operatorInterface = RobotBuilder.operatorInterface();
        PowerDistributionPanel pdp = new PowerDistributionPanel();
        Logger.getInstance().registerSwitch("Arm lowered", components.armLifterLowerSwitch());
        Logger.getInstance().registerSwitch("Arm raised", components.armLifterUpperSwitch());
        Logger.getInstance().registerSwitch("Capturable", components.capturableSwitch());
        Logger.getInstance().registerSwitch("Captured", components.capturedSwitch());
        Logger.getInstance().registerSwitch("Guardrail opened", components.guardRailOpenSwitch());
        Logger.getInstance().registerSwitch("Guardrail closed", components.guardRailClosedSwitch());
        Logger.getInstance().register("Kicker motor angle", () -> (short) (components.kickerMotor().getAngle()) * 1000);
        Logger.getInstance().register("Right drive motor speed", ()->components.rightDriveMotor().getSpeedAsShort());
        Logger.getInstance().register("Left drive motor speed", ()->components.leftDriveMotor().getSpeedAsShort());
        Logger.getInstance().register("Arm motor speed", ()->components.armLifterActuator().getSpeedAsShort());
        Logger.getInstance().register("Kicker motor speed", () -> components.kickerMotor().getSpeedAsShort());
        Logger.getInstance().register("Guardrail motor speed", ()->components.guardRailActuator().getSpeedAsShort());
        Logger.getInstance().register("Channel 15 Current", () -> (short) (pdp.getCurrent(15) * 1000));
        Logger.getInstance().register("Channel 14 Current", () -> (short) (pdp.getCurrent(14) * 1000));
        Logger.getInstance().startup();
        // Start each of the subsystems and other objects that need initializing ...
        systems.startup();
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        double throttle = operatorInterface.throttle.read();
        double wheel = operatorInterface.wheel.read();
        double driveSpeed = operatorInterface.driveSpeed.read();
        double turnSpeed = operatorInterface.turnSpeed.read();

        boolean quickTurn = operatorInterface.quickTurn.isTriggered();
        if(Math.abs(driveSpeed)<0.05)
            quickTurn = true;
        systems.drive.cheesy(throttle*0.5, wheel*0.5, quickTurn);
        if(operatorInterface.writeData.isTriggered())
            Logger.getInstance().shutdown();
    }

    @Override
    public void free() {
        systems.shutdown();
        super.free();
    }

    public static Robot getInstance() {
        return instance;
    }

    public static long time() {
        return System.nanoTime()-startTime;
    }

    public static int elapsedTimeInMillis() {
        return (int)(System.currentTimeMillis()-startTimeInMillis);
    }
    
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
