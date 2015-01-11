
package org.usfirst.frc.team4931.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    private DigitalInput button;
    private DigitalOutput led;
    private AnalogInput pot;
    private SpeedController motor;
    private PowerDistributionPanel panel;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	@Override
    public void robotInit() {
    	System.out.println("hello world im a bag robot");
    	button = new DigitalInput(0);
    	led = new DigitalOutput(1);
    	pot = new AnalogInput(0);
    	motor = new Talon(0);
    	panel = new PowerDistributionPanel();
    	panel.startLiveWindowMode();
    }
	
    /**
     * This function is called periodically during operator control
     */
	@Override
    public void teleopPeriodic() {
    	led.set(button.get());
    	motor.set(pot.getVoltage()/5.0);
    	
    	SmartDashboard.putData("Power Panel", panel);
    	if(System.nanoTime()%1E6<1E5)
    	    System.out.println("Nanotime is divisable by 1e6");
    }
	
	@Override
	public void disabledPeriodic(){
	    if(System.nanoTime()%1E6<1E5)
            System.out.println("Nanotime is divisable by 1e6");
	}
}
