/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;

/**
 * A type of input device similar to an Xbox controller.
 */
public interface Gamepad extends InputDevice {
    public abstract AnalogAxis getLeftX();
    public abstract AnalogAxis getLeftY();
    public abstract AnalogAxis getRightX();
    public abstract AnalogAxis getRightY();
    public abstract AnalogAxis getLeftTrigger();
    public abstract AnalogAxis getRightTrigger();
    public abstract Switch getLeftBumper();
    public abstract Switch getRightBumper();
    public abstract Switch getA();
    public abstract Switch getB();
    public abstract Switch getX();
    public abstract Switch getY();
    public abstract Switch getStart();
    public abstract Switch getSelect();
    public abstract Switch getLeftStick();
    public abstract Switch getRightStick();
}
