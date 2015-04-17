/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.system.PowerPanel;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * 
 */
class HardwarePowerPanel implements PowerPanel {
    private static final PowerDistributionPanel PDP = new PowerDistributionPanel();

    @Override
    public double getCurrent(int channel) {
        return PDP.getCurrent(channel);
    }
    
    @Override
    public double getTotalCurrent() {
        return PDP.getTotalCurrent();
    }
    
    @Override
    public double getVoltage() {
        return PDP.getVoltage();
    }
    
    @Override
    public double getTemperature() {
        return PDP.getTemperature();
    }
}
