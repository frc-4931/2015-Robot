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

import java.util.function.Supplier;

import org.frc4931.robot.component.LimitedMotor;
import org.frc4931.robot.component.Solenoid;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A subsystem used to control the ramp totes are stored on and ejected from.
 */
public final class Ramp extends SubsystemBase {
    public static final double RAIL_MOVE_SPEED = 0.0d;

    private final Solenoid rampLifter;
    private final Solenoid stackLifter;
    private final Solenoid stackPusher;
    private final LimitedMotor guardRail; // Debatable - solenoid vs motor?

    /**
     * Creates a new Ramp subsystem using a Solenoid for the lifting mechanism and a LimitedMotor for the guardrails. Assumes no default command.
     *
     * @param rampLifter The lifting mechanism for the ramp; may not be null.
     * @param stackLifter The lifting mechanism for the stack; may not be null.
     * @param stackPusher the solenoid that pushes the stack off of the ramp; may not be null.
     * @param guardRail The guard rail control mechanism; may not be null.
     */
    public Ramp(Solenoid rampLifter, Solenoid stackLifter, Solenoid stackPusher, LimitedMotor guardRail) {
        this(rampLifter, stackLifter, stackPusher, guardRail, null);
    }
    
    /**
     * Creates a new Ramp subsystem using a Solenoid for the lifting mechanism and a LimitedMotor for the guardrails.
     *
     * @param rampLifter The lifting mechanism for the ramp; may not be null.
     * @param stackLifter The lifting mechanism for the stack; may not be null.
     * @param stackPusher the solenoid that pushes the stack off of the ramp; may not be null.
     * @param guardRail The guard rail control mechanism; may not be null.
     * @param defaultCommandSupplier The supplier for this subsystem's default command; may be null if there is no default command.
     */
    public Ramp(Solenoid rampLifter, Solenoid stackLifter, Solenoid stackPusher, LimitedMotor guardRail, Supplier<Command> defaultCommandSupplier) {
        super(defaultCommandSupplier);
        this.rampLifter = rampLifter;
        this.stackLifter = stackLifter;
        this.stackPusher = stackPusher;
        this.guardRail = guardRail;
    }

    /**
     * Raises the ramp using the lifting mechanism.
     */
    public void raiseRamp() {
        rampLifter.extend();
    }

    /**
     * Lowers the ramp using the lifting mechanism.
     */
    public void lowerRamp() {
        rampLifter.retract();
    }

    /**
     * Use the stack lifter to raise the loaded stack.
     */
    public void raiseStack() {
        stackLifter.extend();
    }

    /**
     * Use the stack lifter to lower the loaded stack.
     */
    public void lowerStack() {
        stackLifter.retract();
    }

    /**
     * Extends the solenoid that pushes the totes off the ramp.
     */
    public void extendPusher() {
        stackPusher.extend();
    }
    
    /**
     * Retracts the solenoid that pushes the totes off the ramp.
     */
    public void retractPusher() {
        stackPusher.retract();
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
}
