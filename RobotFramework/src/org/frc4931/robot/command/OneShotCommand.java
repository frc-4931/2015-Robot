/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;


/**
 * Command helper class that automatically overrides {@link #isFinished()} to return {@code true}.
 * Override this class for commands that only execute once.
 */
public abstract class OneShotCommand extends CommandBase {
    protected OneShotCommand() {
    }

    protected OneShotCommand(String name) {
        super(name);
    }

    protected OneShotCommand(double timeout) {
        super(timeout);
    }

    protected OneShotCommand(String name, double timeout) {
        super(name, timeout);
    }

    @Override
    protected final boolean isFinished() {
        return true;
    }

}
