/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.subsystem.Guardrail;

/**
 * 
 */
public class Ramp {
    public final Lifter lifter;
    public final Guardrail rail;
    
    public Ramp(Lifter lifter, Guardrail rail) {
        this.lifter = lifter;
        this.rail = rail;
    }
}
