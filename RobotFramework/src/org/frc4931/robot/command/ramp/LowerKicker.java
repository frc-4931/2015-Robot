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
 * Lower the kicker to the lowest/stowed position.
 */
public class LowerKicker extends SpeedCommandBase {

    private final Ramp ramp;
    
    public LowerKicker(Ramp ramp, double speed ) {
        super(speed);
        this.ramp = ramp;
        requires(this.ramp);
    }
    
    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        ramp.lowerKicker(speed);
    }
    
    @Override
    protected boolean isFinished() {
        return ramp.isKickerLowered();
    }

    @Override
    protected void end() {
    }

}
