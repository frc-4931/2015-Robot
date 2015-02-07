/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;

/**
 * A type of input device consisting of a joystick with twist and throttle.
 */
public interface FlightStick extends InputDevice {
    public AnalogAxis getPitch();
    public AnalogAxis getYaw();
    public AnalogAxis getRoll();
    public AnalogAxis getThrottle();
    public Switch getTrigger();
    public Switch getThumb();
}
