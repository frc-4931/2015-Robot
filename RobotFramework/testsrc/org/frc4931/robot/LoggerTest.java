/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.Logger.Mode;
import org.frc4931.robot.mock.MockSwitch;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class LoggerTest {
    private Logger logger;
    
    @Before
    public void beforeEach() {
        logger = new Logger();
    }
    
    @Test
    public void shouldCombineAllTruesToAllOnes(){
        boolean[] values = new boolean[15];
        for(int i = 0; i<values.length;i++)
            values[i] = true;
        assertThat(Logger.bitmask(values)).isEqualTo((short) 0b111111111111111);
    }
    
    @Test
    public void shouldCombineAllFalseToZero(){
        boolean[] values = new boolean[15];
        for(int i = 0; i<values.length;i++)
            values[i] = false;
        assertThat(Logger.bitmask(values)).isEqualTo((short) 0);
    }
    
    @Test
    public void shouldCombineOneTrueToOne(){
        boolean[] values = {true};
        assertThat(Logger.bitmask(values)).isEqualTo((short) 1);
    }
    
    @Test
    public void shouldCombineOneFalseToZero(){
        boolean[] values = {false};
        assertThat(Logger.bitmask(values)).isEqualTo((short) 0);
    }
    
    @Test
    public void shouldCombineBooleansToCorrectBits(){
        boolean[] values = {true, false, true, true, false, false, true};
        assertThat(Logger.bitmask(values)).isEqualTo((short) 0b1001101);
    }
    
    @Test
    public void shouldRegisterSeveralSwitchesToLog() throws InterruptedException {
        logger.setMode(Mode.LOCAL_FILE);
        MockSwitch dummy1 = MockSwitch.createNotTriggeredSwitch();
        MockSwitch dummy2 = MockSwitch.createNotTriggeredSwitch();
        MockSwitch dummy3 = MockSwitch.createNotTriggeredSwitch();
        logger.registerSwitch("Dummy1", dummy1);
        logger.registerSwitch("Dummy2", dummy2);
        logger.registerSwitch("Dummy3", dummy3);
        
        logger.startup();
        
        logger.execute(0);
        dummy1.setTriggered();
        
        logger.execute(1);
        dummy2.setTriggered();
        
        logger.execute(2);
        dummy3.setTriggered();
        
        logger.execute(3);
        logger.shutdown();
    }
}
