/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.group;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.grabber.LowerGrabber;
import org.frc4931.robot.command.grabber.OpenGrabber;
import org.frc4931.robot.command.grabber.RaiseGrabber;
import org.frc4931.robot.command.ramp.AdvanceTotes;
import org.frc4931.robot.command.ramp.CloseGuardrail;
import org.frc4931.robot.command.ramp.LowerKicker;
import org.frc4931.robot.command.ramp.OpenGuardrail;

/**
 * 
 */
public class PickUpTote extends CommandGroup {
    public PickUpTote(Systems systems){
        addSequential(new LowerKicker(systems.ramp,0.4));
        addSequential(new RaiseGrabber(systems.grabber,0.4));
        addSequential(new OpenGuardrail(systems.ramp));
        addSequential(new AdvanceTotes(systems.ramp,0.6));
        addSequential(new CloseGuardrail(systems.ramp));
        addSequential(new LowerGrabber(systems.grabber, 0.4));
        addSequential(new OpenGrabber(systems.grabber));
    }

}


