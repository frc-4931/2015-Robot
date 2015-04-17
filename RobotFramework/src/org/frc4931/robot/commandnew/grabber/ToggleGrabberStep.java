/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.grabber;

import org.frc4931.robot.commandnew.CommandGroup;
import org.frc4931.robot.system.Grabber;
import org.frc4931.robot.system.Grabber.Position;

/**
 * 
 */
public class ToggleGrabberStep extends CommandGroup {
    public ToggleGrabberStep(Grabber grabber) {
        if(!grabber.is(Position.STEP)) {
            sequentially(new MoveGrabberToStep(grabber));
        } else {
            sequentially(new MoveGrabberToGround(grabber));
        }
    }
}
