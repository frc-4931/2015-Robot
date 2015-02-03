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
    private CommandRunner[] children = null;
    private CommandRunner next;
    private State state = State.UNINITIALIZED;
    
    public CommandRunner(Command command) {
        // Just a command and no next is a leaf
        this(null, command);
    }
    
    public CommandRunner(CommandRunner next, Command command) {
        // A command and a next is a node
        this.command = command;
        this.next = next;
    }
    
    public CommandRunner(CommandRunner next, CommandRunner... commands) {
        // A next and several children is a branch
        this.children = commands;
        this.next = next;
    }
    
    /**
     * Steps through all of the state logic for its {@link Command}.
     * @return {@code true} if this {@link CommandRunner} is ready to be terminated;
     * {@code false} otherwise
     */
    public boolean step() {
        // If we don't have children or a command, we are a fork and must be done
        if(children.length == 0 && command == null) return true;
        
        // If we have children, but no command, we are a branch
        if(children.length > 0) {
            assert command == null;
            
            // If we were canceled, cancel our children
            if(state == State.INTERUPTED)
                for(CommandRunner command : children) command.cancel();
            
            // We are done as long as none of our children are not
            boolean flag = true;
            for(CommandRunner command : children) if(!command.step()) flag = false;
            return flag;
        }
        
        // If we have a command, but no children, manage our command
        
        // If we are uninitialized initialize us
        if(state == State.UNINITIALIZED) {
            command.initialize();
            state = State.INITIALIZED;
        }
        
        // If we haven't been executed yet, execute the first time, otherwise just execute
        if(state == State.INITIALIZED) {
            if(command.firstExecute()) state = State.FINISHED;
        } else if(state == State.RUNNING) {
            if(command.execute()) state = State.FINISHED;
        }
        
        // If we are pending finalization
        if(state == State.FINISHED || state == State.INTERUPTED) {
            command.finalize();
            state = State.FINALIZED;
            return true;
        }
        return false;
    }
    
    public void after(Commands commandList) {
        // Add our own next (if we have one) and the nexts of our children
        if(next != null) commandList.add(next);
        for(CommandRunner command : children) command.after(commandList);
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
