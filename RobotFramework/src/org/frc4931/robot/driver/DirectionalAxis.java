/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

/**
 * Defines an axis that points in a direction.
 */
@FunctionalInterface
public interface DirectionalAxis {
    public int getDirection();
}
