/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;

public class MicrosoftSideWinder extends DSInputDevice implements FlightStick {

    public MicrosoftSideWinder(int port) {
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
        return () ->  joystick.getRawButton(0);
    }

    @Override
    public Switch getThumb() {
        return () -> joystick.getRawButton(1);
    }

    @Override
    public Switch getButton(int button) {
        return () -> joystick.getRawButton(button);
    }
}
