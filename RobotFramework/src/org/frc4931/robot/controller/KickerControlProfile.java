/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.controller;

import java.util.function.IntSupplier;

import org.frc4931.robot.controller.Controller.Profile;

/**
 * Control profile for the motor driving the kicker. Runs at a constant speed depending on the number of
 * totes held.
 */
public class KickerControlProfile implements Profile{
    private final double   tolerance;
    private final double[] holdSpeeds;
    private final double[] moveSpeeds;
    
    private final IntSupplier toteCount;
    
    public KickerControlProfile(double tolerance, double[] moveSpeeds,
                                double[] holdSpeeds, IntSupplier toteCount) {
        this.tolerance = tolerance;
        this.moveSpeeds = moveSpeeds;
        this.holdSpeeds = holdSpeeds;
        this.toteCount = toteCount;
    }
    
    @Override
    public double getOutput(double error) {
        // error is how far we need to move in a direction to reach target, we want to know the displacement
        // from the target
        error = -1 * error;
        if(error < 0)
            return moveSpeeds[toteCount.getAsInt()];
        if(error > tolerance)
            return -moveSpeeds[0];
        return holdSpeeds[toteCount.getAsInt()];
    }

    @Override
    public boolean inTolerance(double error) {
        // error is how far we need to move in a direction to reach target, we want to know the displacement
        // from the target
        error = -1 * error;
        if(error < 0)
            return false;
        if(error > tolerance)
            return false;
        return true;
    }
}
