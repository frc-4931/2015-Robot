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
 * This class uses the {@link org.frc4931.robot.component.SerialDataStream} to send and receive data.
 */
public final class HardwareRIODuino extends SerialDataStream {
    /**
     * Create a new connection via MXP to the RIODuino.
     *
     * @param baudRate The rate at which to send data over the serial port.
     */
    public HardwareRIODuino(int baudRate) {
        super(new SerialPort(baudRate, SerialPort.Port.kMXP));
    }
}
