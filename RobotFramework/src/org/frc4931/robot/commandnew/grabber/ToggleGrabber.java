/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.grabber;

import org.frc4931.robot.commandnew.CommandGroup;
import org.frc4931.robot.system.Grabber;

/**
 * 
 */
public class ToggleGrabber extends CommandGroup {
    public ToggleGrabber(Grabber grabber) {
        if(grabber.isOpen()) {
            sequentially(new CloseGrabber(grabber));
        } else {
            sequentially(new OpenGrabber(grabber));
        }
    }
}
