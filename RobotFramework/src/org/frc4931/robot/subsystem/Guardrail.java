/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.subsystem;

import org.frc4931.robot.component.LimitedMotor;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The {@link Subsystem} that controls the ramp guardrails.
 */
public class Guardrail extends Subsystem{
    public static final double RAIL_MOVE_SPEED = 0.0d;
    private final LimitedMotor guardRail;
    
    public Guardrail(LimitedMotor guardRail){
        this.guardRail = guardRail;
    }
    
    /**
     * Opens the ramp's guard rails, releasing the held objects.
     */
    public void open() {
        guardRail.moveTowardsHigh(RAIL_MOVE_SPEED);
    }

    /**
     * Closes the ramp's guard rails, securing the held objects.
     */
    public void close() {
        guardRail.moveTowardsLow(RAIL_MOVE_SPEED);
    }
    /**
     * @return Checks to see if Guardrail is open.
     */
    public boolean isOpen(){
        return guardRail.isHigh();
    }
    /**
     *  Checks to see if Guardrail is closed
     * @return true if the guardrail is in the closed position, or false otherwise
     */
    public boolean isClosed(){
        return guardRail.isLow();
    }
    
    @Override
    protected void initDefaultCommand() {}

}
