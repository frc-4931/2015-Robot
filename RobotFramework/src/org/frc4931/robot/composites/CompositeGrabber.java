/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.composites;

import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.component.Solenoid;


/**
 * 
 */
public class CompositeGrabber implements org.frc4931.robot.system.Grabber {
    private final MotorWithAngle motor;
    private final Solenoid grabberL;
    private final Solenoid grabberR;
    
    public CompositeGrabber(MotorWithAngle motor, Solenoid grabberL, Solenoid grabberR) {
        this.motor = motor;
        this.grabberL = grabberL;
        this.grabberR = grabberR;
    }
    
    @Override
    public void set(Position pos) {
        motor.setAngle(pos.getAngle());
    }

    @Override
    public boolean is(Position pos) {
        return motor.isAt(pos.getAngle());
    }

    @Override
    public void close() {
        grabberL.extend();
        grabberR.extend();
    }

    @Override
    public boolean isClosed() {
        return grabberL.isExtending();
    }

    @Override
    public void open() {
        grabberL.retract();
        grabberR.retract();
    }

    @Override
    public boolean isOpen() {
        return grabberL.isRetracting();
    }
    
    @Override
    public void home() {
        motor.home(-0.1);
    }

}
