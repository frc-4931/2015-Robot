/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.ramp;

import org.frc4931.robot.command.SpeedCommandBase;
import org.frc4931.robot.component.LeadScrew;
import org.frc4931.robot.subsystem.Ramp;

/**
 * Lowers the whole stack of totes down the ramp.
 */
public class MoveStack extends SpeedCommandBase {
    
    private final Ramp ramp;
    private final LeadScrew.Position desiredPosition;

    public MoveStack(Ramp ramp, double speed, LeadScrew.Position desiredPosition ) {
        super(speed);
        this.desiredPosition = desiredPosition;
        this.ramp = ramp;
        requires(ramp);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        ramp.moveStackTowards(desiredPosition,speed);
    }
    
    @Override
    protected boolean isFinished() {
        return ramp.getStackPosition() == LeadScrew.Position.LOW;
    }

    @Override
    protected void end() {
    }

}
