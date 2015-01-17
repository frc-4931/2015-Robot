/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.ramp;

import org.frc4931.robot.command.SpeedCommandBase;
import org.frc4931.robot.subsystem.Ramp;

/**
 * Raise the kicker to advance the totes.
 */
public class AdvanceTotes extends SpeedCommandBase {

    private final Ramp ramp;
    
    public AdvanceTotes(Ramp ramp, double speed ) {
        super(speed);
        this.ramp = ramp;
        requires(this.ramp);
    }
    
    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        ramp.raiseKicker(speed);
    }
    
    @Override
    protected boolean isFinished() {
        return ramp.isKickerRaised();
    }

    @Override
    protected void end() {
    }

}
