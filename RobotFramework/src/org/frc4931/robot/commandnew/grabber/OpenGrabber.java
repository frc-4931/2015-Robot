/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.grabber;

import org.frc4931.robot.commandnew.Command;
import org.frc4931.robot.system.Grabber;

/**
 * 
 */
public class OpenGrabber extends Command {
    private final Grabber grabber;
    
    public OpenGrabber(Grabber grabber) {
        super(grabber);
        this.grabber = grabber;
    }
    
    @Override
    public boolean execute() {
        grabber.open();
        return grabber.isOpen();
    }

}
