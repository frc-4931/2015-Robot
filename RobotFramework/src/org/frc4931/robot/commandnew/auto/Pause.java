/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.commandnew.Command;

/**
 * 
 */
public class Pause extends Command {
    public Pause(double time) {
        super(time);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
