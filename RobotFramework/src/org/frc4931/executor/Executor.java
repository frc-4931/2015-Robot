/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.executor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.frc4931.acommand.Command;
import org.frc4931.robot.Robot;
import org.frc4931.utils.Metronome;

/**
 * 
 */
public final class Executor {

    @FunctionalInterface
    public static interface ExecutorInitializer {
        /**
         * Initialization function that should be run before the executor is started.
         * 
         * @param registry the logger registry that can be used to register motors, switches, and other components; never null
         */
        void initialize(Registry registry);
    }

    private static Executor INSTANCE;

    /**
     * Begin the executor's startup sequence and operation.
     * 
     * @param options the executor's configuration options; may be null if the default options are to be used
     * @param initializer the initializer; may be null if nothing is to be initialized
     */
    public static synchronized void initialize(ExecutorOptions options, ExecutorInitializer initializer) {
        if (INSTANCE == null) {
            INSTANCE = new Executor(options, initializer);
        }
    }

    /**
     * Get the log associated with this executor.
     * 
     * @return the log; never null
     */
    public static Logger log() {
        try {
            return INSTANCE.loggers;
        } catch (NullPointerException e) {
            throw new IllegalStateException("The Executor must be initialized before 'log' can be called");
        }
    }

    /**
     * Submit the given command for execution.
     * 
     * @param command the new command to be executed
     */
    public static void submit(Command command) {
        if (command != null) {
            try {
                INSTANCE.submitCommand(command);
            } catch (NullPointerException e) {
                throw new IllegalStateException("The Executor must be initialized before 'submit' can be called");
            }
        }
    }

    /**
     * Shut down this executor, blocking until all resources have been stopped.
     * 
     * @return {@code true} if the executor and all resources were stopped successfully, or {@code false} if the executor could
     *         not be stopped before the time limit elapsed or if this thread has been interrupted while waiting for the executor
     *         to stop
     */
    public static synchronized boolean shutdown() {
        try {
            return INSTANCE.stop();
        } finally {
            INSTANCE = null;
        }
    }

    /**
     * Shut down this executor, blocking at most for the given timeout while all resources are stopped.
     * 
     * @param timeout the maximum time to wait
     * @param unit the time unit of the {@code timeout} argument
     * @return {@code true} if the executor and all resources were stopped successfully, or {@code false} if the executor could
     *         not be stopped before the time limit elapsed or if this thread has been interrupted while waiting for the executor
     *         to stop
     */
    public static synchronized boolean shutdown(long timeout, TimeUnit unit) {
        try {
            return INSTANCE.stop(timeout, unit);
        } finally {
            INSTANCE = null;
        }
    }

    static interface Executable {
        /**
         * Run this executable.
         * 
         * @param elapsedTimeInMillis the elapsed match time in milliseconds
         */
        void run(int elapsedTimeInMillis);
    }

    private final Loggers loggers;
    private final Metronome metronome;
    private final Thread thread;
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile boolean keepRunning = true;

    private Executor(ExecutorOptions options, ExecutorInitializer initializer) {
        metronome = new Metronome(options.getFrequency(), options.getFrequencyUnits());
        loggers = new Loggers(options, Robot::elapsedTimeInMillis);
        if (initializer != null) initializer.initialize(loggers);
        loggers.startup();
        thread = new Thread(this::run);
        thread.setName("Robot exec");
        thread.start();
    }

    protected void run() {
        try {
            while (keepRunning) {
                if (!metronome.pause()) return;

                // run the commands ...

                // Log the data ...
                loggers.run(Robot.elapsedTimeInMillis());
            }
        } finally {
            latch.countDown();
        }
    }

    /**
     * Submit a command for execution.
     * 
     * @param command the command; may not be null
     */
    protected void submitCommand(Command command) {
        // TODO complete
    }

    protected boolean stop() {
        keepRunning = false;
        loggers.shutdown();
        try {
            latch.await();
            return true;
        } catch (InterruptedException e) {
            Thread.interrupted();
            return false;
        }
    }

    protected boolean stop(long timeout, TimeUnit timeUnit) {
        keepRunning = false;
        loggers.shutdown();
        try {
            return latch.await(timeout, timeUnit);
        } catch (InterruptedException e) {
            Thread.interrupted();
            return false;
        }
    }

}
