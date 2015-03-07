/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.controller;

import org.frc4931.robot.controller.Controller.Profile;


/**
 * Control profile for the motor connected to the grabbers. Implemented as vanilla p control.
 */
public class GrabberControlProfile implements Profile {
    private final double maxRaiseSpeed;
    private final double maxLowerSpeed;
    private final double p;
    private final double tolerance;
    
    public GrabberControlProfile(double maxRaiseSpeed, double maxLowerSpeed, double p, double tolerance) {
        this.p = p;
        this.maxRaiseSpeed = maxRaiseSpeed;
        this.maxLowerSpeed = maxLowerSpeed;
        this.tolerance = tolerance;
    }
    
    @Override
    public double getOutput(double error) {
        double output = p * error;

        output = Math.max(-maxLowerSpeed, output);
        output = Math.min(maxRaiseSpeed, output);
        
        return output;
    }

    @Override
    public boolean inTolerance(double error) {
        return Math.abs(error) < tolerance;
    }

}
