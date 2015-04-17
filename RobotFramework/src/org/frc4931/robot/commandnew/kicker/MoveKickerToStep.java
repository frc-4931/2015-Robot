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
public class MoveKickerToStep extends Command {
    private final Kicker kicker;

    public MoveKickerToStep(Kicker kicker) {
        super(kicker);
        this.kicker = kicker;
    }
    
    @Override
    public boolean execute() {
        kicker.set(Position.STEP);
        return kicker.is(Position.STEP);
    }
    
}
