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
import org.frc4931.robot.system.Robot;


/**
 * 
 */
public class TransferTote extends CommandGroup {
    
    public TransferTote(Robot robot) {
        robot.toteCounter.increase();
        sequentially(
                    new MoveGrabberToTransfer(robot.structure.grabber),
                    new MoveKickerToTransfer(robot.structure.kickerSystem.kicker),
                    new OpenGuardrail(robot.structure.ramp.rail),
                    new Pause(0.5),
                    simultaneously(
                        new OpenGrabber(robot.structure.grabber),
                        new MoveKickerToGuardrail(robot.structure.kickerSystem.kicker)),
                    new CloseGuardrail(robot.structure.ramp.rail),
                    new Pause(1.0),
                    simultaneously(
                        new MoveGrabberToGround(robot.structure.grabber),
                        new MoveKickerToGround(robot.structure.kickerSystem.kicker)));
    }

}
