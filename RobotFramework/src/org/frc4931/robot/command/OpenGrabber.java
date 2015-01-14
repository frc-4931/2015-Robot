/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.subsystem.LoaderArm;

/**
 * Opens the grabber arm.
 */
public class OpenGrabber extends CommandBase {
    
    private final LoaderArm arm;
    
    public OpenGrabber(LoaderArm arm){
        this.arm = arm;
        requires(this.arm);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        arm.grab();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }

}
