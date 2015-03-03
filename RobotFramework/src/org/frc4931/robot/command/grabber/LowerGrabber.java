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
 * Lowers the grabber arm to tote/ground level.
 * @see org.frc4931.robot.subsystem.LoaderArm
 */
@Deprecated
public class LowerGrabber extends SpeedCommandBase {
    private LoaderArm loaderArm;

    public LowerGrabber(LoaderArm loaderArm, double speed ) {
        super(speed);
        this.loaderArm = loaderArm;
        requires(loaderArm);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        loaderArm.lower(speed);
    }
    
    @Override
    protected boolean isFinished() {
        return loaderArm.isLowered();
    }

    @Override
    protected void end() {
    }

}
