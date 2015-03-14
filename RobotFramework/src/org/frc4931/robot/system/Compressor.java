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
public interface Compressor extends Requireable{
    public boolean isPressurized();
    
    public void activate();
    
    public void deactivate();
}
