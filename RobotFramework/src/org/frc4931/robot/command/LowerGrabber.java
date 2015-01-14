/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.subsystem.LoaderArm;

/**
 * Lowers the grabber arm to tote/ground level.
 */
public class LowerGrabber extends OneShotCommand {
    private LoaderArm loaderArm;

    public LowerGrabber(LoaderArm loaderArm) {
        this.loaderArm = loaderArm;
        requires(loaderArm);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        loaderArm.lower();
    }

    @Override
    protected void end() {
    }

}
