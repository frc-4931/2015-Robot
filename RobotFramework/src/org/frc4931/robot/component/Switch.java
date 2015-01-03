/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * A switch is any readable device that has am active state when it is triggered
 * and an inactive state when it isn't.
 * 
 * @author Zach Anderson
 * 
 */
@FunctionalInterface
public interface Switch {
    /**
     * Checks if this switch is triggered.
     * 
     * @return {@code true} if this switch is not in its resting state, or
     *         {@code false} otherwise
     */
    boolean isTriggered();
}
