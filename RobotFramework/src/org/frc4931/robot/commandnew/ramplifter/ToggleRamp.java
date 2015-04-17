/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.ramplifter;

import org.frc4931.robot.commandnew.CommandGroup;
import org.frc4931.robot.system.Lifter;

/**
 * 
 */
public class ToggleRamp extends CommandGroup {
    public ToggleRamp(Lifter lifter) {
        if(lifter.isUp()) {
            System.out.println("RAMP LOWERED");
            sequentially(new LowerRamp(lifter));
        } else {
            System.out.println("RAMP RAISED");
            sequentially(new RaiseRamp(lifter));
        }
    }
}
