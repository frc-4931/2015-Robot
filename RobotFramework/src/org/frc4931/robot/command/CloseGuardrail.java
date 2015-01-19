/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.subsystem.Guardrail;

/**
 * Closes the guardrail.
 */
public class CloseGuardrail extends CommandBase {
    
    private Guardrail rail;
    public CloseGuardrail(Guardrail rail){
        this.rail = rail;
        requires(rail);
    }
    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        rail.close();
    }

    @Override
    protected boolean isFinished() {
        return rail.isClosed();
    }

    @Override
    protected void end() {
    }

}
