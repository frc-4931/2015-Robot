/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.AngleSensor;

import edu.wpi.first.wpilibj.Encoder;

/**
 * 
 */
class HardwareEncoder implements AngleSensor{
    private Encoder encoder;
    private double zero;
 
    public HardwareEncoder(int aChannel, int bChannel, double distancePerPulse) {
        encoder = new Encoder(aChannel, bChannel);
        encoder.setDistancePerPulse(distancePerPulse);
    }
    
    @Override
    public double getAngle() {
        return encoder.getDistance() - zero;
    }

    @Override
    public void reset() {
        zero = encoder.getDistance();
    }

}
