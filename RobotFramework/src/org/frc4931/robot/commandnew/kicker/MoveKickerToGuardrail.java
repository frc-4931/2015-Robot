/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.kicker;

import org.frc4931.robot.commandnew.Command;
import org.frc4931.robot.system.Kicker;
import org.frc4931.robot.system.Kicker.Position;

/**
 * 
 */
public class MoveKickerToGuardrail extends Command {
    private final Kicker kicker;

    public MoveKickerToGuardrail(Kicker kicker) {
        requires(kicker);
        this.kicker = kicker;
    }
    
    @Override
    public boolean execute() {
        kicker.set(Position.GUARDRAIL);
        return kicker.is(Position.GUARDRAIL);
    }
}
