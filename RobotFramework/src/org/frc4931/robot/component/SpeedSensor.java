/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * An abstraction of a speed sensor.
 */
@FunctionalInterface
public interface SpeedSensor {
    
    /**
     * Get the current speed.
     * @return the current speed
     */
    double getSpeed();
}
