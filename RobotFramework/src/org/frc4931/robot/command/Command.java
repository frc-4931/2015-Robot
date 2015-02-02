/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

/**
 * 
 */
public interface Command {
    /**
     * Set up this {@link Command}. No physical hardware should be manipulated.
     */
    public void initialize();
    
    /**
     * Executed once after this {@link Command} is initialized, should return {@code true}
     * if this {@link Command} is complete. Defers to {@link #execute()} by default.
     * @return {@code true} if this {@link Command} is complete; {@code false} otherwise
     */
    public default boolean firstExecute() {
        return execute();
    }
    
    /**
     * Executed repeatedly after this {@link Command} is initialized until it
     * returns {@code true}.
     * @return {@code true} if this {@link Command} is complete; {@code false} otherwise
     */
    public boolean execute();
    
    /**
     * Cleans up the resources used by this command and puts the robot in a safe state.
     */
    public void finalize();
}
