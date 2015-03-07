/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.executor;

import java.util.function.IntSupplier;

import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Switch;

/**
 * 
 */
public interface Registry {

    /**
     * Registers the value of the specified {@link IntSupplier} to be logged. This method should be called before the
     * @param name the name of this data point; may not be null
     * @param supplier the {@link IntSupplier} of the value to be logged; may not be null
     */
    void register(String name, IntSupplier supplier);

    /**
     * Registers a {@link Motor} to be logged.
     * @param name the name of the {@link Motor}; may not be null
     * @param motor the {@link Motor} to be logged; may not be null
     */
    void registerMotor(String name, Motor motor);

    /**
     * Registers a {@link Switch} to be logged.
     * @param name the name of the {@link Switch}; may not be null
     * @param swtch the {@link Switch} to be logged; may not be null
     */
    void registerSwitch(String name, Switch swtch);
}
