/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.executor;

import java.util.function.IntSupplier;

import org.frc4931.acommand.CommandID;
import org.frc4931.acommand.CommandState;
import org.frc4931.executor.Executor.Executable;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Switch;
import org.frc4931.utils.Lifecycle;

/**
 * 
 */
final class Loggers implements Lifecycle, Logger, Executable, Registry {
    
    private final MetricLogger dataLogger;
    private final EventLogger eventLogger;
    private final IntSupplier robotElapsedTimeSupplier;
    
    public Loggers( ExecutorOptions options, IntSupplier timeSupplier ) {
        dataLogger = new MetricLogger(options);
        eventLogger = new EventLogger(options);
        robotElapsedTimeSupplier = timeSupplier;
    }
    
    @Override
    public void startup() {
    }
    
    @Override
    public void run( int elapsedTimeInMillis) {
        dataLogger.run(elapsedTimeInMillis);
        eventLogger.run(elapsedTimeInMillis);
    }
    
    @Override
    public void shutdown() {
        try {
            dataLogger.shutdown();
        } finally {
            eventLogger.shutdown();
        }
    }

    /**
     * Registers the value of the specified {@link IntSupplier} to be logged. This method should be called before the
     * @param name the name of this data point; may not be null
     * @param supplier the {@link IntSupplier} of the value to be logged; may not be null
     */
    @Override
    public void register(String name, IntSupplier supplier) {
        dataLogger.register(name,supplier);
    }

    /**
     * Registers a {@link Motor} to be logged.
     * @param name the name of the {@link Motor}; may not be null
     * @param motor the {@link Motor} to be logged; may not be null
     */
    @Override
    public void registerMotor(String name, Motor motor) {
        dataLogger.registerMotor(name, motor);
    }

    /**
     * Registers a {@link Switch} to be logged.
     * @param name the name of the {@link Switch}; may not be null
     * @param swtch the {@link Switch} to be logged; may not be null
     */
    @Override
    public void registerSwitch(String name, Switch swtch) {
        eventLogger.registerSwitch(name, swtch);
    }
    
    @Override
    public void record( String message ) {
        eventLogger.record(robotElapsedTimeSupplier.getAsInt(), message,null);
    }

    @Override
    public void record( String message, Throwable error ) {
        eventLogger.record(robotElapsedTimeSupplier.getAsInt(),message,error);
    }
    
    @Override
    public void record(Throwable error) {
        eventLogger.record(robotElapsedTimeSupplier.getAsInt(),null,error);
    }

    @Override
    public void record( CommandID command, CommandState state ) {
        eventLogger.record(robotElapsedTimeSupplier.getAsInt(),command,state);
    }
    
    
}
