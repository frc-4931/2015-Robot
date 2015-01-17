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
        SwitchListener.getInstance().start();
        Components components = RobotBuilder.components();
        systems = RobotBuilder.build(components);
    }
    
    public static Robot getInstance(){
        return instance;
    }
    
    public static final class Systems {
        public final DriveSystem drive;
        public final LoaderArm grabber;
        public final Ramp ramp;
        
        public Systems(DriveSystem drive, LoaderArm arm, Ramp ramp){
            this.drive = drive;
            this.grabber = arm;
            this.ramp = ramp;
        }
    }
    
    public static interface Components {
        Relay shifter();
        Motor leftDriveMotor();
        Motor rightDriveMotor();
        Solenoid armLifterActuator();
        Solenoid grabberActuator();
        Switch capturableSwitch();
        Switch capturedSwitch();
        Solenoid rampLifterActuator();
        Solenoid stackLifterActuator();
        Solenoid kickerActuator();
        Motor guardRailActuator();
        Switch guardRailOpenSwitch();
        Switch guardRailClosedSwitch();
    }
}
