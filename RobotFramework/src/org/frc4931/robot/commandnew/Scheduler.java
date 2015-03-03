/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * 
 */
public class Scheduler {
    private static final Scheduler INSTANCE = new Scheduler();
    public static Scheduler getInstance() { return INSTANCE; }
    
    private final Commands list = new Commands();
    
    /**
     * Schedule a {@link Command} to be added to the {@link Scheduler}.
     * @param command the {@link Command} to be added
     */
    public void add(Command command) {
        add(command, 0);
    }
    
    public void killAll() {
        list.killAll();
    }
    
    public void add(Command command, long timeout) {
        if(command instanceof CommandGroup)
            command = ((CommandGroup) command).getRoot();
        
        CommandRunner runner = buildCR(command, null);
        runner.setTimeout(timeout);
        list.add(runner);
    }
    
    private CommandRunner buildCR(Command command, CommandRunner last) {
        if(command instanceof CommandGroup){
            CommandGroup cg = (CommandGroup) command;
            Command[] commands = cg.getCommands();
            switch (cg.getType()) {
                case SEQUENTIAL:
                    for(int i = commands.length-1; i >=0 ; i--) {
                        last = buildCR(commands[i], last);
                    }
                    return last;
                case PARRALLEL:
                    CommandRunner[] crs = new CommandRunner[commands.length];
                    for(int i = 0; i<crs.length; i++){
                        crs[i] = buildCR(commands[i], null);
                    }
                    return new CommandRunner(last, crs);
                case FORK:
                    assert commands.length == 1;
                    return new CommandRunner(last, new CommandRunner(buildCR(commands[0], null)));
            }
            // This line should never happen, the switch will throw an exception first
            return null;
        }
        return new CommandRunner(last, command);
    }
    
    /**
     * Steps once though all of the {@link Command}s in the {@link Scheduler}.
     * @param time the current system time in millis
     */
    public void step(long time) {
        list.step(time);
    }
    
    /**
     * Tests if there are any {@link Command}s currently executing or pending execution.
     * 
     * @return {@code true} if there are no {@link Command}s executing or pending;
     *          {@code false} otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    final static class Commands {
        private Queue<CommandRunner> beingExecuted = new LinkedList<>();
        private Queue<CommandRunner> pendingAddition = new LinkedList<>();
        
        private Map<Requireable, CommandRunner> inUse = new HashMap<>();
        
        public Commands() { }

        public void step(long time) {
            int pendingLength = pendingAddition.size();
            for(int i = 0; i < pendingLength; i++)
                if(reserve(pendingAddition.peek()))
                    pendingAddition.poll();
                else
                    // What to do if can't reserve requirement
                    // As of now, don't even try again
                    pendingAddition.poll();
            
            // Run all of the commands, if one is done, don't put it back in the queue
            int initialSize = beingExecuted.size();
            for(int i = 0; i < initialSize; i++) {
                CommandRunner runner= beingExecuted.poll();
                if(runner.step(time))
                    remove(runner, time);
                else
                    beingExecuted.offer(runner);
            }
        }
        
        void add(CommandRunner command) {
            // Add the command runner
            pendingAddition.offer(command);
        }
        
        private boolean reserve(CommandRunner command) {
            Set<Requireable> requirements = command.getRequired();
            
            // Verify that every requirement can be obtained
            for(Requireable required : requirements) {
                CommandRunner user = inUse.get(required);
                if(user != null && !user.isInterruptible())
                    return false;
            }
            
            // Reserve the requirements
            for(Requireable required : requirements) {
                if(inUse.containsKey(required)) inUse.get(required).cancel();
                inUse.put(required, command);
            }
            beingExecuted.offer(command);
            return true;
        }
        
        private void remove(CommandRunner runner, long time) {
            for(Requireable required : runner.getRequired()) inUse.remove(required);
            runner.after(this, time);
        }
        
        boolean isEmpty() {
            return pendingAddition.isEmpty() && beingExecuted.isEmpty();
        }
        
        void killAll() {
            while(!pendingAddition.isEmpty()) pendingAddition.poll();
            int l = beingExecuted.size();
            for(int i = 0; i < l; i++) {
                CommandRunner c = beingExecuted.poll();
                c.cancel();
                c.step(0);
            }
            
        }
    }
    
    public static interface Requireable {}
}
