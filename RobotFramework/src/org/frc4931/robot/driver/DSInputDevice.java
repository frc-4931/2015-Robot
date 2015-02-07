/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import edu.wpi.first.wpilibj.Joystick;
import org.frc4931.robot.component.Switch;

public class DSInputDevice implements InputDevice {
    protected final Joystick joystick;

    public DSInputDevice(int port) {
        joystick = new Joystick(port);
    }

    @Override
    public AnalogAxis getAxis(int axis) {
        return () -> joystick.getRawAxis(axis);
    }

    @Override
    public Switch getButton(int button) {
        return () -> joystick.getRawButton(button);
    }

    @Override
    public DirectionalAxis getDPad(int pad) {
        return () -> joystick.getPOV(pad);
    }
}
