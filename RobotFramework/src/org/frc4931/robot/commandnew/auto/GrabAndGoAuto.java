/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.commandnew.CommandGroup;
import org.frc4931.robot.commandnew.grabber.MoveGrabberToStep;
import org.frc4931.robot.commandnew.guardrail.CloseGuardrail;
import org.frc4931.robot.commandnew.ramplifter.LowerRamp;
import org.frc4931.robot.system.DriveInterpreter;
import org.frc4931.robot.system.Superstructure;

/**
 * 
 */
public class GrabAndGoAuto extends CommandGroup{
    public GrabAndGoAuto(DriveInterpreter drive, Superstructure structure) {
        sequentially(
                     new Drive(drive, 0.6, 0.5),
                     new CloseGuardrail(structure.ramp.rail),
                     new MoveGrabberToStep(structure.grabber),
                     new LowerRamp(structure.ramp.lifter),
                     new Pause(1.5),
                     new Drive(drive, -0.6, 3),
                     new Turn(drive, 0.6, 1.75),
                     new Drive(drive, 0, 0.1));
    }

}
