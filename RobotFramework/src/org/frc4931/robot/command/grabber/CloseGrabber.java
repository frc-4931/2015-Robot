/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.grabber;

import org.frc4931.robot.command.OneShotCommand;
import org.frc4931.robot.subsystem.LoaderArm;

/**
 * Closes the grabber arm.
 * @see org.frc4931.robot.subsystem.LoaderArm
 */
@Deprecated
public class CloseGrabber extends OneShotCommand {
    private LoaderArm loaderArm;

    public CloseGrabber(LoaderArm loaderArm) {
        this.loaderArm = loaderArm;
        requires(loaderArm);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        loaderArm.grab();
    }
    
    @Override
    protected void end() {
    }

}
