/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import java.util.ArrayList;
import java.util.List;

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
    
    /**
     * Runs once though all of the {@link Command}s in the {@link Scheduler}.
     */
    public void run() {
        list.step();
    }
    
    final static class Commands {
        List<CommandRunner> beingExecuted = new ArrayList<>();
        List<CommandRunner> pendingAdditon = new ArrayList<>();
        
        public Commands() { }

        public void step() {
            beingExecuted.addAll(pendingAdditon);
            beingExecuted.forEach((runner)->step());
        }
        
        public void add(Command command) {
            pendingAdditon.add(new CommandRunner(command));
        }
        
        public void add(CommandRunner command) {
            pendingAdditon.add(command);
        }
    }
}
