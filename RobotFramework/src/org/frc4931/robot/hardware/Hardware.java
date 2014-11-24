/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.Relay;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.DigitalInput;
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
        edu.wpi.first.wpilibj.Relay relay = new edu.wpi.first.wpilibj.Relay(channel);
        return new HardwareRelay(relay);
    }

    /**
     * Create a generic normally closed switch on the specified channel.
     * 
     * @param channel the channel the switch is connected to
     * @return a switch on the specified channel
     */
    public static Switch normallyClosedSwitch(int channel) {
        DigitalInput input = new DigitalInput(channel);
        return new HardwareNormallyClosedSwitch(input);
    }

    /**
     * Create a generic normally open switch on the specified channel.
     * 
     * @param channel the channel the switch is connected to
     * @return a switch on the specified channel
     */
    public static Switch normallyOpenSwitch(int channel) {
        DigitalInput input = new DigitalInput(channel);
        return new HardwareNormallyOpenSwitch(input);
    }

    /**
     * Create a solenoid that uses the specified channels on the default module.
     * 
     * @param extendChannel the channel that extends the solenoid
     * @param retractChannel the channel that retracts the solenoid
     * @param initialDirection the initial direction for the solenoid; may not be null
     * @return a solenoid on the specified channels
     */
    public static Solenoid doubleSolenoid(int extendChannel,
                                          int retractChannel, Solenoid.Direction initialDirection ) {
        DoubleSolenoid solenoid = new DoubleSolenoid(extendChannel, retractChannel);
        return new HardwareDoubleSolenoid(solenoid,initialDirection);
    }

    /**
     * Create a solenoid that uses the specified channels on the given module.
     * 
     * @param module the module for the channels
     * @param extendChannel the channel that extends the solenoid
     * @param retractChannel the channel that retracts the solenoid
     * @param initialDirection the initial direction for the solenoid; may not be null
     * @return a solenoid on the specified channels
     */
    public static Solenoid doubleSolenoid(int module, int extendChannel,
                                          int retractChannel, Solenoid.Direction initialDirection) {
        DoubleSolenoid solenoid = new DoubleSolenoid(module, extendChannel, retractChannel);
        return new HardwareDoubleSolenoid(solenoid,initialDirection);
    }
}
