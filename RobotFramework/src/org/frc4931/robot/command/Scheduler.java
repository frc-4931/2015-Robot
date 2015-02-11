/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import java.util.LinkedList;
import java.util.Queue;

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
    
    final static class Commands {
        private Queue<CommandRunner> beingExecuted = new LinkedList<>();
        private Queue<CommandRunner> pendingAdditon = new LinkedList<>();
        
        public Commands() { }

        public void step(long time) {
            while(!pendingAdditon.isEmpty()) beingExecuted.offer(pendingAdditon.poll());
            
            // Run all of the commands, if one is done, don't put it back in the queue
            int initialSize = beingExecuted.size();
            for(int i = 0; i < initialSize; i++) {
                CommandRunner runner = beingExecuted.poll();
                if(runner.step(time))
                    runner.after(this, time);
                else
                    beingExecuted.offer(runner);
            }
        }
        
        void add(CommandRunner command) {
            pendingAdditon.offer(command);
        }
    }
}
