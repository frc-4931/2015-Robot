/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;

/**
 * Holds the state of the operator interface.
 * 
 * @author Zach Anderson
 */
public class OperatorInterface {
    /* Cheesy */
    public final AnalogAxis wheel;
    public final AnalogAxis throttle;

    /* Arcade */
    public final AnalogAxis driveSpeed;
    public final AnalogAxis turnSpeed;

    public final Switch toggleClaw;
    public final Switch toggleLift;
    public final Switch toggleRamp;
    public final Switch toggleRails;

    public final Switch writeData;
    public final Switch quickTurn;

    public OperatorInterface(FlightStick flightStick) {
        throttle = flightStick.getPitch();
        wheel = flightStick.getYaw();
        quickTurn = flightStick.getButton(5);

        driveSpeed = flightStick.getPitch();
        turnSpeed = flightStick.getRoll();

        toggleClaw = flightStick.getTrigger();
        toggleLift = flightStick.getThumb();
        toggleRamp = flightStick.getButton(2);
        toggleRails = flightStick.getButton(3);

        writeData = flightStick.getButton(6);
    }

    public OperatorInterface(Gamepad gamepad) {
        throttle = gamepad.getLeftY();
        wheel = gamepad.getRightX();
        quickTurn = gamepad.getRightBumper();

        driveSpeed = gamepad.getLeftY();
        turnSpeed = gamepad.getLeftX();

        toggleClaw = gamepad.getA();
        toggleLift = gamepad.getX();
        toggleRamp = gamepad.getY();
        toggleRails = gamepad.getB();

        writeData = gamepad.getStart();
    }
}
