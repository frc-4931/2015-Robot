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
public class Drive extends Command {
    private final double speed;
    private final DriveInterpreter drive;
    
    public Drive(DriveInterpreter drive, double speed, double time) {
        super(time, drive);
        this.drive = drive;
        this.speed = speed;
    }

    @Override
    public boolean execute() {
        drive.arcade(speed, 0);
        return false;
    }

    @Override
    public void end() {
        drive.stop();
    }

}
