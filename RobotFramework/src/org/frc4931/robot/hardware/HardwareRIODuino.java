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
     * Create a new connection via the MXP serial port to the RIODuino.
     */
    public HardwareRIODuino() {
        super(new SerialPort(9600, SerialPort.Port.kMXP));
    }
}
