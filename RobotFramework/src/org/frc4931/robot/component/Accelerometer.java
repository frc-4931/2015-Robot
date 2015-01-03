/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.utils.Triple;

/**
 * An accelerometer is a device capable of sensing acceleration. By performing
 * two integrations, an accelerometer can also find velocity and displacement.
 * 
 * @author Zach Anderson
 */
public interface Accelerometer {
    
    /**
     * Gets the acceleration on all three axis of this {@link Accelerometer}.
     * @return a {@link Triple} containing all three axis of acceleration
     */
    public Triple getAcceleration();
    
    /**
     * Gets the velocity on all three axis of this {@link Accelerometer},
     * found by integrating the acceleration with respect to time.
     * @return a {@link Triple} containing all three axis of velocity
     */
    public Triple getVelocity();
    
    /**
     * Gets the displacement on all three axis of this {@link Accelerometer},
     * found by integrating the velocity with respect to time.
     * @return a {@link Triple} containing all three axis of displacement
     */
    public Triple getDisplacement();
    
    /**
     * Updates the current acceleration of this {@link Accelerometer} and
     * performs the integrations to find velocity and displacement.
     */
    public void update();
}
