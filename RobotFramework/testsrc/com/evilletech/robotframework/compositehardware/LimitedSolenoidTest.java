/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package com.evilletech.robotframework.compositehardware;

import org.junit.Before;
import org.junit.Test;

import com.evilletech.robotframework.api.Solenoid.Action;
import com.evilletech.robotframework.api.Solenoid.Position;
import com.evilletech.robotframework.mockhardware.MockSolenoid;
import com.evilletech.robotframework.mockhardware.MockSwitch;

import static org.fest.assertions.Assertions.assertThat;

public class LimitedSolenoidTest {

    private MockSolenoid solenoid;
    private MockSwitch retract;
    private MockSwitch extend;
    private LimitedSolenoid limitedSolenoid;

    @Before
    public void beforeEach() {
        // Start out stopped in an unknown position ...
        solenoid = MockSolenoid.unknown();
        retract = MockSwitch.notTriggered();
        extend = MockSwitch.notTriggered();
        limitedSolenoid = new LimitedSolenoid(solenoid, retract, extend);
    }

    @Test
    public void shouldBeInitializedAsUnknown() {
        assertOffAtUnknownPosition();
    }

    @Test
    public void shouldExtendUntilExtendSwitchIsTriggered() {
        // Neither switch should be triggered ...
        assertThat(extend.isTriggered()).isFalse();
        assertThat(retract.isTriggered()).isFalse();
        // Telling the solenoid to extend should change it to 'extending' ...
        limitedSolenoid.extend();
        assertExtending();
        // Once the extend switch is triggered, the solenoid should be extended ...
        extend.setTriggered();
        assertExtended();
    }

    @Test
    public void shouldRetractUntilRetractSwitchIsTriggered() {
        // Neither switch should be triggered ...
        assertThat(extend.isTriggered()).isFalse();
        assertThat(retract.isTriggered()).isFalse();
        // Telling the solenoid to extend should change it to 'extending' ...
        limitedSolenoid.retract();
        assertRetracting();
        // Once the extend switch is triggered, the solenoid should be extended ...
        retract.setTriggered();
        assertRetracted();
    }

    @Test
    public void shouldChangeFromExtendingToRetractingUntilRetractSwitchIsTriggered() {
        // Neither switch should be triggered ...
        assertThat(extend.isTriggered()).isFalse();
        assertThat(retract.isTriggered()).isFalse();
        // Telling the solenoid to extend should change it to 'extending' ...
        limitedSolenoid.extend();
        assertExtending();
        // Telling the solenoid to retract should change it to 'retracting' ...
        limitedSolenoid.retract();
        assertRetracting();
        // Once the extend switch is triggered, the solenoid should be extended ...
        retract.setTriggered();
        assertRetracted();
    }

    @Test
    public void shouldChangeFromRetractingToExtendingUntilExtendSwitchIsTriggered() {
        // Neither switch should be triggered ...
        assertThat(extend.isTriggered()).isFalse();
        assertThat(retract.isTriggered()).isFalse();
        // Telling the solenoid to extend should change it to 'extending' ...
        limitedSolenoid.retract();
        assertRetracting();
        // Telling the solenoid to retract should change it to 'retracting' ...
        limitedSolenoid.extend();
        assertExtending();
        // Once the extend switch is triggered, the solenoid should be extended ...
        extend.setTriggered();
        assertExtended();
    }

    @Test
    public void shouldIgnoreRetractSwitchWhenExtending() {
        // Neither switch should be triggered ...
        assertThat(extend.isTriggered()).isFalse();
        assertThat(retract.isTriggered()).isFalse();
        // Telling the solenoid to extend should change it to 'extending' ...
        limitedSolenoid.extend();
        assertExtending();
        retract.setTriggered();
        assertExtending();
        // Once the extend switch is triggered, the solenoid should be extended ...
        extend.setTriggered();
        assertExtended();
    }

