/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

/**
 * Base command that finishes itself when it times out.
 */
public abstract class TimedCommand extends CommandBase {
    protected TimedCommand(double timeout) {
        super(timeout);
    }

    protected TimedCommand(String name, double timeout) {
        super(name, timeout);
    }

    @Override
    protected final boolean isFinished() {
        return isTimedOut();
    }
}
