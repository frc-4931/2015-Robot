/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.RobotManager;
import org.frc4931.robot.commandnew.Command;

/**
 * 
 */
public class Pause extends Command {
    private final long t;
    private long endTime;
    
    public Pause(double time) {
        t = (long) (time*1000);
    }
    
    @Override
    public void initialize() {
        endTime = RobotManager.time() + t;
    }
    
    @Override
    public boolean execute() {
        return RobotManager.time() > endTime;
    }

}
