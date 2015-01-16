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

import org.frc4931.utils.Lifecycle;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * A base subsystem that implements commonly-used features.
 */
public abstract class SubsystemBase extends Subsystem implements Lifecycle {
    private final Supplier<Command> defaultCommandSupplier;

    public SubsystemBase(){
        this(null);
    }
    
    public SubsystemBase(Supplier<Command> defaultCommandSupplier) {
        this.defaultCommandSupplier = defaultCommandSupplier;
    }

    /**
     * Prepare this subsystem for use. By default, this method does nothing.
     */
    @Override
    public void startup() {
    }

    /**
     * Stop this subsystem and release any claimed resources. By default, this method does nothing.
     */
    @Override
    public void shutdown() {
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
