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

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.function.Supplier;

/**
 * A base subsystem that implements commonly-used features.
 */
public abstract class SubsystemBase extends Subsystem {
    private final Supplier<Command> defaultCommandSupplier;

    public SubsystemBase(Supplier<Command> defaultCommandSupplier) {
        this.defaultCommandSupplier = defaultCommandSupplier;
    }

    @Override
    protected void initDefaultCommand() {
        if (defaultCommandSupplier != null) {
            Command command = defaultCommandSupplier.get();
            assert command != null;
            setDefaultCommand(command);
        }
    }
}
