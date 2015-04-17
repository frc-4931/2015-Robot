/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew;

import org.frc4931.robot.commandnew.Scheduler.Requireable;

/**
 * Cancels all of the commands using the specified {@link Requireable}s
 */
public class Cancel extends Command{
    public Cancel(Requireable... toCancel) {
        super(toCancel);
    }
    
    @Override
    public boolean execute() {
        return true;
    }
    
}
