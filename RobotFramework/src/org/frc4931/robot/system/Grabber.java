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
public interface Grabber extends Requireable{
    /**
     * Moves this {@link Grabber} to the specified {@link Position}.
     * @param pos the target {@link Position} of this {@link Grabber}
     */
    public void set(Position pos);
    
    /**
     * Tests if this {@link Grabber} is at the specified {@link Position}.
     * @param pos the {@link Position} to test if this {@link Grabber} is at
     * @return {@code true} if this {@link Grabber} is at the specified position;
     * {@code false} otherwise
     */
    public boolean is(Position pos);

    /**
     * Retracts the arms toward the center, grabbing anything inside of them.
     */
    public void close();
    
    /**
     * Tests if the grabbers are closed.
     * @return {@code true} if the grabbers are closed; {@code false} otherwise
     */
    public boolean isClosed();

    /**
     * Extends the arms from the center, releasing anything inside of them.
     */
    public void open();
    
    /**
     * Tests if the grabbers are open.
     * @return {@code true} if the grabbers are open; {@code false} otherwise
     */
    public boolean isOpen();
    
    public void home();

    public static enum Position {
        DOWN(0),TRANSFER(45);
        
        private final double angle;
        private Position(double angle){
            this.angle = angle;
        }
        public double getAngle(){
          return angle;
        }
    }
}