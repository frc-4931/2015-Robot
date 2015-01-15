/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.subsystem.LoaderArm;

/**
 * Raise the grabber arm to tote/ground level.
 */
public class RaiseGrabber extends OneShotCommand {

    private final LoaderArm arm;
    
    public RaiseGrabber(LoaderArm arm){
        this.arm = arm;
        requires(this.arm);
    }
    
    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        arm.raise();
    }

    @Override
    protected void end() {
    }

}
