/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.subsystem;

import edu.wpi.first.wpilibj.PIDController;
import org.frc4931.robot.component.AngleSensor;
import org.frc4931.robot.component.Motor;

/**
 * 
 */
public class Kicker extends SubsystemBase {
    
    private final AngleSensor angleSensor;
    private final Motor motor;
    
    public Kicker(AngleSensor pot,Motor motor){
        
        angleSensor = pot;
        this.motor = motor;
    }
    
    public void setAngle(double angle){
        PIDController pid = new PIDController(1, 0, 0, 0, ()->angleSensor.getAngle(), (speed)->motor.setSpeed(speed));
        pid.setInputRange(0, 270);
        pid.setOutputRange(-1.0, 1.0);
        pid.setContinuous(false);
        pid.setSetpoint(angle);
        
        pid.enable();
    }
    
}
