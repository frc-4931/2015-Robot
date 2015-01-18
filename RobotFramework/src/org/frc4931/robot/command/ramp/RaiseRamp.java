/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.ramp;

import org.frc4931.robot.command.OneShotCommand;
import org.frc4931.robot.subsystem.Ramp;

/**
 * Raises the ramp so it is perpendicular to the ground.
 * @see org.frc4931.robot.subsystem.Ramp
 */
public class RaiseRamp extends OneShotCommand {

    private final Ramp ramp;
    
    public RaiseRamp(Ramp ramp){
        this.ramp = ramp;
        requires(this.ramp);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        ramp.raiseRamp();
    }

    @Override
    protected void end() {
    }

}
