/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */

package org.frc4931.robot.subsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc4931.robot.component.LimitedMotor;
import org.frc4931.robot.component.Solenoid;

import java.util.function.Supplier;

/**
 * A subsystem used to control the ramp totes are stored on and ejected from.
 */
public final class Ramp extends Subsystem {
    public static final double RAIL_MOVE_SPEED = 0.0d;

    private final Solenoid lifter;
    private final LimitedMotor guardRail; // Debatable - solenoid vs motor?
    private final Supplier<Command> defaultCommandSupplier;

    /**
     * Creates a new Ramp subsystem using a Solenoid for the lifting mechanism and a LimitedMotor for the guardrails.
     *
     * @param lifter The lifting mechanism; may not be null.
     * @param guardRail The guard rail control mechanism; may not be null.
     * @param defaultCommandSupplier The supplier for this subsystem's default command; may be null if there is no default command.
     */
    public Ramp(Solenoid lifter, LimitedMotor guardRail, Supplier<Command> defaultCommandSupplier) {
        this.lifter = lifter;
        this.guardRail = guardRail;
        this.defaultCommandSupplier = defaultCommandSupplier;
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

    /**
     * Opens the ramp's guard rails, releasing the held objects.
     */
    public void openGuardRail() {
        guardRail.moveTowardsHigh(RAIL_MOVE_SPEED);
    }

    /**
     * Closes the ramp's guard rails, securing the held objects.
     */
    public void closeGuardRail() {
        guardRail.moveTowardsLow(RAIL_MOVE_SPEED);
    }

    @Override
    protected void initDefaultCommand() {
        if ( defaultCommandSupplier != null ) {
            Command command = defaultCommandSupplier.get();
            setDefaultCommand(command);
        }
    }
}
