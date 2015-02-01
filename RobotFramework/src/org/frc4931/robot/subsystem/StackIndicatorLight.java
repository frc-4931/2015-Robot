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

    private final RIODuino rioDuino;

    private byte stackHeight;
    private RIODuino.LightColor color;

    /**
     * Creates a new StackIndicatorLight that uses a data stream.
     *
     * @param rioDuino The RIODuino object to control.
     */
    public StackIndicatorLight(RIODuino rioDuino) {
        this.rioDuino = rioDuino;
        stackHeight = 0;
        color = RIODuino.LightColor.BLACK;
    }

    public byte getStackHeight() {
        return stackHeight;
    }

    /**
     * Sets the stack height to a new value and sends it to the RIODuino.
     *
     * @param stackHeight The new height to set.
     */
    public void setStackHeight(byte stackHeight) {
        this.stackHeight = stackHeight;
        send();
    }

    public RIODuino.LightColor getColor() {
        return color;
    }

    /**
     * Sets the color to a new color and sends it to the RIODuino.
     *
     * @param color The new color to set.
     */
    public void setColor(RIODuino.LightColor color) {
        this.color = color;
        send();
    }

    /**
     * Sets the color according to an alliance and sends it to the RIODuino.
     * @param alliance The alliance that corresponds to the new color.
     */
    public void setColor(DriverStation.Alliance alliance) {
        if (alliance == DriverStation.Alliance.Red) {
            setColor(RIODuino.LightColor.RED);
        } else if (alliance == DriverStation.Alliance.Blue) {
            setColor(RIODuino.LightColor.BLUE);
        }
    }

    /**
     * Sends the currently-stored stack height and color data to the RIODuino.
     */
    protected void send() {
        rioDuino.sendStackIndicatorInfo(stackHeight, color);
    }

    @Override
    public void startup() {
        super.startup();
        rioDuino.startup();
    }

    @Override
    public void shutdown() {
        try {
            super.shutdown();
        } finally {
            rioDuino.shutdown();
        }
    }
}
