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
    private final double timeout;
    private final Requireable[] requirements;
    private boolean interruptible = true;
    
    /**
     * @param timeout how long this command executes before terminating, zero is forever
     * @param requirements the {@link Requireable}s this {@link Command} requires
     */
    public Command(double timeout, Requireable... requirements) {
        this.timeout = timeout;
        this.requirements = requirements;
    }
    
    public Command(Requireable... requirements) {
        this(0, requirements);
    }
    
    /**
     * Set up this {@link Command}. No physical hardware should be manipulated.
     */
    public void initialize() { }
    
    /**
     * Executed repeatedly after this {@link Command} is initialized until it
     * returns {@code true}.
     * @return {@code true} if this {@link Command} is complete; {@code false} otherwise
     */
    public abstract boolean execute();
    
    /**
     * Cleans up the resources used by this command and puts the robot in a safe state.
     */
    public void end() { }
    
    final Requireable[] getRequirements(){
        return requirements;
    }
    
    final double getTimeout() {
        return timeout;
    }
    
    /**
     * Sets this {@link Command} to not be interrupted if another command with the same
     * requirements is added to the scheduler. By default the new command will cancel the
     * old one.
     */
    protected final void setNotInterruptible() {
        this.interruptible = false;
    }
    
    final boolean isInterruptible() {
        return interruptible;
    }
}
