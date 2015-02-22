/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.commandnew.CommandGroup;
import org.frc4931.robot.commandnew.grabber.MoveGrabberToGround;
import org.frc4931.robot.commandnew.grabber.MoveGrabberToTransfer;
import org.frc4931.robot.commandnew.grabber.OpenGrabber;
import org.frc4931.robot.commandnew.guardrail.CloseGuardrail;
import org.frc4931.robot.commandnew.guardrail.OpenGuardrail;
import org.frc4931.robot.commandnew.kicker.MoveKickerToGround;
import org.frc4931.robot.commandnew.kicker.MoveKickerToGuardrail;
import org.frc4931.robot.commandnew.kicker.MoveKickerToTransfer;
import org.frc4931.robot.system.Superstructure;


/**
 * 
 */
public class TransferTote extends CommandGroup {
    
    public TransferTote(Superstructure structure) {
        sequentially(
                     simultaneously(
                                    new MoveGrabberToTransfer(structure.grabber),
                                    new MoveKickerToTransfer(structure.kickerSystem.kicker)),
                     simultaneously(
                                    new OpenGuardrail(structure.ramp.rail),
                                    new OpenGrabber(structure.grabber)),
                     simultaneously(
                                    new MoveKickerToGuardrail(structure.kickerSystem.kicker),
                                    new MoveGrabberToGround(structure.grabber)),
                     new CloseGuardrail(structure.ramp.rail),
                     new MoveKickerToGround(structure.kickerSystem.kicker));
    }

}
