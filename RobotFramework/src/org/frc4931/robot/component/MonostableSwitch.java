/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * A switch that, once triggered, is guaranteed return to the off state.
 */
public interface MonostableSwitch extends Switch{
    public void trigger();
}
