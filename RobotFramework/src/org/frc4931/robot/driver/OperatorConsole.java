/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

public class OperatorConsole extends DSInputDevice {
    public OperatorConsole(int port) {
        super(port);
    }

    public AnalogAxis getLeftFader() {
        return () -> joystick.getRawAxis(0);
    }

    public AnalogAxis getRightFader() {
        return () -> joystick.getRawAxis(1);
    }

    public AnalogAxis getLeftTrim() {
        return () -> joystick.getRawAxis(2);
    }

    public AnalogAxis getRightTrim() {
        return () -> joystick.getRawAxis(3);
    }
}
