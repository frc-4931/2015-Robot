/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.subsystem.Ramp;

/**
 * Lowers the whole stack of totes down the ramp.
 */
public class LowerStacker extends OneShotCommand {
    private Ramp ramp;

    public LowerStacker(Ramp ramp) {
        this.ramp = ramp;
        requires(ramp);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        ramp.lowerStack();
    }

    @Override
    protected void end() {
    }

}
