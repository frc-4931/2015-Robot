/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.group;

import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.CloseGuardrail;
import org.frc4931.robot.command.LowerGrabber;
import org.frc4931.robot.command.OpenGrabber;
import org.frc4931.robot.command.OpenGuardrail;
import org.frc4931.robot.command.RaiseGrabber;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * 
 */
public class PickUpTote extends CommandGroup {
    public PickUpTote(Systems systems){
        addSequential(new RaiseGrabber(systems.grabber,0.4));
        addSequential(new OpenGuardrail(systems.ramp.guardrail));
        addSequential(new CloseGuardrail(systems.ramp.guardrail));
        addSequential(new LowerGrabber(systems.grabber, 0.4));
        addSequential(new OpenGrabber(systems.grabber));
    }

}


