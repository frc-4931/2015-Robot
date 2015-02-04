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
public class CommandGroup {
    /**
     * Wraps a {@link Command} in a {@link CommandRunner}.
     * @param command the {@link Command} to wrap
     * @return the {@link CommandRunner} wrapping that {@link Command}
     */
    public CommandRunner ex(Command command) {
        return new CommandRunner(command);
    }

    /**
     * Wraps several {@link CommandRunner}s in a single {@link CommandRunner} that executes
     * them simultaneously.
     * @param commands the {@link CommandRunner}s to wrap
     * @return the {@link CommandRunner} wrapping the {@link CommandRunner}s
     */
    public CommandRunner simultaneously(CommandRunner... commands) {
        return new CommandRunner(null, commands);
    }
    
    /**
     * Creates a single {@link CommandRunner} that executes several {@link CommandRunner}s in
     * sequential order.
     * @param commands the {@link CommandRunner}s to be executed
     * @return the {@link CommandGroup} doing the executing
     */
    public CommandRunner sequentially(CommandRunner... commands) {
        CommandRunner root = new CommandRunner(null, commands[commands.length-1]);
        for(int i = commands.length-2; i >= 0; i--){
            root = new CommandRunner(root, commands[i]);
        }
        return root;
    }
    
    /**
     * Creates a {@link CommandRunner} that executes a single {@link CommandRunner} independent
     * of any other {@link CommandRunner}s in the current {@link CommandRunner}.
     * @param forked the {@link CommandRunner} to fork
     * @return the {@link CommandRunner} that will fork
     */
    public CommandRunner fork(CommandRunner forked) {
        return new CommandRunner(forked);
    }
}
