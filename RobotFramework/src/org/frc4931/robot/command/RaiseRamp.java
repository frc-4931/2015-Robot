/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.subsystem.Ramp;

/**
 * Perpenticualrizes the ramp to the gorund.
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
