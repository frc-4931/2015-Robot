/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.robot.component.Solenoid.Direction;
import org.frc4931.robot.component.SolenoidWithPosition.Position;
import org.frc4931.robot.component.mock.MockSolenoid;
import org.frc4931.robot.component.mock.MockSwitch;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class LimitedSolenoidTest {

    private MockSolenoid solenoid;
    private MockSwitch retract;
    private MockSwitch extend;
    private SolenoidWithPosition limitedSolenoid;

    @Before
    public void beforeEach() {
        // Start out stopped in an unknown position ...
        solenoid = MockSolenoid.extending();
        retract = MockSwitch.notTriggered();
        extend = MockSwitch.notTriggered();
        limitedSolenoid = new SolenoidWithPosition(solenoid, retract, extend);
    }

    @Test
    public void shouldBeInitializedAsExtending() {
        assertExtending();
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
        assertExtending();
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
        assertRetracting();
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
        assertRetracting();
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
        assertExtending();
        assertExtended();
    }

    @Test
    public void shouldKeepPositionAndDirectionIndepdendentWhenExtending() {
        // Neither switch should be triggered ...
        assertThat(extend.isTriggered()).isFalse();
        assertThat(retract.isTriggered()).isFalse();
        // Telling the solenoid to extend should change it to 'extending' ...
        limitedSolenoid.extend();
        assertExtending();
        retract.setTriggered();
        assertExtending();
        assertRetracted();
        // Once the extend switch is triggered, the solenoid should be extended ...
        extend.setTriggered();
        assertExtending();
        retract.setNotTriggered();
        assertExtended();
    }

    @Test
    public void shouldKeepPositionAndDirectionIndepdendentWhenRetracting() {
        // Neither switch should be triggered ...
        assertThat(extend.isTriggered()).isFalse();
        assertThat(retract.isTriggered()).isFalse();
        // Telling the solenoid to retract should change it to 'retracting' ...
        limitedSolenoid.retract();
        assertRetracting();
        extend.setTriggered();
        assertRetracting();
        assertExtended();
        // Once the retract switch is triggered, the solenoid should be retracted ...
        retract.setTriggered();
        assertRetracting();
        extend.setNotTriggered();
        assertRetracted();
    }
    
    

    protected void assertExtending() {
        assertThat(limitedSolenoid.getDirection()).isEqualTo(Direction.EXTENDING);
        assertThat(limitedSolenoid.isExtending()).isTrue();
        assertThat(limitedSolenoid.isRetracting()).isFalse();
    }

    protected void assertRetracting() {
        assertThat(limitedSolenoid.getDirection()).isEqualTo(Direction.RETRACTING);
        assertThat(limitedSolenoid.isExtending()).isFalse();
        assertThat(limitedSolenoid.isRetracting()).isTrue();
    }

    protected void assertExtended() {
        assertThat(limitedSolenoid.getPosition()).isEqualTo(Position.EXTENDED);
        assertThat(limitedSolenoid.isExtended()).isTrue();
        assertThat(limitedSolenoid.isRetracted()).isFalse();
    }

    protected void assertRetracted() {
        assertThat(limitedSolenoid.getPosition()).isEqualTo(Position.RETRACTED);
        assertThat(limitedSolenoid.isExtended()).isFalse();
        assertThat(limitedSolenoid.isRetracted()).isTrue();
    }
}
