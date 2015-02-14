/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew;

import org.frc4931.robot.commandnew.Scheduler.Commands;

public class CommandGroup extends Command{
    private Command root;
    private final Command[] commands;
    private final Type type;
    
    protected CommandGroup() {
        commands = null;
        type = Type.SEQUENTIAL;
    }
    
    private CommandGroup(Command[] commands, Type type) {
        this.commands = commands;
        this.type = type;
    }

    Type getType() {
        return type;
    }

    Command[] getCommands() {
        return commands;
    }

    Command getRoot() {
        return root;
    }

    /**
     * Wraps several {@link Commands}s in a single {@link CommandGroup} that executes
     * them simultaneously.
     * @param commands the {@link CommandRunner}s to wrap
     * @return the {@link CommandGroup} wrapping the {@link Command}s
     */
    public CommandGroup simultaneously(Command... commands) {
       CommandGroup cg = new CommandGroup(commands, Type.PARRALLEL);
       root = cg;
       return cg;
    }
    
    /**
     * Creates a single {@link CommandGroup} that executes several {@link Command}s in
     * sequential order.
     * @param commands the {@link Command}s to be executed
     * @return the {@link CommandGroup} wrapping the {@link Command}s
     */
    public CommandGroup sequentially(Command... commands) {
        CommandGroup cg = new CommandGroup(commands, Type.SEQUENTIAL);
        root = cg;
        return cg;
    }
    
    /**
     * Creates a {@link CommandRunner} that executes a single {@link CommandRunner} independent
     * of any other {@link CommandRunner}s in the current {@link CommandRunner}.
     * @param forked the {@link CommandRunner} to fork
     * @return the forked {@link CommandGroup}
     */
    public CommandGroup fork(Command forked) {
        CommandGroup cg = new CommandGroup(new Command[]{forked}, Type.FORK);
        root = cg;
        return cg;
    }
    
    @Override
    public final void initialize() {}

    @Override
    public final boolean execute() { return false; }

    @Override
    public final void end() {}

    static enum Type {
        SEQUENTIAL, PARRALLEL, FORK;
    }
}
