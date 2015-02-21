/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.composites;

import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.system.Lifter;

/**
 * 
 */
public class CompositeLifter implements Lifter{
    private final Solenoid solenoid;
    
    public CompositeLifter(Solenoid solenoid) {
        this.solenoid = solenoid;
    }
    
    @Override
    public void raise() {
        solenoid.extend();
    }

    @Override
    public boolean isUp() {
        return solenoid.isExtending();
    }

    @Override
    public void lower() {
        solenoid.retract();
    }

    @Override
    public boolean isDown() {
        return solenoid.isRetracting();
    }

}
