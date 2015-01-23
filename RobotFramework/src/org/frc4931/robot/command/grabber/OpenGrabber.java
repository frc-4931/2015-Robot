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
 * Opens the grabber arm.
 * @see org.frc4931.robot.subsystem.LoaderArm
 */
public class OpenGrabber extends OneShotCommand {
    
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
    protected void end() {
    }

}
