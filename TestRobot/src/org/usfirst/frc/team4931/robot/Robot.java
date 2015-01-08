
package org.usfirst.frc.team4931.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	@Override
    public void robotInit() {
    	System.out.println("hello world im a bag robot");
    }
	
    /**
     * This function is called periodically during operator control
     */
	@Override
    public void teleopPeriodic() {
    	DigitalInput in = new DigitalInput(0);
    	DigitalOutput out = new DigitalOutput(1);
    	out.set(in.get());
    }
}
