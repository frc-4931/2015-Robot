/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.frc4931.robot.commandnew.Scheduler.Commands;
import org.frc4931.robot.commandnew.Scheduler.Requireable;

/**
 * Manages all of the state information for a {@link Command}.
 */
class CommandRunner {
    private boolean timed = false;
    private long timeout;
    private long endTime;
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
        this.timeout = (long)(command.getTimeout()*1000);
    }
    
    public CommandRunner(CommandRunner next, CommandRunner... commands) {
        // A next and several children is a branch
        if(commands.length!=0) this.children = commands;
        this.next = next;
    }
    
    /**
     * Steps through all of the state logic for its {@link Command}.
     * @param time the current system time in nanos
     * @return {@code true} if this {@link CommandRunner} is ready to be terminated;
     * {@code false} otherwise
     */
    public boolean step(long time) {
        // if we have a timeout
        if(timeout != 0) {
            endTime = time + timeout;
            timed = true;
            timeout = 0;
        }
        if(timed && time >= endTime) {
            state = State.INTERUPTED;
        }
        
        // If we don't have children or a command, we are a fork and must be done
        if(children == null && command == null) return true;
        
        // If we have children, but no command, we are a branch
        if(children != null && command == null) {
            assert command == null;
            // We are done as long as none of our children are not
            boolean childrenDone = true;
            for(CommandRunner command : children) if(!command.step(time)) childrenDone = false;
            return childrenDone;
        }
        
        // If we have a command, but no children, manage our command
        
        // If we are uninitialized initialize us
        if(state == State.UNINITIALIZED) {
            command.initialize();
            state = State.INITIALIZED;
        }
        
        // If we haven't been executed yet
        if(state == State.INITIALIZED) {
            state = State.RUNNING;
        }
        
        // If we should be running
        if(state == State.RUNNING) {
            if(command.execute()) state = State.FINISHED;
        }
        
        // If we are pending finalization
        if(state == State.FINISHED || state == State.INTERUPTED) {
            command.end();
            state = State.FINALIZED;
        }
        
        if(state==State.FINALIZED) return true;
        return false;
    }
    
    public void after(Commands commandList) {
        // Add our own next (if we have one) and the nexts of our children (if we have them)
        if(next != null) {
            commandList.add(next);
        }
        if(children != null) {
            for(CommandRunner command : children) command.after(commandList);
        }
    }
    
    /**
     * Schedules its {@link Command} to be canceled next iteration.
     */
    public void cancel() {
        state = State.INTERUPTED;
        if(children!=null)
            for(CommandRunner runner : children)
                runner.cancel();
        if(next!=null)
            next = null;
    }
    
    @Override
    public String toString() {
        if(command != null) {
            if(next != null) return command.toString() + " -> " + next.toString();
            return command.toString();
        }
        
        if(children!=null) {
            if(next != null) return Arrays.toString(children) + " -> " + next.toString();
            return Arrays.toString(children);
        }
        
        return "FORK<" + next.toString() +">";
    }
    
    public boolean isInterruptible() {
        if(command !=null)
            return command.isInterruptible();
        
        else if(children!=null)
            for(CommandRunner runner : children)
                if(!runner.isInterruptible()) return false;
        return true;
    }

    public Set<Requireable> getRequired() {
        Set<Requireable> required = new HashSet<>();
        if(command != null) {
              for(Requireable r : command.getRequirements()) required.add(r);
        } else if(children!=null) {
            for(CommandRunner runner : children) {
                required.addAll(runner.getRequired());
            }
        }
        return required;
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
