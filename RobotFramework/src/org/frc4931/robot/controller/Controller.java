/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.controller;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import org.frc4931.robot.Executor;
import org.frc4931.robot.Executor.Executable;

public class Controller implements Executable {
    private final DoubleSupplier input;
    private final DoubleConsumer output;
    private final Profile profile;
    
    private volatile boolean enabled = false;
    // TODO Atomic?
    private volatile double setpoint;
    
    public Controller(DoubleSupplier input, DoubleConsumer output, Profile profile) {
        this.input = input;
        this.output = output;
        this.profile = profile;
        Executor.getInstance().register(this);
    }
    
    @Override
    public void execute(long time) {
        if(enabled) output.accept(profile.getOutput(input.getAsDouble(), setpoint));
    }
    
    public boolean isAt(double angle) {
        return profile.inTolerance(input.getAsDouble(), angle);
    }
    
    public double getError() {
        return setpoint - input.getAsDouble();
    }
    
    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }
    
    public void enable() {
        enabled = true;
    }
    
    public void disable() {
        enabled = false;
    }
    
    /**
     * Defines the behavior of a controller, how it attempts to correct error.
     */
    public static interface Profile{
        
        /**
         * Calculates the appropriate output value given the current input and target.
         * @param input the current input of the controller
         * @param target the target of the controller
         * @return the output to correct that error
         */
        public double getOutput(double input, double target);
        
        /**
         * Tests if the specified target is acceptable given the tolerance of this {@link Profile}.
         * @param input the current input of the controller
         * @param target the target of the controller
         * @return {@code true} if the error is acceptable; {@code false} otherwise
         */
        public boolean inTolerance(double input, double target);
    }
}
