/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;

public class LogitechGamepadF310 extends DSInputDevice implements Gamepad {
    public LogitechGamepadF310(int port) {
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
        return () -> joystick.getRawAxis(4);
    }

    @Override
    public AnalogAxis getRightY() {
        return () -> joystick.getRawAxis(5) * -1;
    }

    @Override
    public AnalogAxis getLeftTrigger() {
        return () -> joystick.getRawAxis(2);
    }

    @Override
    public AnalogAxis getRightTrigger() {
        return () -> joystick.getRawAxis(3);
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
        return () -> joystick.getRawButton(0);
    }

    @Override
    public Switch getB() {
        return () -> joystick.getRawButton(1);
    }

    @Override
    public Switch getX() {
        return () -> joystick.getRawButton(2);
    }

    @Override
    public Switch getY() {
        return () -> joystick.getRawButton(3);
    }

    @Override
    public Switch getStart() {
        return () -> joystick.getRawButton(7);
    }

    @Override
    public Switch getSelect() {
        return () -> joystick.getRawButton(6);
    }

    @Override
    public Switch getLeftStick() {
        return () -> joystick.getRawButton(8);
    }

    @Override
    public Switch getRightStick() {
        return () -> joystick.getRawButton(9);
    }
}
