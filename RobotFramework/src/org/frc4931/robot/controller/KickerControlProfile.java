/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.controller;

import java.util.function.IntSupplier;

import org.frc4931.robot.controller.Controller.Profile;
import org.frc4931.robot.system.Kicker.Position;

/**
 * Control profile for the motor driving the kicker. Runs at a constant speed depending on the number of
 * totes held.
 */
public class KickerControlProfile implements Profile{
    // How close to a Kicker.Position to be considered that position
    private static final double POSITION_TOLERANCE = 5;
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
    //TODO Very application specific, not necessarily a bad thing
    public double getOutput(double input, double target) {
        //TODO if limit switch fails kicker will have a bad day
        // Target is ground
        if(Math.abs(target) < POSITION_TOLERANCE)
            return -moveSpeeds[0];
        
        double error = input - target;
        
        // Below target
        if(error < 0) {
            // Target is transfer
            if(Math.abs(target-Position.TRANSFER.getAngle()) < POSITION_TOLERANCE)
                return moveSpeeds[0];
            
            // Target is guardrail
            return moveSpeeds[toteCount.getAsInt()];
        }
        
        // Above target
        if(error > tolerance)
            return -moveSpeeds[0];
        
        // On target
        assert 0 < error && error < tolerance;
        // Target is transfer (not supporting totes)
        if(Math.abs(target-Position.TRANSFER.getAngle()) < POSITION_TOLERANCE)
            return holdSpeeds[0];
        
        // Target is guardrail (supporting totes)
        return holdSpeeds[toteCount.getAsInt()];
    }

    @Override
    public boolean inTolerance(double input, double target) {
        // error is how far we need to move in a direction to reach target, we want to know the displacement
        // from the target
        double error = input - target;
        if(error < 0)
            return false;
        if(error > tolerance)
            return false;
        return true;
    }
}
