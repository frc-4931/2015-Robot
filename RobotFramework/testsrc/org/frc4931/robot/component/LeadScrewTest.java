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

        low.setTriggered();
        leadScrew.stop();

        assertAtLow();
        assertStopped();
    }

    @Test
    public void upUntilToteOnStep() {
        assertStopped();

        leadScrew.moveTowardsToteOnStep(0.5);
        leadScrew.moveTowardsToteOnStep(0.5);
        assertMovingUp();

        toteOnStep.setTriggered();
        leadScrew.stop();

        assertAtToteOnStep();
        assertStopped();
    }

    @Test
    public void lowToStep() {
        low.setTriggered();
        leadScrew.update();

        assertAtLow();
        assertStopped();

        leadScrew.moveTowardsStep(0.5);
        low.setNotTriggered();
        assertMovingUp();

        step.setTriggered();
        leadScrew.stop();

        assertAtStep();
        assertStopped();
    }

    @Test
    public void lowToTote() {
        low.setTriggered();
        leadScrew.update();

        assertAtLow();
        assertStopped();

        leadScrew.moveTowardsTote(0.5);
        low.setNotTriggered();
        assertMovingUp();

        tote.setTriggered();
        leadScrew.stop();

        assertAtTote();
        assertStopped();
    }

    @Test
    public void lowToToteOnStep() {
        low.setTriggered();
        leadScrew.update();

        assertAtLow();
        assertStopped();

        leadScrew.moveTowardsToteOnStep(0.5);
        low.setNotTriggered();
        assertMovingUp();

        toteOnStep.setTriggered();
        leadScrew.stop();

        assertAtToteOnStep();
        assertStopped();
    }

    @Test
    public void stepToTote() {
        step.setTriggered();
        leadScrew.update();

        assertAtStep();
        assertStopped();

        leadScrew.moveTowardsTote(0.5);
        step.setNotTriggered();
        assertMovingUp();

        tote.setTriggered();
        leadScrew.stop();

        assertAtTote();
        assertStopped();
    }

    @Test
    public void stepToToteOnStep() {
        step.setTriggered();
        leadScrew.update();

        assertAtStep();
        assertStopped();

        leadScrew.moveTowardsToteOnStep(0.5);
        step.setNotTriggered();
        assertMovingUp();

        toteOnStep.setTriggered();
        leadScrew.stop();

        assertAtToteOnStep();
        assertStopped();
    }

    @Test
    public void toteToToteOnStep() {
        tote.setTriggered();
        leadScrew.update();

        assertAtTote();
        assertStopped();

        leadScrew.moveTowardsToteOnStep(0.5);
        tote.setNotTriggered();
        assertMovingUp();

        toteOnStep.setTriggered();
        leadScrew.stop();

        assertAtToteOnStep();
        assertStopped();
    }

    @Test
    public void toteOnStepToTote() {
        toteOnStep.setTriggered();
        leadScrew.update();

        assertAtToteOnStep();
        assertStopped();

        leadScrew.moveTowardsTote(0.5);
        toteOnStep.setNotTriggered();
        assertMovingDown();

        tote.setTriggered();
        leadScrew.stop();

        assertAtTote();
        assertStopped();
    }

    @Test
    public void toteOnStepToStep() {
        toteOnStep.setTriggered();
        leadScrew.update();

        assertAtToteOnStep();
        assertStopped();

        leadScrew.moveTowardsStep(0.5);
        toteOnStep.setNotTriggered();
        assertMovingDown();

        step.setTriggered();
        leadScrew.stop();

        assertAtStep();
        assertStopped();
    }

    @Test
    public void toteOnStepToLow() {
        toteOnStep.setTriggered();
        leadScrew.update();

        assertAtToteOnStep();
        assertStopped();

        leadScrew.moveTowardsLow(0.5);
        toteOnStep.setNotTriggered();
        assertMovingDown();

        low.setTriggered();
        leadScrew.stop();

        assertAtLow();
        assertStopped();
    }

    @Test
    public void toteToStep() {
        tote.setTriggered();
        leadScrew.update();

        assertAtTote();
        assertStopped();

        leadScrew.moveTowardsStep(0.5);
        tote.setNotTriggered();
        assertMovingDown();

        step.setTriggered();
        leadScrew.stop();

        assertAtStep();
        assertStopped();
    }

    @Test
    public void toteToLow() {
        tote.setTriggered();
        leadScrew.update();

        assertAtTote();
        assertStopped();

        leadScrew.moveTowardsLow(0.5);
        tote.setNotTriggered();
        assertMovingDown();

        low.setTriggered();
        leadScrew.stop();

        assertAtLow();
        assertStopped();
    }

    @Test
    public void stepToLow() {
        step.setTriggered();
        leadScrew.update();

        assertAtStep();
        assertStopped();

        leadScrew.moveTowardsLow(0.5);
        step.setNotTriggered();
        assertMovingDown();

        low.setTriggered();
        leadScrew.stop();

        assertAtLow();
        assertStopped();
    }

//    @Test
//    public void lowToHighIntermittent() {
//        low.setTriggered();
//        leadScrew.update();
//        assertAtLow();
//
//        leadScrew.moveTowardsToteOnStep(0.5);
//        low.setNotTriggered();
//        assertMovingUp();
//
//        tote.setTriggered();
//        leadScrew.update();
//        tote.setNotTriggered();
//
//        leadScrew.moveTowardsStep(0.5);
//        assertMovingDown();
//
//        step.setTriggered();
//        leadScrew.stop();
//
//        assertAtStep();
//        assertStopped();
//    }

//    @Test
//    public void HighToLowIntermittent() {
//        toteOnStep.setTriggered();
//        leadScrew.update();
//        assertAtToteOnStep();
//
//        leadScrew.moveTowardsLow(0.5);
//        toteOnStep.setNotTriggered();
//        assertMovingDown();
//
//        step.setTriggered();
//        leadScrew.update();
//        step.setNotTriggered();
//
//        leadScrew.moveTowardsStep(0.5);
//        assertMovingUp();
//
//        step.setTriggered();
//        leadScrew.stop();
//
//        assertAtStep();
//        assertStopped();
//    }

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
