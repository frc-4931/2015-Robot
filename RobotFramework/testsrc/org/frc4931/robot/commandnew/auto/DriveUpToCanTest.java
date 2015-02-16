/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.commandnew.CommandTester;
import org.frc4931.robot.mock.MockSwitch;
import org.frc4931.robot.system.DriveInterpreter;
import org.frc4931.robot.system.mock.MockDrive;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class DriveUpToCanTest {
    private MockDrive driveTrain;
    private DriveInterpreter drive;
    private MockSwitch swtch;
    private CommandTester command;
    
    @Before
    public void beforeEach() {
        driveTrain = new MockDrive();
        drive = new DriveInterpreter(driveTrain);
        swtch = MockSwitch.createNotTriggeredSwitch();
        command = new CommandTester(new DriveUpToCan(drive, swtch));
    }
    
    @Test
    public void shouldDriveUpToCan() {
        // Preconditions
        drive.stop();
        driveTrain.assertStopped();
        swtch.setNotTriggered();
        assertThat(swtch.isTriggered()).isEqualTo(false);
        
        command.step(0);
        driveTrain.assertDrivingForward();
        
        command.step(1);
        driveTrain.assertDrivingForward();
        
        swtch.setTriggered();
        
        command.step(2);
        driveTrain.assertStopped();
    }
}
