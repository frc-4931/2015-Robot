/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.robot.RobotManager;

/**
 * A distance sensor is a sensor capable of sensing distance. The unit is
 * assumed to be inches.
 * 
 * @author Zach Anderson
 *
 */
@FunctionalInterface
public interface DistanceSensor {
    /**
     * Gets the current value of this {@link DistanceSensor} in inches.
     * 
     * @return the value of this {@link DistanceSensor}
     */
    public double getDistance();
    
    public default short getAsShort(){
        return (short)(getDistance()*Math.pow(2, RobotManager.NUMBER_OF_ADC_BITS));
    }
}
