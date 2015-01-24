/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.guardrail;

import org.frc4931.robot.command.CommandBase;
import org.frc4931.robot.subsystem.Guardrail;

/**
 * Opens the guardrail.
 * @see org.frc4931.robot.subsystem.Ramp
 */
public class OpenGuardrail extends CommandBase {
    private Guardrail rail;
    public OpenGuardrail(Guardrail rail){
        this.rail = rail;
        requires(rail);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return rail.isOpen();
    }

    @Override
    protected void end() {
    }

}
