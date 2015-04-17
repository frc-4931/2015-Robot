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
 * 
 */
public class LogitechAttack3 {
    public final Joystick joystick;
    
    public LogitechAttack3(int channel) {
        this.joystick = new Joystick(channel);
    }
    public Switch getButton(int button) {
        return ()->joystick.getRawButton(button);
    }
}
