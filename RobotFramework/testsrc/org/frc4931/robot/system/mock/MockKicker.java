/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system.mock;

import org.frc4931.robot.system.Kicker;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class MockKicker implements Kicker{
    private Position position;
    
    @Override
    public void set(Position pos) {
        this.position = pos;
    }

    @Override
    public boolean is(Position pos) {
        return pos == position;
    }

    public void assertToteStep() {
        assertThat(position).isEqualTo(Position.TOTE_STEP);
    }
    
    public void assertTote() {
        assertThat(position).isEqualTo(Position.TOTE);
    }
    
    public void assertStep() {
        assertThat(position).isEqualTo(Position.STEP);
    }
    
    public void assertGround() {
        assertThat(position).isEqualTo(Position.DOWN);
    }
}
