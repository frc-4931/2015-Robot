/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.grabber;

import org.frc4931.robot.command.SpeedCommandBase;
import org.frc4931.robot.subsystem.LoaderArm;

/**
 * Raise the grabber arm to tote/ground level.
 * @see org.frc4931.robot.subsystem.LoaderArm
 */
public class RaiseGrabber extends SpeedCommandBase {

    private final LoaderArm arm;
    
    public RaiseGrabber(LoaderArm arm, double speed ) {
        super(speed);
        this.arm = arm;
        requires(this.arm);
    }
    
    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        arm.raise(speed);
    }
    
    @Override
    protected boolean isFinished() {
        return arm.isRaised();
    }

    @Override
    protected void end() {
    }

}
