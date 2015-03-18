/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.commandnew.Command;
import org.frc4931.robot.component.Switch;
import org.frc4931.robot.system.DriveInterpreter;

/**
 * 
 */
public class DriveUpToCan extends Command {
    private final static double SPEED = 0.5;
    private final DriveInterpreter drive;
    private final Switch swtch;
    
    public DriveUpToCan(DriveInterpreter drive, Switch swtch) {
        super(drive);
        this.drive = drive;
        this.swtch = swtch;
    }

    @Override
    public boolean execute() {
        drive.arcade(SPEED, 0);
        return swtch.isTriggered();
    }

    @Override
    public void end() {
        drive.stop();
    }

}
