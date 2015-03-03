/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.commandnew.CommandTester;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class PauseTest {
    CommandTester command;
    
    @Before
    public void beforeEach() {
        command = new CommandTester(new Pause(1));
    }
    
    @Test
    public void shouldFinishAfterTime() throws InterruptedException {
        assertThat(command.step(System.currentTimeMillis())).isEqualTo(false);
        Thread.sleep(100);
        assertThat(command.step(System.currentTimeMillis())).isEqualTo(false);
        Thread.sleep(150);
        assertThat(command.step(System.currentTimeMillis())).isEqualTo(false);
        Thread.sleep(250);
        assertThat(command.step(System.currentTimeMillis())).isEqualTo(false);
        Thread.sleep(500);
        assertThat(command.step(System.currentTimeMillis())).isEqualTo(true);
    }
}
