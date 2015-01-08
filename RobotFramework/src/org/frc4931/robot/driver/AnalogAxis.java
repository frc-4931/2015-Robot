/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

/**
 * Defines some axis that expresses its value between [-1.0, 1.0] inclusive.
 * 
 * @author Zach Anderson
 */
@FunctionalInterface
public interface AnalogAxis {
    public double read();
}
