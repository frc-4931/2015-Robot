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
    }

    @Override
    public boolean is(Position pos) {
        return false;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void open() {
    }

    @Override
    public boolean isOpen() {
        return false;
    }

}
