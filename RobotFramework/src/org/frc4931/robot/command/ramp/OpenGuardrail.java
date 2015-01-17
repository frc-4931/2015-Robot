/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.ramp;

import org.frc4931.robot.command.CommandBase;
import org.frc4931.robot.subsystem.Ramp;

/**
 * Opens the guardrail.
 */
public class OpenGuardrail extends CommandBase {
    private Ramp ramp;
    public OpenGuardrail(Ramp ramp){
        this.ramp = ramp;
        requires(ramp);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return ramp.isGuardRailOpen();
    }

    @Override
    protected void end() {
    }

}
