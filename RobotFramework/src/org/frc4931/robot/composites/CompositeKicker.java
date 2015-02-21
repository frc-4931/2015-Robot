/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.composites;

import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.system.Kicker;

/**
 * 
 */
public class CompositeKicker implements Kicker{
    private final MotorWithAngle motor;
    
    public CompositeKicker(MotorWithAngle motor){
        this.motor = motor;
    }
    
    @Override
    public void set(Position pos){
        motor.setAngle(pos.getAngle());
    }
    
    @Override
    public boolean is(Position pos){
        return motor.isAt(pos.getAngle());
    }
    
    @Override
    public void home() {
        // Direction * speed
        motor.home(-1 * 0.25);
    }
}
