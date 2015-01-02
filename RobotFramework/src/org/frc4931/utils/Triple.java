/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.utils;

/**
 * Simple class for storing and accessing three doubles.
 * 
 * @author Zach Anderson
 */
public class Triple {
    private final double x;
    private final double y;
    private final double z;
    
    public Triple(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
}
