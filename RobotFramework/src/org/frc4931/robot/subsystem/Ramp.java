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

import org.frc4931.robot.component.LeadScrew;
import org.frc4931.robot.component.LimitedMotor;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.utils.Operations;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A subsystem used to control the ramp totes are stored on and ejected from.
 */
public final class Ramp extends SubsystemBase {
    public static final double RAIL_MOVE_SPEED = 0.0d;

    private final Solenoid rampLifter;
    private final LeadScrew leadScrew;
    private final LimitedMotor kicker;
    private final LimitedMotor guardRail;

    /**
     * Creates a new Ramp subsystem using a Solenoid for the lifting mechanism and a LimitedMotor for the guardrails. Assumes no default command.
     *
     * @param rampLifter The lifting mechanism for the ramp; may not be null.
     * @param leadScrew The lead screw that raises the stack and guard rails; may not be null.
     * @param kicker the motor that pushes the stack of totes up the ramp; may not be null.
     * @param guardRail The guard rail control mechanism; may not be null.
     */
    public Ramp(Solenoid rampLifter, LeadScrew leadScrew, LimitedMotor kicker, LimitedMotor guardRail) {
        this(rampLifter, leadScrew, kicker, guardRail, null);
    }
    
    /**
     * Creates a new Ramp subsystem using a Solenoid for the lifting mechanism and a LimitedMotor for the guardrails.
     *
     * @param rampLifter The lifting mechanism for the ramp; may not be null.
     * @param leadScrew The lead screw that raises the stack and guard rails; may not be null.
     * @param kicker the motor that pushes the stack of totes up the ramp; may not be null.
     * @param guardRail The guard rail control mechanism; may not be null.
     * @param defaultCommandSupplier The supplier for this subsystem's default command; may be null if there is no default command.
     */
    public Ramp(Solenoid rampLifter, LeadScrew leadScrew, LimitedMotor kicker, LimitedMotor guardRail, Supplier<Command> defaultCommandSupplier) {
        super(defaultCommandSupplier);
        this.rampLifter = rampLifter;
        this.leadScrew = leadScrew;
        this.kicker = kicker;
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
     * Use the lead screw to move the guard rail and stack to the lowest position.
     * @param desiredPosition the desired position for the rail and stack; may not be null
     * @param speed the speed to move the guard rail and stack; truncated to be greater than 0 and less than or equal to 1.
     */
    public void moveStackTowards( LeadScrew.Position desiredPosition, double speed ) {
        speed = Operations.positiveLimit(MAXIMUM_VOLTAGE, speed, MINIMUM_USABLE_VOLTAGE);
        leadScrew.moveTo(desiredPosition,speed);
    }

    /**
     * Get the current position of the guard rail and stack.
     * @return the current position; never null
     */
    public LeadScrew.Position getStackPosition() {
        return leadScrew.getPosition();
    }

    /**
     * Move the kicker to the low position.
     * @param speed the speed to move the kicker; truncated to be greater than 0 and less than or equal to 1.
     */
    public void lowerKicker( double speed ) {
        speed = Operations.positiveLimit(MAXIMUM_VOLTAGE, speed, MINIMUM_USABLE_VOLTAGE);
        kicker.moveTowardsLow(speed);
    }

    /**
     * Move the kicker to the high position, advancing any totes on the ramp.
     * @param speed the speed to move the kicker; truncated to be greater than 0 and less than or equal to 1.
     */
    public void raiseKicker( double speed ) {
        speed = Operations.positiveLimit(MAXIMUM_VOLTAGE, speed, MINIMUM_USABLE_VOLTAGE);
        kicker.moveTowardsHigh(speed);
    }

    /**
     * Determine if the kicker is in the raised position.
     * @return true if the arms are in the raised position, or false otherwise.
     */
    public boolean isKickerRaised() {
        return kicker.isHigh();
    }
    
    /**
     * Determine if the kicker is in the lower position.
     * @return true if the arms are in the lower position, or false otherwise.
     */
    public boolean isKickerLowered() {
        return kicker.isLow();
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
    /**
     * @return Checks to see if Guardrail is open.
     */
    public boolean isGuardRailOpen(){
        return guardRail.isHigh();
    }
    /**
     *  Checks to see if Guardrail is closed
     * @return true if the guardrail is in the closed position, or false otherwise
     */
    public boolean isGuardRailClosed(){
        return guardRail.isLow();
    }
}
