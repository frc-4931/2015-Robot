/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.group;

import org.frc4931.robot.Robot.Systems;
import org.frc4931.robot.command.CloseGuardrail;
import org.frc4931.robot.command.LowerRamp;
import org.frc4931.robot.command.OpenGuardrail;
import org.frc4931.robot.command.RaiseRamp;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * 
 */
public class PutDownTote extends CommandGroup {
    public PutDownTote(Systems systems){
        addSequential(new LowerRamp(systems.ramp));
        addSequential(new OpenGuardrail(systems.ramp));
        //TODO move back two feet
        addSequential(new CloseGuardrail(systems.ramp));
        addSequential(new RaiseRamp(systems.ramp));
        
    }

}
