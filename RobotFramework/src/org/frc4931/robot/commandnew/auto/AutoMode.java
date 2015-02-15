/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.commandnew.CommandGroup;
import org.frc4931.robot.commandnew.grabber.CloseGrabber;
import org.frc4931.robot.commandnew.guardrail.CloseGuardrail;
import org.frc4931.robot.commandnew.guardrail.OpenGuardrail;
import org.frc4931.robot.commandnew.kicker.MoveKickerToGround;
import org.frc4931.robot.commandnew.kicker.MoveKickerToStep;
import org.frc4931.robot.commandnew.kicker.MoveKickerToTote;
import org.frc4931.robot.commandnew.ramplifter.LowerRamp;
import org.frc4931.robot.system.Drive;
import org.frc4931.robot.system.Superstructure;
import org.frc4931.robot.vision.Camera;

public class AutoMode extends CommandGroup{
    public AutoMode(Drive drive, Superstructure structure, Camera eyes) {
        sequentially(
                     // Grab can in guardrails
                     new DriveUpToCan(drive, structure.kickerSystem.canCapture),
                     new CloseGuardrail(structure.ramp.rail),
                     new LowerRamp(structure.ramp.lifter),
                     
                     // Put kicker under can
                     new MoveKickerToStep(structure.kickerSystem.kicker),
                     
                     // Transfer from guardrail to kicker
                     simultaneously(new OpenGuardrail(structure.ramp.rail),
                                    new MoveKickerToTote(structure.kickerSystem.kicker)),
                     new CloseGuardrail(structure.ramp.rail),
                     
                     // Reset kicker and turn towards yellow tote
                     simultaneously(new MoveKickerToGround(structure.kickerSystem.kicker),
                                    new TurnToTote(drive, eyes)),
                                    
                     // Grab yellow tote with grabbers
                     new DriveUpToCan(drive, structure.kickerSystem.canCapture),
                     new CloseGrabber(structure.grabber));
    }
}
