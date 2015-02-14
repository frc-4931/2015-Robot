/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.guardrail;

import org.frc4931.robot.commandnew.Command;
import org.frc4931.robot.system.Guardrail;

/**
 * 
 */
public class CloseGuardrail extends Command {
    private final Guardrail rail;
    
    public CloseGuardrail(Guardrail rail) {
        requires(rail);
        this.rail = rail;
    }
    
    @Override
    public boolean execute() {
        rail.close();
        return rail.isClosed();
    }
}
