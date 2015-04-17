/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.group;

import org.frc4931.robot.RobotManager.Systems;
import org.frc4931.robot.command.guardrail.CloseGuardrail;
import org.frc4931.robot.command.guardrail.OpenGuardrail;
import org.frc4931.robot.command.ramplifter.LowerRamp;
import org.frc4931.robot.command.ramplifter.RaiseRamp;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Ejects the tote stack at ground level.
 */
@Deprecated //TODO Re-do tote ejection
public class PutDownTote extends CommandGroup {
    public PutDownTote(Systems systems){
        addSequential(new RaiseRamp(systems.ramp.rampLift));
        addSequential(new OpenGuardrail(systems.ramp.guardrail));
        addSequential(new LowerRamp(systems.ramp.rampLift));
        addSequential(new CloseGuardrail(systems.ramp.guardrail));
    }

}
