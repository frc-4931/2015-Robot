/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;


/**
 * Converts a Logitech Attack 3D to Axis and Buttons.
 * 
 * @author Zach Anderson
 */
public class LogitechAttack3D extends DSInputDevice implements FlightStick {

    public LogitechAttack3D(int port) {
        super(port);
    }

    @Override
    public AnalogAxis getPitch() {
        return () -> joystick.getY() * -1;
    }

    @Override
    public AnalogAxis getYaw() {
        return joystick::getTwist;
    }

    @Override
    public AnalogAxis getRoll() {
        return joystick::getX;
    }

    @Override
    public AnalogAxis getThrottle() {
        return joystick::getThrottle;
    }

    @Override
    public Switch getTrigger() {
        return () -> joystick.getRawButton(0);
    }
}
