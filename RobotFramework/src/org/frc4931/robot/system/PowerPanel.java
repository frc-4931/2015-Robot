/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;


/**
 * 
 */
public interface PowerPanel {

    /**
     * Gets the current output from the specified channel in amps.
     * @param channel the channel to read
     * @return the current of the specified channel
     */
    public double getCurrent(int channel);

    /**
     * Gets the total output current of this {@link PowerPanel} in amps.
     * @return the output current of this {@link PowerPanel}
     */
    public double getTotalCurrent();

    /**
     * Gets the input voltage of this {@link PowerPanel} in volts.
     * @return the input voltage of this {@link PowerPanel}
     */
    public double getVoltage();

    /**
     * Gets the temperature of this {@link PowerPanel} in degrees Celsius.
     * @return the temperature of this {@link PowerPanel}
     */
    public double getTemperature();

}