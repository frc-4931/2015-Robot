/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.acommand;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A immuntable unique identifier for a command.
 */
public final class CommandID implements Comparable<CommandID>{
    
    private static final Map<Class<Command>,CommandID> COMMANDS = new HashMap<>();
    private static final AtomicInteger ID = new AtomicInteger();
    
    /**
     * Get the identifier for the given command class with optional name.
     * @param clazz the {@link Command} class; may not be null
     * @param name the human-readable name of the class; if null, then the class' {@link Class#getSimpleName() simple name} will be used
     * @return the identifier for the command; never null
     */
    public static synchronized CommandID get( Class<Command> clazz, String name ) {
        CommandID id = COMMANDS.get(clazz);
        if ( id == null ) {
            if ( clazz == null ) throw new IllegalArgumentException("The 'clazz' argument may not be null");
            id = new CommandID(clazz,ID.incrementAndGet(),name);
            COMMANDS.put(clazz,id);
        }
        return id;
    }

    private final Class<Command> commandClass;
    private final int id;
    private final String name;
    
    private CommandID(Class<Command> clazz, int id, String name) {
        this.commandClass = clazz;
        this.name = name;
        this.id = id;
    }
    
    /**
     * The the name of the command.
     * @return the command's name; never null
     */
    public String getName() {
        return name != null ? name : commandClass.getSimpleName();
    }
    
    /**
     * Get the unique numeric identifier for this command.
     * @return the numeric identifier
     */
    public int asInt() {
        return id;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof CommandID ) {
            return ((CommandID)obj).id == this.id;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return getName() + " (" + id + ")";
    }
    
    @Override
    public int compareTo(CommandID that) {
        if ( this == that ) return 0;
        if ( that == null ) return 1;
        return this.id - that.id;
    }

}
