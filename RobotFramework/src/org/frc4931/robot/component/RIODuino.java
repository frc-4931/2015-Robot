/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.utils.Lifecycle;

/**
 * A component representing a RIODuino, an Arduino connected to the roboRIO via MXP.
 * This class uses the {@link org.frc4931.robot.component.SerialDataStream} to send and receive data.
 */
public final class RIODuino implements Lifecycle {
    /**
     * Colors that are able to be displayed on the stack indicator light
     */
    public enum LightColor {
        BLACK((byte) 0b000),
        WHITE((byte) 0b111),
        RED((byte) 0b100),
        YELLOW((byte) 0b110),
        GREEN((byte) 0b010),
        CYAN((byte) 0b011),
        BLUE((byte) 0b001),
        MAGENTA((byte) 0b101);

        private final byte data;

        LightColor(byte data) {
            this.data = data;
        }

        protected byte getData() {
            return data;
        }
    }

    private final DataStream dataStream;

    /**
     * Create a new connection via the MXP serial port to the RIODuino.
     */
    public RIODuino(DataStream dataStream) {
        this.dataStream = dataStream;
    }

    /**
     * Writes new stack height and color data to the stream.
     */
    public void sendStackIndicatorInfo(byte stackHeight, LightColor color) {
        byte data = (byte) ((stackHeight << 4) + color.getData());

        dataStream.writeByte(data);
        dataStream.flush();
    }

    @Override
    public void startup() {
        dataStream.startup();
    }

    @Override
    public void shutdown() {
        dataStream.shutdown();
    }
}
