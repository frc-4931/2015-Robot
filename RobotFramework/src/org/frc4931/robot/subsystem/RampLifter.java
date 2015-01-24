/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.subsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc4931.robot.component.Solenoid;

import java.util.function.Supplier;

/**
 * The {@link Subsystem} that raises the ramp to the vertical position.
 */
public class RampLifter extends SubsystemBase{
    private final Solenoid lifter;

    /**
     * Constructs a new {@link RampLifter} with the specified {@link Solenoid} and no
     * default command.
     * 
     * @param lifter the {@link Solenoid} that actuates this {@link RampLifter}
     */
    public RampLifter(Solenoid lifter) {
        this(lifter, null);
    }
    
    /**
     * Constructs a new {@link RampLifter} with the specified {@link Solenoid}
     * and default command.
     * 
     * @param lifter the {@link Solenoid} that actuates this {@link RampLifter}
     * 
     * @param defaultCommandSupplier a {@link Supplier} that supplies the default {@link Command}
     *                                to be executed by this {@link RampLifter}
     */
    public RampLifter(Solenoid lifter, Supplier<Command> defaultCommandSupplier) {
        super(defaultCommandSupplier);
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
