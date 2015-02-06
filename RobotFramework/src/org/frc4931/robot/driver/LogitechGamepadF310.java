/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import org.frc4931.robot.component.Switch;

public class LogitechGamepadF310 extends org.frc4931.robot.driver.Joystick {
    private Joystick joystick;

    public LogitechGamepadF310(int port) {
        joystick = new Joystick(port);
    }

    @Override
    public AnalogAxis getPitch() {
        return () -> joystick.getX() * -1;
    }

    @Override
    public AnalogAxis getYaw() {
        return joystick::getY;
    }

    @Override
    public AnalogAxis getRoll() {
        return joystick::getZ;
    }

    @Override
    public AnalogAxis getThrottle() {
        return joystick::getTwist;
    }

    @Override
    public Switch getButton(int button) {
        return () -> joystick.getRawButton(button);
    }
}
