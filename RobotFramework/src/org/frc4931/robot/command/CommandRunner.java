/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.command.Scheduler.Commands;

/**
 * Manages all of the state information for a {@link Command}.
 */
class CommandRunner {
    private Command command;
    private CommandRunner[] commands = null;
    private CommandRunner next;
    private final boolean isBranch;
    private State state = State.UNINITIALIZED;
    
    public CommandRunner(Command command) {
        this(null, command);
    }
    
    public CommandRunner(CommandRunner next, Command command) {
        this.command = command;
        this.next = next;
        isBranch = false;
    }
    
    public CommandRunner(CommandRunner next, CommandRunner... commands) {
        this.commands = commands;
        this.next = next;
        isBranch = true;
    }
    
    /**
     * Steps through all of the state logic for its {@link Command}.
     * @return {@code true} if this {@link CommandRunner} is ready to be terminated;
     * {@code false} otherwise
     */
    public boolean step() {
        if(isBranch) {
            boolean flag = true;
            for(CommandRunner command : commands) if(!command.step()) flag = false;
            return flag;
        }
        
        // If we are uninitialized initialize us
        if(state == State.UNINITIALIZED) {
            command.initialize();
            state = State.INITIALIZED;
        }
        
        // Run
        if(state == State.INITIALIZED) {
            // If any command is not finished, we set the state to running
            state = State.FINISHED;
            if(!command.firstExecute()) state = State.RUNNING;
            
        } else if(state == State.RUNNING) {
            // If any command is not finished, we set the state to running
            state = State.FINISHED;
            if(!command.execute()) state = State.RUNNING;
        }
        
        if(state == State.FINISHED || state == State.INTERUPTED) {
            command.finalize();
            state = State.FINALIZED;
            return true;
        }
        return false;
    }
    
    public void after(Commands commandList) {
        commandList.add(next);
    }
    
    /**
     * Schedules its {@link Command} to be canceled next iteration.
     */
    public void cancel() {
        state = State.INTERUPTED;
    }
    
    static final class ForkRunner extends CommandRunner {
        private final CommandRunner forkedCommand;

        public ForkRunner(CommandRunner forked) {
            super(null);
            forkedCommand = forked;
        }
        
        @Override
        public boolean step() { return true; }
        
        @Override
        public void after(Commands commandList) {
            super.after(commandList);
            commandList.add(forkedCommand);
        }
        
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
