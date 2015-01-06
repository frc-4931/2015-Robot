/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component.mock;

import org.frc4931.robot.component.Switch;

/**
 * A test implementation of <code>Switch</code> that can be used without any
 * hardware. This class must be constructed through <code>MockFactory</code>.
 * This class provides methods to set the value as well as get it, as it does
 * not interface with actual hardware.
 * 
 * @author Zach Anderson
 * @see Switch
 */
public final class MockSwitch implements Switch {

    /**
     * Creates a {@link MockSwitch} initialized as {@link Switch#isTriggered()
     * untriggered} that does not interface with any hardware components, but
     * still behaves as if it was.
     * 
     * @return the mock switch
     */
    public static MockSwitch notTriggered() {
        return new MockSwitch(false);
    }

    /**
     * Creates a {@link MockSwitch} initialized as {@link Switch#isTriggered()
     * triggered} that does not interface with any hardware components, but
     * still behaves as if it was.
     * 
     * @return the mock switch
     */
    public static MockSwitch triggered() {
        return new MockSwitch(true);
    }

    private boolean triggered;

    private MockSwitch(boolean initialState) {
        triggered = initialState;
    }

    @Override
    public boolean isTriggered() {
        return triggered;
    }

    /**
     * Set this switch as being triggered.
     */
    public void setTriggered() {
        triggered = true;
    }

    /**
     * Set this switch as being not triggered.
     */
    public void setNotTriggered() {
        triggered = false;
    }

    @Override
    public String toString() {
        return triggered ? "closed" : "open";
    }
}
