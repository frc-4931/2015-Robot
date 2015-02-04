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
        list.add(command);
    }
    
    public void add(CommandRunner command) {
        list.add(command);
    }
    
    /**
     * Steps once though all of the {@link Command}s in the {@link Scheduler}.
     */
    public void step() {
        list.step();
    }
    
    final static class Commands {
        Queue<CommandRunner> beingExecuted = new LinkedList<>();
        Queue<CommandRunner> pendingAdditon = new LinkedList<>();
        
        public Commands() { }

        public void step() {
            while(!pendingAdditon.isEmpty()) beingExecuted.offer(pendingAdditon.poll());
            
            // Run all of the commands, if one is done, don't put it back in the queue
            int initialSize = beingExecuted.size();
            for(int i = 0; i < initialSize; i++) {
                CommandRunner runner = beingExecuted.poll();
                if(runner.step())
                    runner.after(this);
                else
                    beingExecuted.offer(runner);
            }
        }
        
        public void add(Command command) {
            pendingAdditon.offer(new CommandRunner(command));
        }
        
        public void add(CommandRunner command) {
            pendingAdditon.offer(command);
        }
    }
}
