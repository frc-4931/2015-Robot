/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.kicker;

import org.frc4931.robot.command.CommandBase;
import org.frc4931.robot.subsystem.Kicker;
import org.frc4931.robot.subsystem.Kicker.Position;

/**
 * 
 */
@Deprecated
public class MoveKickerToDown extends CommandBase {
    private final Kicker kicker;
 
    public MoveKickerToDown(Kicker kicker){
        this.kicker = kicker;
        requires(kicker);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void executeFirstTime() {
        kicker.set(Position.DOWN);
    }

    @Override
    protected boolean isFinished() {
        return kicker.is(Position.DOWN);
    }

    @Override
    protected void end() {
    }

}
