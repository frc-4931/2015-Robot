/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.guardrail;

import org.frc4931.robot.commandnew.CommandGroup;
import org.frc4931.robot.system.Guardrail;

/**
 * 
 */
public class ToggleGuardrail extends CommandGroup {
    public ToggleGuardrail(Guardrail rail) {
        if(rail.isOpen()) {
            sequentially(new CloseGuardrail(rail));
        } else {
            sequentially(new OpenGuardrail(rail));
        }
    }
}
