/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.kicker;

import org.frc4931.robot.commandnew.CommandGroup;
import org.frc4931.robot.system.Kicker;
import org.frc4931.robot.system.Kicker.Position;

/**
 * 
 */
public class ToggleKickerGuardrail extends CommandGroup{
    public ToggleKickerGuardrail(Kicker kicker) {
        if(!kicker.is(Position.GUARDRAIL)) {
            sequentially(new MoveKickerToGuardrail(kicker));
        } else {
            sequentially(new MoveKickerToGround(kicker));
        }
    }
}
