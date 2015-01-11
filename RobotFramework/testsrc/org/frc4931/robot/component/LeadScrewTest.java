/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */

/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.robot.component.mock.MockMotor;
import org.frc4931.robot.component.mock.MockSwitch;
import org.frc4931.utils.Operations;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class LeadScrewTest {
    private MockMotor motor;
    private MockSwitch low;
    private MockSwitch step;
    private MockSwitch tote;
    private MockSwitch toteOnStep;
    private LeadScrew leadScrew;

    @Before
    public void beforeEach() {
        // Start stopped in an unknown position
        motor = MockMotor.stopped();
        low = MockSwitch.notTriggered();
        step = MockSwitch.notTriggered();
        tote = MockSwitch.notTriggered();
        toteOnStep = MockSwitch.notTriggered();
        leadScrew = new LeadScrew(motor, low, step, tote, toteOnStep);
    }

    @Test
    public void initialization() {
        assertStopped();
        assertPositionUnknown();
        assertLastPositionUnknown();
    }

    @Test
    public void downUntilLow() {
        assertStopped();

        leadScrew.moveTowardsLow(0.5);
        assertMovingDown();
        assertPositionUnknown();

        low.setTriggered();
        // A command will be responsible for stopping the motor
        leadScrew.stop();

        assertStopped();
        assertAtLow();
    }

    @Test
    public void upUntilHigh() {
        assertStopped();

        leadScrew.moveTowardsToteOnStep(0.5);
        leadScrew.moveTowardsToteOnStep(0.5);
        assertMovingUp();
        assertPositionUnknown();

        toteOnStep.setTriggered();
        // A command will be responsible for stopping the motor
        leadScrew.stop();

        assertAtToteOnStep();
        assertStopped();
    }

    @Test
    public void lowToHigh() {
        low.setTriggered();

        assertAtLow();
        assertStopped();

        leadScrew.moveTowardsToteOnStep(0.5);
        low.setNotTriggered();

        assertPositionUnknown();
        assertMovingUp();

        toteOnStep.setTriggered();
        // A command will be responsible for stopping the motor
        leadScrew.stop();

        assertAtToteOnStep();
        assertStopped();
    }

    @Test
    public void highToLow() {
        toteOnStep.setTriggered();

        assertAtToteOnStep();
        assertStopped();

        leadScrew.moveTowardsLow(0.5);
        toteOnStep.setNotTriggered();

        assertPositionUnknown();
        assertMovingDown();

        low.setTriggered();
        // A command will be responsible for stopping the motor
        leadScrew.stop();

        assertAtLow();
        assertStopped();
    }

    @Test
    public void noSwitchesTriggered() {
        low.setNotTriggered();
        step.setNotTriggered();
        tote.setNotTriggered();
        toteOnStep.setNotTriggered();
        assertPositionUnknown();
    }

    @Test
    public void multipleSwitchesTriggered() {
        low.setTriggered();
        step.setTriggered();
        tote.setTriggered();
        toteOnStep.setTriggered();
        assertPositionUnknown();
    }

    @Test
    public void correctSignWhenHigh() {
        leadScrew.moveTowardsToteOnStep(-0.5);
        assertThat(Operations.fuzzyCompare(motor.getSpeed(), 0.5)).isEqualTo(0);
    }

    @Test
    public void correctSignWhenLow() {
        leadScrew.moveTowardsLow(0.5);
        assertThat(Operations.fuzzyCompare(motor.getSpeed(), -0.5))
                .isEqualTo(0);
    }

    private void assertPositionUnknown() {
        assertThat(leadScrew.getPosition()).isEqualTo(LeadScrew.Position.UNKNOWN);
    }

    private void assertLastPositionUnknown() {
        assertThat(leadScrew.getLastPosition()).isEqualTo(LeadScrew.Position.UNKNOWN);
    }

    private void assertAtLow() {
        assertThat(leadScrew.getPosition()).isEqualTo(LeadScrew.Position.LOW);
        assertThat(leadScrew.isLow()).isTrue();
        assertThat(leadScrew.isAtStep()).isFalse();
        assertThat(leadScrew.isAtTote()).isFalse();
        assertThat(leadScrew.isAtToteOnStep()).isFalse();
    }

    private void assertAtStep() {
        assertThat(leadScrew.getPosition()).isEqualTo(LeadScrew.Position.STEP);
        assertThat(leadScrew.isLow()).isFalse();
        assertThat(leadScrew.isAtStep()).isTrue();
        assertThat(leadScrew.isAtTote()).isFalse();
        assertThat(leadScrew.isAtToteOnStep()).isFalse();
    }

    private void assertAtTote() {
        assertThat(leadScrew.getPosition()).isEqualTo(LeadScrew.Position.TOTE);
        assertThat(leadScrew.isLow()).isFalse();
        assertThat(leadScrew.isAtStep()).isFalse();
        assertThat(leadScrew.isAtTote()).isTrue();
        assertThat(leadScrew.isAtToteOnStep()).isFalse();
    }

    private void assertAtToteOnStep() {
        assertThat(leadScrew.getPosition()).isEqualTo(LeadScrew.Position.TOTE_ON_STEP);
        assertThat(leadScrew.isLow()).isFalse();
        assertThat(leadScrew.isAtStep()).isFalse();
        assertThat(leadScrew.isAtTote()).isFalse();
        assertThat(leadScrew.isAtToteOnStep()).isTrue();
    }

    private void assertMovingDown() {
        assertThat(leadScrew.getDirection()).isEqualTo(LeadScrew.Direction.DOWN);
        assertThat(motor.getSpeed()).isLessThan(0.0);
    }

    private void assertMovingUp() {
        assertThat(leadScrew.getDirection()).isEqualTo(LeadScrew.Direction.UP);
        assertThat(motor.getSpeed()).isGreaterThan(0.0);
    }

    private void assertStopped() {
        assertThat(leadScrew.getDirection()).isEqualTo(LeadScrew.Direction.STOPPED);
        assertThat(Operations.fuzzyCompare(motor.getSpeed(), 0.0)).isEqualTo(0);
    }
}
