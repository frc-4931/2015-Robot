/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system.mock;

import org.frc4931.robot.system.Lifter;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class MockLifter implements Lifter{
    private boolean up;
    
    @Override
    public void raise() {
        up = true;
    }

    @Override
    public boolean isUp() {
        return up;
    }

    @Override
    public void lower() {
        up = false;
    }

    @Override
    public boolean isDown() {
        return !up;
    }

    public void assertLowered() {
        assertThat(up).isEqualTo(false);
    }
    
    public void assertRaised() {
        assertThat(up).isEqualTo(true);
    }
}
