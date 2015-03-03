/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.SwitchWithIndicator;

public class OperatorConsole extends DSInputDevice {
    public OperatorConsole(int port) {
        super(port);
    }

    @Override
    public SwitchWithIndicator getButton(int button) {
        return new SwitchWithIndicator() {
            @Override
            public void setIndicator(boolean value) {
                joystick.setOutput(button, value);
            }

            @Override
            public boolean isTriggered() {
                return joystick.getRawButton(button);
            }
        };
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
