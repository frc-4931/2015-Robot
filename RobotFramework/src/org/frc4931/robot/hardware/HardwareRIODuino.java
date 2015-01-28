/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import edu.wpi.first.wpilibj.SerialPort;
import org.frc4931.robot.component.SerialDataStream;

/**
 * A component representing a RIODuino, an Arduino connected to the roboRIO via MXP.
 */
public final class HardwareRIODuino extends SerialDataStream {
    /**
     * Create a new connection over I2C to the RIODuino.
     *
     * @param baudRate The rate at which to send data over the serial port.
     */
    public HardwareRIODuino(int baudRate) {
        super(new SerialPort(baudRate, SerialPort.Port.kMXP));
    }
}
