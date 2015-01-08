/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

/**
 * Defines a two state button that expresses its state as a boolean.
 * 
 * @author Zach Anderson
 */
@FunctionalInterface
public interface DigitalButton {
    public boolean get();
}
