/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.commandnew.Scheduler.Requireable;

/**
 * 
 */
public interface Kicker extends Requireable {
    public void set(Position pos);
    
    public boolean is(Position pos);
    
    public void home();
    
    public static enum Position {
        DOWN(0),STEP(29.5),TOTE(65.1),TOTE_STEP(92.5), TRANSFER(40.0), GUARDRAIL(155.0);
        
        private final double angle;
        private Position(double angle){
            this.angle = angle;
        }
        public double getAngle(){
          return angle;
        }
    }
}
