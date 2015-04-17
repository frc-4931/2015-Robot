/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.AngleSensor;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import edu.wpi.first.wpilibj.AnalogInput;

/**
 * 
 */
public class HardwarePot implements AngleSensor {
    private static final double VOLTS_PER_DEGREE = 240.0/5.0;
    private final AnalogInput pot;
    HardwarePot(int channel){
        pot = new AnalogInput(channel);
        
    }
    @Override
    public double getAngle() {
        return pot.getAverageVoltage()*VOLTS_PER_DEGREE;
    }
    
    @Override
    public void reset() {
        throw new NotImplementedException();
    }

}
