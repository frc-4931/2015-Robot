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
    private final DataStream stream;

    private boolean autoSend;
    private int stackHeight;
    private DriverStation.Alliance alliance;

    /**
     * Creates a new StackIndicatorLight that uses a data stream.
     *
     * @param stream The stream used to send alliance/height data.
     * @param autoSend Whether or not to automatically send data after it has been updated.
     */
    public StackIndicatorLight(DataStream stream, boolean autoSend) {
        this.stream = stream;
        this.autoSend = autoSend;
        stackHeight = 0;
        alliance = null;
    }

    /**
     * Creates a new StackIndicatorLight that uses a data stream.
     *
     * @param stream The stream used to send alliance/height data over.
     */
    public StackIndicatorLight(DataStream stream) {
        this(stream, false);
    }

    public int getStackHeight() {
        return stackHeight;
    }

    /**
     * Transmits the stack height to the Arduino in order to display correct length.
     *
     * @param height The (number of?) totes stacked
     */
    public void setStackHeight(int height) {
        boolean newValue = height != this.stackHeight;
        this.stackHeight = height;

        if (newValue && autoSend) {
            send();
        }
    }

    public DriverStation.Alliance getAlliance() {
        return alliance;
    }

    /**
     * Transmits the alliance team to the Arduino in order to display correct color.
     *
     * @param alliance The alliance we are currently on.
     */
    public void setAlliance(DriverStation.Alliance alliance) {
        boolean newValue = alliance != this.alliance;
        this.alliance = alliance;

        if (newValue && autoSend) {
            send();
        }
    }

    public void send() {
        byte data = (byte) (stackHeight << 4);

        if (alliance == DriverStation.Alliance.Blue) {
            data += 1;
        } else if (alliance == DriverStation.Alliance.Red) {
            data += 2;
        }

        stream.write(data);
        stream.flush();
    }

    public boolean isAutoSend() {
        return autoSend;
    }

    public void setAutoSend(boolean autoSend) {
        this.autoSend = autoSend;
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
