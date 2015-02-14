/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

/**
 * 
 */
public class Superstructure {
    public final Grabber grabber;
    public final Kicker kicker;
    public final Ramp ramp;
    
    public Superstructure(Grabber grabber, Kicker kicker, Ramp ramp) {
        this.grabber = grabber;
        this.kicker = kicker;
        this.ramp = ramp;
    }
}
