/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

/**
 * Manages all of the state information for a {@link Command}.
 */
class CommandRunner {
    private final Command command;
    private State state = State.UNINITIALIZED;
    
    public CommandRunner(Command command) {
        this.command = command;
    }
    
    /**
     * Steps through all of the state logic for its {@link Command}.
     */
    public void step() {
        // If we are uninitialized initialize us
        if(state == State.UNINITIALIZED) {
            command.initialized();
            state = State.INITIALIZED;
        }
        
        // Run
        if(state == State.INITIALIZED) {
            state = State.RUNNING;
            if(command.firstExecute()) state = State.FINISHED;
        } else if(state == State.RUNNING) {
            if(command.execute()) state = State.FINISHED;
        }
        
        
        if(state == State.FINISHED || state == State.INTERUPTED) {
            command.finalize();
            state = State.FINALIZED;
        }
    }
    
    /**
     * Schedules its {@link Command} to be canceled next iteration.
     */
    public void cancel() {
        state = State.INTERUPTED;
    }
    
    /**
     * Defines the current state of a {@link Command}.
     * <li>{@link #UNINITIALIZED} - The {@link Command} has not been initialized or executed yet.
     * <li>{@link #INITIALIZED} - The {@link Command} has been initialized but has not been executed yet.
     * <li>{@link #RUNNING} - The {@link Command} has been initialized and executed at least once.
     * <li>{@link #INTERUPTED} - The {@link Command} has been interrupted, but it has not been processed.
     * <li>{@link #FINISHED} - The {@link Command} has finished but has not been finalized.
     * <li>{@link #FINALIZED} - The {@link Command} has finished and been cleaned up.
     */
    public static enum State{
        UNINITIALIZED, INITIALIZED, RUNNING, INTERUPTED, FINISHED, FINALIZED;
    }
}
