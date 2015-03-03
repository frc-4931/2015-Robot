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

    public AnalogAxis getFader1() {
        return () -> joystick.getRawAxis(0);
    }

    public AnalogAxis getFader2() {
        return () -> joystick.getRawAxis(1);
    }

    public AnalogAxis getTrim1() {
        return () -> joystick.getRawAxis(2);
    }

    public AnalogAxis getTrim2() {
        return () -> joystick.getRawAxis(3);
    }

    public AnalogAxis getTrim3() { return () -> joystick.getRawAxis(4); }
}
