/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;

/**
 * Abstract joystick that provides human readable method names for joystick properties.
 * 
 */
public abstract class Joystick {
    public abstract AnalogAxis getPitch();
    public abstract AnalogAxis getYaw();
    public abstract AnalogAxis getRoll();
    public abstract AnalogAxis getThrottle();
    
    public abstract Switch getButton(int button);
}
