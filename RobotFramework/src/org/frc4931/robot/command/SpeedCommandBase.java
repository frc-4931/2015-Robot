/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Abstract base class for command that require a speed.
 */
public abstract class SpeedCommandBase extends Command {
    
    protected final double speed;
    
    protected SpeedCommandBase( double speed ) {
        this.speed = speed;
    }

    @Override
    protected void interrupted() {
        end();
    }

}