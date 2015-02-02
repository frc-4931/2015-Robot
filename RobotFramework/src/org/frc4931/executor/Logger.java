/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.executor;

import org.frc4931.acommand.CommandID;
import org.frc4931.acommand.CommandState;

/**
 * 
 */
public interface Logger {

    /**
     * Record a message to the log. This method does nothing if the {@code message} is {@code null}.
     * 
     * @param message the message
     */
    void record(String message);

    /**
     * Record an exception and a message to the log. This method does nothing if the {@code message} and exception are both
     * {@code null}.
     * 
     * @param message the message
     * @param error the exception
     */
    void record(String message, Throwable error);

    /**
     * Record an exception to the log. This method does nothing if the {@code error} is {@code null}.
     * 
     * @param error the exception
     */
    void record(Throwable error);

    /**
     * Record the change in state of a command.
     * @param command the identifier of the command; may not be null
     * @param state the new state of the command; may not be null
     */
    void record(CommandID command, CommandState state);
}
