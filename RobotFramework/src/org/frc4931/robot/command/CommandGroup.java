/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.command.CommandRunner.ForkRunner;

/**
 * 
 */
public class CommandGroup {
    public CommandGroup() {
        Command A = null, B = null, C = null, D = null, E = null, F = null, G = null, H = null, I = null;
        group(
              sequence(A, B, C),
              group(D, E, F),
              fork(group(G, H))
              );
    }
    
    protected CommandRunner group(Command... commands) {
        CommandRunner[] runners = new CommandRunner[commands.length];
        for(int i = 0; i < runners.length; i++) {
            runners[i] = new CommandRunner(commands[i]);
        }
        return group(runners);
    }
    
    private CommandRunner group(CommandRunner... commands) {
        return new CommandRunner(null, commands);
    }
    
    protected CommandRunner sequence(Command... commands) {
        CommandRunner root = new CommandRunner(commands[commands.length-1]);
        for(int i = commands.length-2; i >= 0; i--){
            root = new CommandRunner(root, commands[i]);
        }
        return root;
    }
    
    protected CommandRunner fork(CommandRunner forked) {
        return new ForkRunner(forked);
    }
}
