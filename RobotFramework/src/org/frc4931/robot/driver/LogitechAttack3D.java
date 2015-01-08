/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Converts a Logitech Attack 3D to Axis and Buttons.
 * 
 * @author Zach Anderson
 */
public class LogitechAttack3D {
    Joystick joy;
    
    public LogitechAttack3D(int port) {
        joy = new Joystick(port);
    }

    public AnalogAxis getPitch(){
        return ()->joy.getY();
    }
    
    public AnalogAxis getRoll(){
        return ()->joy.getX();
    }
    
    public AnalogAxis getYaw(){
        return ()->joy.getZ();
    }
    
    public AnalogAxis getThrottle(){
        return ()->joy.getThrottle();
    }
    
    public DigitalButton getButton(int button){
        return ()->joy.getRawButton(button);
    }
}
