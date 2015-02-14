/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew;

import org.frc4931.robot.commandnew.Scheduler.Requireable;

/**
 * 
 */
public abstract class Command {
    private Requireable[] requirements = new Requireable[0];
    private boolean interruptable = true;
    /**
     * Set up this {@link Command}. No physical hardware should be manipulated.
     */
    public abstract void initialize();
    
    /**
     * Executed once after this {@link Command} is initialized, should return {@code true}
     * if this {@link Command} is complete. Defers to {@link #execute()} by default.
     * @return {@code true} if this {@link Command} is complete; {@code false} otherwise
     */
    public boolean firstExecute() {
        return execute();
    }
    
    /**
     * Executed repeatedly after this {@link Command} is initialized until it
     * returns {@code true}.
     * @return {@code true} if this {@link Command} is complete; {@code false} otherwise
     */
    public abstract boolean execute();
    
    /**
     * Cleans up the resources used by this command and puts the robot in a safe state.
     */
    public abstract void end();
    
    /**
     * Sets the requirements of this {@link Command}. If a command cannot obtain its
     * requirements, it will not be executed.
     * @param requirements the {@link Requireable}s this {@link Command} requires
     */
    public final void requires(Requireable... requirements) {
        this.requirements = requirements;
    }
    
    final Requireable[] getRequirements(){
        return requirements;
    }
    
    /**
     * Sets this {@link Command} to not be interrupted if another command with the same
     * requirements is added to the scheduler. By default the new command will cancel the
     * old one.
     */
    protected final void setNotInterruptable() {
        this.interruptable = false;
    }
    
    final boolean isInterruptable() {
        return interruptable;
    }
}
