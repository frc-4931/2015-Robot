/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.composites;

import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.system.Guardrail;

/**
 * 
 */
public class CompositeGuardrail implements Guardrail{
    private final Solenoid solenoid;
    
    public CompositeGuardrail(Solenoid solenoid) {
        this.solenoid = solenoid;
    }
    
    @Override
    public void open() {
        solenoid.retract();
    }

    @Override
    public boolean isOpen() {
        return solenoid.isRetracting();
    }

    @Override
    public void close() {
        solenoid.extend();
    }

    @Override
    public boolean isClosed() {
        return solenoid.isExtending();
    }

}
