/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.subsystem;

import edu.wpi.first.wpilibj.DriverStation;
import org.frc4931.robot.component.RIODuino;

/**
 * A subsystem using a string of addressable LED lights to convey stack height to the driver.
 */
public final class StackIndicatorLight extends SubsystemBase {
    public static final int STACK_HEIGHT_REGISTER = 1;
    public static final int ALLIANCE_REGISTER = 2;

    private RIODuino arduino;

    /**
     * Create a new instance of this subsystem, in which the lights are controlled by an Arduino.
     *
     * @param arduino The Arduino to interface with.
     */
    public StackIndicatorLight(RIODuino arduino) {
        this.arduino = arduino;
    }

    /**
     * Transmits the stack height to the Arduino in order to display correct length.
     *
     * @param height The (number of?) totes stacked
     */
    public void setStackHeight(int height) {
        arduino.write(STACK_HEIGHT_REGISTER, height);
    }

    /**
     * Transmits the alliance team to the Arduino in order to display correct color.
     *
     * @param alliance The alliance we are currently on.
     */
    public void setAlliance(DriverStation.Alliance alliance) {
        if (alliance == DriverStation.Alliance.Blue) {
            arduino.write(ALLIANCE_REGISTER, 0);
        } else if (alliance == DriverStation.Alliance.Red) {
            arduino.write(ALLIANCE_REGISTER, 1);
        }
    }

    @Override
    public void shutdown() {
        try {
            super.shutdown();
        } finally {
            arduino.shutdown();
        }

    }
}
