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

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
    private static Robot instance;
    private Systems systems;
    
    public Systems getSubsystems(){
        return systems;
    }
    
    @Override
    public void robotInit() {
        instance = this;
        Components components = RobotBuilder.components();
        systems = RobotBuilder.build(components);
    }
    
    public static Robot getInstance(){
        return instance;
    }
    
    public static final class Systems {
        public final OperatorInterface operatorInterface;
        public final DriveSystem drive;
        public final LoaderArm grabber;
        public final Ramp ramp;
        
        public Systems(OperatorInterface operatorInterface, DriveSystem drive, LoaderArm arm, Ramp ramp){
            this.operatorInterface = operatorInterface;
            this.drive = drive;
            this.grabber = arm;
            this.ramp = ramp;
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
    }
}
