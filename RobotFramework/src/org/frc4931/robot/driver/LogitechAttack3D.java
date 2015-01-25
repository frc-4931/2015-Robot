/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.Joystick;


/**
 * Converts a Logitech Attack 3D to Axis and Buttons.
 * 
 * @author Zach Anderson
 */
public class LogitechAttack3D extends org.frc4931.robot.driver.Joystick{
    private final Joystick joy;
    
    public LogitechAttack3D(int port) {
        joy = new Joystick(port);
    }

    @Override
    public AnalogAxis getPitch(){
        return ()->joy.getY()*-1;
    }
    
    @Override
    public AnalogAxis getRoll(){
        return ()->joy.getX();
    }
    
    @Override
    public AnalogAxis getYaw(){
        return ()->joy.getZ();
    }
    
    @Override
    public AnalogAxis getThrottle(){
        return ()->joy.getThrottle();
    }
    
    @Override
    public Switch getButton(int button){
        return ()->joy.getRawButton(button);
    }
}
