/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;


/**
 * An accelerometer is a device capable of sensing acceleration. By performing
 * two integrations, an accelerometer can also find velocity and displacement.
 * 
 * @author Zach Anderson
 */
public interface Accelerometer {
    
    public double getXacceleration();
    
    public double getYacceleration();
    
    public double getZacceleration();
}
