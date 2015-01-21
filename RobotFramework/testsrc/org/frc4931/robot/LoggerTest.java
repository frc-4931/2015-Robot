/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class LoggerTest {
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
}
