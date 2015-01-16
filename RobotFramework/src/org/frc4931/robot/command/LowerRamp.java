/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.subsystem.Ramp;

/**
 * Lowers the ramp.
 */
public class LowerRamp extends OneShotCommand {
    private Ramp ramp;

    public LowerRamp(Ramp ramp) {
        this.ramp = ramp;
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        ramp.lowerRamp();
    }

    @Override
    protected void end() {
    }

}
