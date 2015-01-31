/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.subsystem;

import org.frc4931.robot.component.MotorWithAngle;

/**
 * 
 */
public class Kicker extends SubsystemBase {
    private final MotorWithAngle motor;
    
    public Kicker(MotorWithAngle motor){
        this.motor = motor;
    }
    
    public void set(Position pos){
        motor.setAngle(pos.getAngle());
    }
    
    public boolean is(Position pos){
        return motor.isAt(pos.getAngle());
    }
    
    public static enum Position{
        DOWN(0),STEP(15),TOTE(30),TOTE_STEP(45);
        
        private final double angle;
        private Position(double angle){
            this.angle = angle;
        }
        public double getAngle(){
          return angle;
        }
    }
    
}
