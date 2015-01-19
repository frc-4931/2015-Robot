/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Relay;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Switch;
import org.frc4931.robot.driver.OperatorInterface;
import org.frc4931.robot.subsystem.DriveSystem;
import org.frc4931.robot.subsystem.LoaderArm;
import org.frc4931.robot.subsystem.Ramp;
import org.frc4931.robot.subsystem.VisionSystem;
import org.frc4931.utils.Lifecycle;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
    public static final int NUMBER_OF_ADC_BITS = 12;
    private static Robot instance;
    private Systems systems;
    private OperatorInterface operatorInterface;

    public Systems getSubsystems() {
        return systems;
    }

    @Override
    public void robotInit() {
        instance = this;
        Components components = RobotBuilder.components();
        systems = RobotBuilder.build(components);
        operatorInterface = RobotBuilder.operatorInterface();

        // Start each of the subsystems and other objects that need initializing ...
        systems.startup();
    }

    @Override
    public void free() {
        systems.shutdown();
        super.free();
    }

    public static Robot getInstance() {
        return instance;
    }

    public static final class Systems implements Lifecycle {
        public final DriveSystem drive;
        public final LoaderArm grabber;
        public final Ramp ramp;
        public final VisionSystem vision;

        public Systems(DriveSystem drive, LoaderArm arm, Ramp ramp, VisionSystem vision) {
            this.drive = drive;
            this.grabber = arm;
            this.ramp = ramp;
            this.vision = vision;
        }

        @Override
        public void startup() {
            drive.startup();
            grabber.startup();
            ramp.startup();
            vision.startup();
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
                        vision.shutdown();
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

        Solenoid rampLifterActuator();
        Motor leadScrewActuator();
        Switch leadScrewLowerSwitch();
        Switch leadScrewStepSwitch();
        Switch leadScrewToteSwitch();
        Switch leadScrewToteOnStepSwitch();
        Motor kickerActuator();
        Switch kickerLowerSwitch();
        Switch kickerUpperSwitch();
        Motor guardRailActuator();

        Switch guardRailOpenSwitch();

        Switch guardRailClosedSwitch();

        String frontCameraName();

        String rearCameraName();
    }
}
