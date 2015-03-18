/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import java.util.function.Supplier;

import org.frc4931.robot.commandnew.CommandGroup;
import org.frc4931.robot.commandnew.grabber.CloseGrabber;
import org.frc4931.robot.commandnew.grabber.MoveGrabberToGround;
import org.frc4931.robot.commandnew.grabber.MoveGrabberToStep;
import org.frc4931.robot.commandnew.guardrail.CloseGuardrail;
import org.frc4931.robot.commandnew.ramplifter.LowerRamp;
import org.frc4931.robot.component.Counter;
import org.frc4931.robot.system.DriveInterpreter;
import org.frc4931.robot.system.Superstructure;

import com.ni.vision.NIVision.Image;

public class AutoMode extends CommandGroup{
    public AutoMode(DriveInterpreter drive, Superstructure structure, Supplier<Image> eyes, Counter totes) {
        totes.increase();
        sequentially(
                     // Grab can in guardrails
                     new Drive(drive, 0.6, 0.65),
                     new CloseGuardrail(structure.ramp.rail),
                     new Pause(0.4),
                     new LowerRamp(structure.ramp.lifter),
                     new Pause(1.5),
                     // Put kicker under can
//                     new MoveKickerToTransfer(structure.kickerSystem.kicker),
                     
                     // Transfer from guardrail to kicker
//                     fork(sequentially(
//                         new OpenGuardrail(structure.ramp.rail),
//                         new Pause(1),
//                         new MoveKickerToGuardrail(structure.kickerSystem.kicker),
//                         new CloseGuardrail(structure.ramp.rail))
//                     ),
//                     // Reset kicker and turn towards yellow tote
                     new Drive(drive, -0.6, 0.95),
                     
                     // Turn TO Tote
                     new Turn(drive, -1, 0.26),
                     new Drive(drive, 0.8, 0.85),
//
//                     // Grab yellow tote with grabbers
                     new CloseGrabber(structure.grabber),
                     new Pause(0.25),
                     fork(new MoveGrabberToStep(structure.grabber)),
//                     new MoveGrabberToStep(structure.grabber),
                     new Turn(drive, 1, 0.425),
                     new Drive(drive, -0.7, 2.5),
                     new Turn(drive, 1, 1.75),
                     new Drive(drive, 0,0.1),
                     new MoveGrabberToGround(structure.grabber)
//                     new OpenGuardrail(structure.ramp.rail),
//                     new RaiseRamp(structure.ramp.lifter)
//                     new Drive(drive, -0.75, 3)
                     );
    }
}
