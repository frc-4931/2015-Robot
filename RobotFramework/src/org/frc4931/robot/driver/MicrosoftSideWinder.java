/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;
import edu.wpi.first.wpilibj.Joystick;

public class MicrosoftSideWinder extends org.frc4931.robot.driver.Joystick {
    private final Joystick joystick;

    public MicrosoftSideWinder(int port) {
        joystick = new Joystick(port);
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
    public Switch getButton(int button) {
        return () -> joystick.getRawButton(button);
    }
}
