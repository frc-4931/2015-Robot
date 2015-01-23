/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

/**
 * Abstract base class for command that require a speed.
 */
public abstract class SpeedCommandBase extends CommandBase {
    protected final double speed;

    protected SpeedCommandBase( double speed ) {
        this.speed = speed;
    }

    protected SpeedCommandBase(String name, double speed) {
        super(name);
        this.speed = speed;
    }

    protected SpeedCommandBase(double timeout, double speed) {
        super(timeout);
        this.speed = speed;
    }

    protected SpeedCommandBase(String name, double timeout, double speed) {
        super(name, timeout);
        this.speed = speed;
    }
}
