/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.subsystem.RampLifter;

/**
 * Lowers the ramp.
 */
public class LowerRamp extends OneShotCommand {
    private RampLifter lifter;

    public LowerRamp(RampLifter lifter) {
        this.lifter = lifter;
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        lifter.lower();
    }

    @Override
    protected void end() {
    }

}
