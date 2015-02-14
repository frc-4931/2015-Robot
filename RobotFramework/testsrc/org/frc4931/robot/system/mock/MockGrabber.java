/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system.mock;

import org.frc4931.robot.system.Grabber;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class MockGrabber implements Grabber {
    private Position position;
    private boolean isOpen;
    
    @Override
    public void set(Position pos) {
        position = pos;
    }

    @Override
    public boolean is(Position pos) {
        return position == pos;
    }

    @Override
    public void close() {
        isOpen = false;
    }

    @Override
    public boolean isClosed() {
        return !isOpen;
    }

    @Override
    public void open() {
        isOpen = true;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }
    
    public void assertGrabberOpen() {
        assertThat(isOpen).isEqualTo(true);
    }
    
    public void assertGrabberClosed() {
        assertThat(isOpen).isEqualTo(false);
    }
    
    public void assertGrabberRaised() {
        assertThat(position).isEqualTo(Position.TRANSFER);
    }
    
    public void assertGrabberLowered() {
        assertThat(position).isEqualTo(Position.DOWN);
    }
}
