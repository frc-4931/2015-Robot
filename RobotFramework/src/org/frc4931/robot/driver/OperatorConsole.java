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

    public SwitchWithIndicator getGrabberToFull() {
        return getButton(3);
    }

    public SwitchWithIndicator getGrabberToHalf() {
        return getButton(5);
    }

    public SwitchWithIndicator getKickerToFull() {
        return getButton(4);
    }

    public SwitchWithIndicator getKickerToTransfer() {
        return getButton(6);
    }

    public SwitchWithIndicator getRampToggle() {
        return getButton(2);
    }

    public SwitchWithIndicator getCounterUp() {
        return getButton(1);
    }

    public SwitchWithIndicator getCounterReset() {
        return getButton(9);
    }

    public AnalogAxis getTopFader() {
        return () -> joystick.getRawAxis(1);
    }

    public AnalogAxis getBottomFader() {
        return () -> joystick.getRawAxis(0);
    }

    public AnalogAxis getLeftTrim() {
        return () -> joystick.getRawAxis(2);
    }

    public AnalogAxis getMiddleTrim() {
        return () -> joystick.getRawAxis(3);
    }

    public AnalogAxis getRightTrim() { return () -> joystick.getRawAxis(4); }
}
