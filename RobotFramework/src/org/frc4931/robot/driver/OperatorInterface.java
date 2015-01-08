/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

/**
 * Holds the state of the operator interface.
 * 
 * @author Zach Anderson
 */
public class OperatorInterface {
    public static AnalogAxis wheel;
    public static AnalogAxis throttle;
    
    public static DigitalButton toggleClaw;
    public static DigitalButton toggleLift;
    public static DigitalButton toggleRamp;
    public static DigitalButton toggleRails;
    
    // Modified to fit driver preference
    public static void initialize(LogitechAttack3D joy){
        wheel = joy.getYaw();
        throttle = joy.getPitch();
        
        toggleClaw = joy.getButton(0);
        toggleLift = joy.getButton(1);
        toggleRamp = joy.getButton(2);
        toggleRails = joy.getButton(3);
    }
}
