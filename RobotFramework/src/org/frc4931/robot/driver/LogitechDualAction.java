/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;

public class LogitechDualAction extends DSInputDevice implements Gamepad {
    public LogitechDualAction(int port) {
        super(port);
    }

    @Override
    public AnalogAxis getLeftX() {
        return () -> joystick.getRawAxis(0);
    }

    @Override
    public AnalogAxis getLeftY() {
        return () -> joystick.getRawAxis(1) * -1;
    }

    @Override
    public AnalogAxis getRightX() {
        return () -> joystick.getRawAxis(2);
    }

    @Override
    public AnalogAxis getRightY() {
        return () -> joystick.getRawAxis(3) * -1;
    }

    @Override
    public AnalogAxis getLeftTrigger() {
        return () -> joystick.getRawButton(6) ? 1.0 : 0.0;
    }

    @Override
    public AnalogAxis getRightTrigger() {
        return () -> joystick.getRawButton(7) ? 1.0 : 0.0;
    }

    @Override
    public Switch getLeftBumper() {
        return () -> joystick.getRawButton(4);
    }

    @Override
    public Switch getRightBumper() {
        return () -> joystick.getRawButton(5);
    }

    @Override
    public Switch getA() {
        return () -> joystick.getRawButton(1);
    }

    @Override
    public Switch getB() {
        return () -> joystick.getRawButton(2);
    }

    @Override
    public Switch getX() {
        return () -> joystick.getRawButton(0);
    }

    @Override
    public Switch getY() {
        return () -> joystick.getRawButton(3);
    }

    @Override
    public Switch getStart() {
        return () -> joystick.getRawButton(9);
    }

    @Override
    public Switch getSelect() {
        return () -> joystick.getRawButton(8);
    }

    @Override
    public Switch getLeftStick() {
        return () -> joystick.getRawButton(10);
    }

    @Override
    public Switch getRightStick() {
        return () -> joystick.getRawButton(11);
    }
}
