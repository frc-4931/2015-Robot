/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.commandnew.Command;
import org.frc4931.robot.system.DriveInterpreter;

/**
 * 
 */
public class Turn extends Command{
    private final DriveInterpreter drive;
    private final double speed;
    
    public Turn(DriveInterpreter drive, double speed, double time) {
        super(time, drive);
        this.speed = speed;
        this.drive = drive;
    }
    
    @Override
    public boolean execute() {
        drive.arcade(0, speed);
        return false;
    }
}
