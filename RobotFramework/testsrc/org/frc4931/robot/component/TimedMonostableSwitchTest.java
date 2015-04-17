/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class TimedMonostableSwitchTest {
    private TimedMonostableSwitch swtch;
    
    @Test
    public void shouldTurnOffSwitchAfterSetTime() {
        swtch = TimedMonostableSwitch.getUnregistered(1);
        
        assertNotTriggered();
        
        // Should be immediately triggered
        swtch.trigger();
        assertTriggered();
        
        swtch.execute(0);
        assertTriggered();
        
        swtch.execute(5);
        assertTriggered();
        
        swtch.execute(10);
        assertTriggered();
        
        swtch.execute(999);
        assertTriggered();
        
        // And stay triggered until 1 second after last trigger
        swtch.execute(1000);
        assertNotTriggered();
    }
    
    @Test
    public void shouldDebounceMultipleInputs() {
        swtch = TimedMonostableSwitch.getUnregistered(0.005);
        
        assertNotTriggered();
        
        // First press triggers as normal
        swtch.trigger();
        assertTriggered();
        
        swtch.execute(0);
        assertTriggered();
        
        swtch.execute(1);
        assertTriggered();
        
        // Second press resets timer, still only one pulse output
        swtch.trigger();
        assertTriggered();
        
        swtch.execute(2);
        assertTriggered();
        
        // Pulse ends 5 millis after last press was registered
        swtch.execute(7);
        assertNotTriggered();
    }
    
    private void assertTriggered() {
        assertThat(swtch.isTriggered()).isEqualTo(true);
    }
    
    private void assertNotTriggered() {
        assertThat(swtch.isTriggered()).isEqualTo(false);
    }
}
