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
    MotorWithAngle motor;
    Solenoid grabber;
    
    public CompositeGrabber(MotorWithAngle motor, Solenoid grabber) {
        this.motor = motor;
        this.grabber = grabber;
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
        grabber.extend();
    }

    @Override
    public boolean isClosed() {
        return grabber.isExtending();
    }

    @Override
    public void open() {
        grabber.retract();
    }

    @Override
    public boolean isOpen() {
        return grabber.isRetracting();
    }
    
    @Override
    public void home() {
        motor.home(0.3 * 1);
    }

}
