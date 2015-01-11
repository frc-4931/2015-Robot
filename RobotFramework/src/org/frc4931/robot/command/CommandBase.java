/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command helper class that automatically overrides {@link #interrupted()} to call {@link #end()}.
 */
public abstract class CommandBase extends Command{
    @Override
    protected void interrupted() {
        end();
    }

}