    @Test
    public void shouldIgnoreExtendSwitchWhenRetracting() {
        // Neither switch should be triggered ...
        assertThat(extend.isTriggered()).isFalse();
        assertThat(retract.isTriggered()).isFalse();
        // Telling the solenoid to retract should change it to 'retracting' ...
        limitedSolenoid.retract();
        assertRetracting();
        extend.setTriggered();
        assertRetracting();
        // Once the retract switch is triggered, the solenoid should be retracted ...
        retract.setTriggered();
        assertRetracted();
    }
    
    @Test
    public void shouldRecoverFromFailedExtending() {
        // Neither switch should be triggered ...
        assertThat(extend.isTriggered()).isFalse();
        assertThat(retract.isTriggered()).isFalse();
        // Telling the solenoid to extend should change it to 'extending' ...
        limitedSolenoid.extend();
        assertExtending();
        // Fail the underlying solenoid, which should no longer be extending ...
        solenoid.stop(Position.UNKNOWN);
        // Verify that the limited solenoid reflects the new state of the wrapped solenoid ...
        assertOffAtUnknownPosition();
    }
    
    @Test
    public void shouldRecoverFromFailedRetracting() {
        // Neither switch should be triggered ...
        assertThat(extend.isTriggered()).isFalse();
        assertThat(retract.isTriggered()).isFalse();
        // Telling the solenoid to retract should change it to 'retracting' ...
        limitedSolenoid.retract();
        assertRetracting();
        // Fail the underlying solenoid, which should no longer be retracting ...
        solenoid.stop(Position.UNKNOWN);
        // Verify that the limited solenoid reflects the new state of the wrapped solenoid ...
        assertOffAtUnknownPosition();
    }

    protected void assertExtending() {
        assertThat(limitedSolenoid.action()).isEqualTo(Action.EXTENDING);
        assertThat(limitedSolenoid.position()).isEqualTo(Position.UNKNOWN);
        assertThat(limitedSolenoid.isExtending()).isTrue();
        assertThat(limitedSolenoid.isExtended()).isFalse();
        assertThat(limitedSolenoid.isRetracted()).isFalse();
        assertThat(limitedSolenoid.isRetracting()).isFalse();
    }

    protected void assertExtended() {
        assertThat(limitedSolenoid.action()).isEqualTo(Action.OFF);
        assertThat(limitedSolenoid.position()).isEqualTo(Position.EXTENDED);
        assertThat(limitedSolenoid.isExtending()).isFalse();
        assertThat(limitedSolenoid.isExtended()).isTrue();
        assertThat(limitedSolenoid.isRetracted()).isFalse();
        assertThat(limitedSolenoid.isRetracting()).isFalse();
    }

    protected void assertRetracting() {
        assertThat(limitedSolenoid.action()).isEqualTo(Action.RETRACTING);
        assertThat(limitedSolenoid.position()).isEqualTo(Position.UNKNOWN);
        assertThat(limitedSolenoid.isExtending()).isFalse();
        assertThat(limitedSolenoid.isExtended()).isFalse();
        assertThat(limitedSolenoid.isRetracted()).isFalse();
        assertThat(limitedSolenoid.isRetracting()).isTrue();
    }

    protected void assertRetracted() {
        assertThat(limitedSolenoid.action()).isEqualTo(Action.OFF);
        assertThat(limitedSolenoid.position()).isEqualTo(Position.RETRACTED);
        assertThat(limitedSolenoid.isExtending()).isFalse();
        assertThat(limitedSolenoid.isExtended()).isFalse();
        assertThat(limitedSolenoid.isRetracted()).isTrue();
        assertThat(limitedSolenoid.isRetracting()).isFalse();
    }

    protected void assertOffAtUnknownPosition() {
        assertThat(limitedSolenoid.action()).isEqualTo(Action.OFF);
        assertThat(limitedSolenoid.position()).isEqualTo(Position.UNKNOWN);
        assertThat(limitedSolenoid.isExtending()).isFalse();
        assertThat(limitedSolenoid.isExtended()).isFalse();
        assertThat(limitedSolenoid.isRetracted()).isFalse();
        assertThat(limitedSolenoid.isRetracting()).isFalse();
    }
}
