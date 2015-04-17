/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system.mock;

import org.frc4931.robot.system.Guardrail;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class MockGuardrail implements Guardrail{
    private boolean open;
    
    @Override
    public void open() {
        open = true;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void close() {
        open = false;
    }

    @Override
    public boolean isClosed() {
        return !open;
    }
    
    public void assertOpen() {
        assertThat(open).isEqualTo(true);
    }
    
    public void assertClosed() {
        assertThat(open).isEqualTo(false);
    }
}
