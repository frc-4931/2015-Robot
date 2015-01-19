/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.subsystem;

import org.frc4931.robot.component.Solenoid;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The {@link Subsystem} that raises the ramp to the vertical position.
 */
public class RampLifter extends Subsystem{
    private final Solenoid lifter;

    public RampLifter(Solenoid lifter) {
        this.lifter = lifter;
    }

    /**
     * Raises the ramp using the lifting mechanism.
     */
    public void raise() {
        lifter.extend();
    }

    /**
     * Lowers the ramp using the lifting mechanism.
     */
    public void lower() {
        lifter.retract();
    }

    @Override
    protected void initDefaultCommand() {}
}
