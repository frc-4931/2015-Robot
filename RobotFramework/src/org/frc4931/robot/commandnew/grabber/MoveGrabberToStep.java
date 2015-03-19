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
public class MoveGrabberToStep extends Command{
private final Grabber grabber;
    
    public MoveGrabberToStep(Grabber grabber) {
        super(grabber);
        this.grabber = grabber;
    }

    @Override
    public boolean execute() {
        grabber.set(Position.STEP);
        return grabber.is(Position.STEP);
    }
}
