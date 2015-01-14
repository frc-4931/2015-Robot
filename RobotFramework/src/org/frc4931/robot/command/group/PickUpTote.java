/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.group;

import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.CloseGrabber;
import org.frc4931.robot.command.CloseGuardrail;
import org.frc4931.robot.command.LowerStacker;
import org.frc4931.robot.command.OpenGrabber;
import org.frc4931.robot.command.OpenGuardrail;
import org.frc4931.robot.command.RaiseGrabber;
import org.frc4931.robot.command.RaiseStacker;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * 
 */
public class PickUpTote extends CommandGroup {
    public PickUpTote(Systems systems){
        addSequential(new CloseGrabber(systems.grabber));
        addSequential(new RaiseGrabber(systems.grabber));
        addSequential(new OpenGrabber(systems.grabber));
        addSequential(new OpenGuardrail(systems.ramp));
        addSequential(new RaiseStacker(systems.ramp));
        addSequential(new CloseGuardrail(systems.ramp));
        addSequential(new LowerStacker(systems.ramp));
    }

}


