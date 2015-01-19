/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.subsystem.RampLifter;

/**
 * Perpenticualrizes the ramp to the gorund.
 */
public class RaiseRamp extends OneShotCommand {

    private final RampLifter lifter;
    
    public RaiseRamp(RampLifter lifter){
        this.lifter = lifter;
        requires(this.lifter);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        lifter.raise();
    }

    @Override
    protected void end() {
    }

}
