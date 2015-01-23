/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.mock.MockSwitch;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public abstract class AbstractDriveSystemStoppableCommandTest extends AbstractDriveSystemCommandTest {
    protected MockSwitch stopDrivingSwitch;

    @Before
    @Override
    public void beforeEach() {
        stopDrivingSwitch = MockSwitch.createNotTriggeredSwitch();
    }

    @Test
    public void testInitialization() {
        assertSwitchNotTriggered();
        assertStopped();
    }

    @Override
    protected void assertDrivingForwardIfSupposedTo() {
        if (stopDrivingSwitch.isTriggered()) {
            assertStopped();
        } else {
            assertDrivingForward();
        }
    }

    @Override
    protected void assertDrivingBackwardIfSupposedTo() {
        if (stopDrivingSwitch.isTriggered()) {
            assertStopped();
        } else {
            assertDrivingBackward();
        }
    }

    @Override
    protected void assertTurningLeftIfSupposedTo() {
        if (stopDrivingSwitch.isTriggered()) {
            assertStopped();
        } else {
            assertTurningLeft();
        }
    }

    @Override
    protected void assertTurningRightIfSupposedTo() {
        if (stopDrivingSwitch.isTriggered()) {
            assertStopped();
        } else {
            assertTurningRight();
        }
    }

    protected void assertSwitchTriggered() {
        assertThat(stopDrivingSwitch.isTriggered()).isTrue();
    }

    protected void assertSwitchNotTriggered() {
        assertThat(stopDrivingSwitch.isTriggered()).isFalse();
    }
}
