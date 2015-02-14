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
public interface Lifter extends Requireable{

    /**
     * Raises the ramp using the lifting mechanism.
     */
    public void raise();
    
    /**
     * Tests if the ramp is fully up.
     * @return {@code true} if the ramp is up; {@code false} otherwise
     */
    public boolean isUp();

    /**
     * Lowers the ramp using the lifting mechanism.
     */
    public void lower();
    
    /**
     * Tests if the ramp is fully down.
     * @return {@code true} if the ramp is down; {@code false} otherwise
     */
    public boolean isDown();

}