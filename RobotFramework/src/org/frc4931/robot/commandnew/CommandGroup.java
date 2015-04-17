/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew;

import org.frc4931.robot.commandnew.Scheduler.Commands;

/**
 * <p>
 * A {@link CommandGroup} is a series of {@link Command}s executed either in sequence or in
 * parallel that must happen in a certain order. A command group is created by extending
 * {@link CommandGroup}. In the constructor of the subclass should be one call to either
 * {@link #sequentially(Command...)} or {@link #simultaneously(Command...)}. In the first
 * call can be any combination of {@link Command}s, {@link #sequentially(Command...)}s,
 * {@link #simultaneously(Command...)}s, and {@link #fork(Command)}.
 * </p><p>
 * 
 * <li>In a {@link #sequentially(Command...)} call the next {@link Command} starts when the
 * previous has ended. The whole block finishes when the final {@link Command} is done.
 * 
 * <li>In a {@link #simultaneously(Command...)} call all of the {@link Command}s are
 * executed at the same time. The whole block does not finish until the last {@link Command}
 * is done.
 * 
 * <li>A {@link #fork(Command)} call should contain either one {@link Command} or one of the
 * above two. when {@link #fork(Command)} is called, everything inside of it is executed
 * independently of the rest of the {@link CommandGroup}. For the purposes of
 * {@link #sequentially(Command...)} the block finishes instantly.
 * 
 * </p><br><p>
 * The constructor of a {@link CommandGroup} that executes three {@link Command}s in order:<br>
 * <code>sequentially(c1, c2, c3);</code>
 * </p><br>
 * 
 * <p>
 * The constructor of a {@link CommandGroup} that executes three {@link Command}s at the
 * same time:<br>
 * <code>simultaneously(c1, c2, c3);</code>
 * </p><br>
 * 
 * <p>
 * The constructor of a {@link CommandGroup} that executes two {@link Command}s at the
 * same time and then a third {@link Command} after both are finished:<br>
 * <code>sequentially(simultaneously(c1, c2), c3);</code>
 * </p>
 */
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
    public final boolean execute() { return false; }

    static enum Type {
        SEQUENTIAL, PARRALLEL, FORK;
    }
}
