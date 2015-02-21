/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * 
 */
@FunctionalInterface
public interface CurrentSensor {
    /**
     * Gets the current of this sensor in amps.
     * @return the current of this sensor in amps
     */
    public double getCurrent();
}
