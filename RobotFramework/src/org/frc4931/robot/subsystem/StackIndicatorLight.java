/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.subsystem;

import edu.wpi.first.wpilibj.DriverStation;
import org.frc4931.robot.component.DataStream;

/**
 * A subsystem using a string of addressable LED lights to convey stack height to the driver.
 */
public final class StackIndicatorLight extends SubsystemBase {
    /**
     * Colors that are able to be written to the stream
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

        byte getData() {
            return data;
        }
    }

    private final DataStream stream;

    private byte stackHeight;
    private LightColor color;

    /**
     * Creates a new StackIndicatorLight that uses a data stream.
     *
     * @param stream The stream used to send alliance/height data.
     */
    public StackIndicatorLight(DataStream stream) {
        this.stream = stream;
        stackHeight = 0;
        color = LightColor.BLACK;
    }

    public byte getStackHeight() {
        return stackHeight;
    }

    public void setStackHeight(byte height) {
        this.stackHeight = height;
        send();
    }

    public LightColor getColor() {
        return color;
    }

    public void setColor(LightColor color) {
        this.color = color;
        send();
    }

    public void setColor(DriverStation.Alliance alliance) {
        if (alliance == DriverStation.Alliance.Red) {
            color = LightColor.RED;
        } else if (alliance == DriverStation.Alliance.Blue) {
            color = LightColor.BLUE;
        }
        send();
    }

    /**
     * Writes the currently-stored stack height and color data to the  stream.
     */
    protected void send() {
        byte data = (byte) ((stackHeight << 4) + color.getData());

        stream.write(data);
        stream.flush();
    }

    @Override
    public void shutdown() {
        try {
            super.shutdown();
        } finally {
            stream.shutdown();
        }
    }
}
