/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.commandnew.Scheduler.Requireable;

/**
 * 
 */
public interface Guardrail extends Requireable{
    /**
     * Opens this {@link Guardrail}.
     */
    public void open();
    
    /**
     * Tests if this {@link Guardrail} is open.
     * @return {@code true} if this {@link Guardrail} is open, {@code false} otherwise
     */
    public boolean isOpen();
    
    /**
     * Closes this {@link Guardrail}.
     */
    public void close();
    
    /**
     * Tests if this {@link Guardrail} is closed.
     * @return {@code true} if this {@link Guardrail} is closed, {@code false} otherwise
     */
    public boolean isClosed();
}
