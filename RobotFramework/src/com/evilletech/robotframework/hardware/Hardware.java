/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package com.evilletech.robotframework.hardware;

import com.evilletech.robotframework.api.Relay;
import com.evilletech.robotframework.api.Solenoid;
import com.evilletech.robotframework.api.Switch;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The factory for all devices that directly interface with robot hardware. HardwareFactory provides factory methods
 * for all physical hardware components.
 * 
 * @author Zach Anderson
 * @see Relay
 */
public class Hardware {

    /**
     * Create a relay on the specified channel.
     * 
     * @param channel the channel the relay is connected to
     * @return a relay on the specified channel
     */
    public static Relay relay(int channel) {
        return new HardwareRelay(channel);
    }

    /**
     * Create a generic normally closed switch on the specified channel.
     * 
     * @param channel the channel the switch is connected to
     * @return a switch on the specified channel
     */
    public static Switch normallyClosedSwitch(int channel) {
        return new HardwareNormallyClosedSwitch(channel);
    }

    /**
     * Create a generic normally open switch on the specified channel.
     * 
     * @param channel the channel the switch is connected to
     * @return a switch on the specified channel
     */
    public static Switch normallyOpenSwitch(int channel) {
        return new HardwareNormallyOpenSwitch(channel);
    }

    /**
     * Create a solenoid that uses the specified channels on the default module.
     * 
     * @param extendChannel the channel that extends the solenoid
     * @param retractChannel the channel that retracts the solenoid
     * @return a solenoid on the specified channels
     */
    public static Solenoid doubleSolenoid(int extendChannel,
                                          int retractChannel) {
        DoubleSolenoid solenoid = new DoubleSolenoid(extendChannel, retractChannel);
        return new HardwareDoubleSolenoid(solenoid);
    }

    /**
     * Create a solenoid that uses the specified channels on the given module.
     * 
     * @param module the module for the channels
     * @param extendChannel the channel that extends the solenoid
     * @param retractChannel the channel that retracts the solenoid
     * @return a solenoid on the specified channels
     */
    public static Solenoid doubleSolenoid(int module, int extendChannel,
                                          int retractChannel) {
        DoubleSolenoid solenoid = new DoubleSolenoid(module, extendChannel, retractChannel);
        return new HardwareDoubleSolenoid(solenoid);
    }
}
