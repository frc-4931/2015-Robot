/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import edu.wpi.first.wpilibj.I2C;
import org.frc4931.utils.Lifecycle;

/**
 * A component representing a RIODuino, an Arduino connected to the roboRIO via MXP/I2C.
 */
public final class RIODuino implements Lifecycle {
    private I2C i2c;

    /**
     * Create a new connection over I2C to the RIODuino.
     *
     * @param address The I2C device addresss to use.
     */
    public RIODuino(int address) {
        this.i2c = new I2C(I2C.Port.kMXP, address);
    }

    /**
     * Read bytes from a remote register.
     *
     * @param register The register address to read from.
     * @param buffer   The buffer to save data to.
     * @param count    The number of bytes to read.
     * @return true if successful; false otherwise.
     */
    public boolean read(int register, byte[] buffer, int count) {
        return !i2c.read(register, count, buffer);
    }

    /**
     * Write bytes to a remote register.
     *
     * @param register The register address to write to.
     * @param data     The data to write to the register.
     * @return true if successful; false otherwise.
     */
    public boolean write(int register, int data) {
        return !i2c.write(register, data);
    }

    public void close() {
        i2c.free();
    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {
        i2c.free();
    }
}
