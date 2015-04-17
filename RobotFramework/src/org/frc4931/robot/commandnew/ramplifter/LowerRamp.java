/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.ramplifter;

import org.frc4931.robot.commandnew.Command;
import org.frc4931.robot.system.Lifter;

/**
 * 
 */
public class LowerRamp extends Command{
    private final Lifter lifter;

    public LowerRamp(Lifter lifter) {
        super(lifter);
        this.lifter = lifter;
    }
    
    @Override
    public boolean execute() {
        lifter.lower();
        return lifter.isDown();
    }
}
