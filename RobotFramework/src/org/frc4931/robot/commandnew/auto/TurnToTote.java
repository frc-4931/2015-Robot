/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.commandnew.Command;
import org.frc4931.robot.system.DriveInterpreter;
import org.frc4931.robot.vision.Camera;


/**
 * 
 */
public class TurnToTote extends Command {
    private final DriveInterpreter drive;
    private final Camera eyes;
    
    public TurnToTote(DriveInterpreter drive, Camera eyes) {
        super(drive);
        this.drive = drive;
        this.eyes = eyes;
    }
    @Override
    public void initialize() {
    }

    // TODO Implement
    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public void end() {
    }

}
