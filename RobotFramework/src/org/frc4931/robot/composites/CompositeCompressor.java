/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.composites;

import org.frc4931.robot.component.Relay;
import org.frc4931.robot.component.Switch;
import org.frc4931.robot.system.Compressor;

/**
 * 
 */
public class CompositeCompressor implements Compressor{
    private final Relay relay;
    private final Switch pressureSwitch;
    
    public CompositeCompressor(Relay relay, Switch pressureSwitch) {
        this.relay = relay;
        this.pressureSwitch = pressureSwitch;
    }
    
    @Override
    public boolean isPressurized() {
        return pressureSwitch.isTriggered();
    }

    @Override
    public void activate() {
        relay.on();
    }

    @Override
    public void deactivate() {
        relay.off();
    }

}
