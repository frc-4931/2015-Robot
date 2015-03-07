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

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * 
 */
public class TransferTote extends CommandGroup {
    
    public TransferTote(Superstructure structure) {
        SmartDashboard.putNumber("toteCount", (int)(SmartDashboard.getNumber("toteCount")+1));
        sequentially(
                    new MoveGrabberToTransfer(structure.grabber),
                    new MoveKickerToTransfer(structure.kickerSystem.kicker),
                    new OpenGuardrail(structure.ramp.rail),
                    new Pause(1.5),
                    simultaneously(
                        new OpenGrabber(structure.grabber),
                        new MoveKickerToGuardrail(structure.kickerSystem.kicker)),
                    new CloseGuardrail(structure.ramp.rail),
                    new Pause(1.5),
                    simultaneously(
                        new MoveGrabberToGround(structure.grabber),
                        new MoveKickerToGround(structure.kickerSystem.kicker)));
    }

}
