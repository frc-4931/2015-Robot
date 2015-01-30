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
    public static final byte UNKNOWN_ALLIANCE_COLOR = 0b010;
    public static final byte RED_ALLIANCE_COLOR = 0b100;
    public static final byte BLUE_ALLIANCE_COLOR = 0b001;

    private final DataStream stream;

    private byte stackHeight;
    private byte color;

    /**
     * Creates a new StackIndicatorLight that uses a data stream.
     *
     * @param stream The stream used to send alliance/height data.
     */
    public StackIndicatorLight(DataStream stream) {
        this.stream = stream;
        stackHeight = 0;
        color = 0b000;
    }

    public byte getStackHeight() {
        return stackHeight;
    }

    public void setStackHeight(byte height) {
        this.stackHeight = height;

        send();
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;

        send();
    }

    public void setColor(boolean red, boolean green, boolean blue) {
        color = 0;
        if (red) {
            color += 0b100;
        }
        if (green) {
            color += 0b010;
        }
        if (blue) {
            color += 0b001;
        }

        send();
    }

    public void setColor(DriverStation.Alliance alliance) {
        if (alliance == DriverStation.Alliance.Red) {
            color = RED_ALLIANCE_COLOR;
        } else if (alliance == DriverStation.Alliance.Blue) {
            color = BLUE_ALLIANCE_COLOR;
        } else {
            color = UNKNOWN_ALLIANCE_COLOR;
        }

        send();
    }

    protected void send() {
        byte data = (byte) ((stackHeight << 4) + color);

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
