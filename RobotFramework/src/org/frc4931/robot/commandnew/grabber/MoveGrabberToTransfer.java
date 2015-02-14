/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.grabber;

import org.frc4931.robot.commandnew.Command;
import org.frc4931.robot.system.Grabber;
import org.frc4931.robot.system.Grabber.Position;

/**
 * 
 */
public class MoveGrabberToTransfer extends Command {
    private final Grabber grabber;
    
    public MoveGrabberToTransfer(Grabber grabber) {
        requires(grabber);
        this.grabber = grabber;
    }
    
    @Override
    public boolean firstExecute() {
        grabber.set(Position.TRANSFER);
        return grabber.is(Position.TRANSFER);
    }
    
    @Override
    public boolean execute() {
        return grabber.is(Position.TRANSFER);
    }

}
