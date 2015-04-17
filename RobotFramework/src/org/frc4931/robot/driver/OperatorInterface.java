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
    public final Switch toggleRamp;
    public final Switch toggleRails;
    
    public final Switch kickerToTransfer;
    public final Switch kickerToGuardrail;
    
    public final Switch transferTote;

    public final Switch writeData;
    public final Switch quickTurn;
    
    public final Switch increaseCounter;
    public final Switch resetCounter;
    
    public final Switch grabberToStep;
    public final Switch grabberToTransfer;

    public OperatorInterface(FlightStick flightStick, LogitechAttack3 codriver) {
        throttle = flightStick.getThrottle();
        wheel = flightStick.getYaw();
        quickTurn = flightStick.getButton(5);

        driveSpeed = flightStick.getPitch();
        turnSpeed = flightStick.getYaw();

        toggleClaw = flightStick.getTrigger();
        toggleRails = flightStick.getButton(4);
        
        toggleRamp = codriver.getButton(1);
        
        transferTote = flightStick.getThumb();
        writeData = flightStick.getButton(6);
        
        resetCounter = codriver.getButton(2);
        increaseCounter = codriver.getButton(3);
        
        grabberToStep = codriver.getButton(7);
        grabberToTransfer = codriver.getButton(6);
        
        kickerToTransfer = codriver.getButton(10);
        kickerToGuardrail = codriver.getButton(11);
    }
}
