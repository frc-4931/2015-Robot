/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.group;

import org.frc4931.robot.command.CloseGrabber;
import org.frc4931.robot.command.CloseGuardrail;
import org.frc4931.robot.command.RaiseStacker;
import org.frc4931.robot.command.LowerStacker;
import org.frc4931.robot.command.OpenGrabber;
import org.frc4931.robot.command.OpenGuardrail;
import org.frc4931.robot.command.RaiseGrabber;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * 
 */
public class PickUpTote extends CommandGroup {
    public PickUpTote(){
        addSequential(new CloseGrabber());
        addSequential(new RaiseGrabber());
        addSequential(new OpenGrabber());
        addSequential(new OpenGuardrail());
        addSequential(new RaiseStacker());
        addSequential(new CloseGuardrail());
        addSequential(new LowerStacker());
    }

}


