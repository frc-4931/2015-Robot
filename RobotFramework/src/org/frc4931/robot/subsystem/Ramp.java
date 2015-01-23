/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */

/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */

package org.frc4931.robot.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;


/**
 * An aggregate of all of the {@link Subsystem}s that make up the physical ramp system.
 */
public final class Ramp extends SubsystemBase {
    public final Guardrail guardrail;
    public final RampLifter rampLift;

    /**
     * Creates a new {@link RampLifter} using a {@link Guardrail} and a {@link RampLifter}.
     *
     * @param guardrail The {@link Guardrail} for this {@link Ramp}
     * @param rampLift The {@link RampLifter} for this {@link Ramp}
     */
    public Ramp(Guardrail guardrail, RampLifter rampLift) {
        this.guardrail = guardrail;
        this.rampLift = rampLift;
    }
}
