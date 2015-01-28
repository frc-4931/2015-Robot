/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.utils.Lifecycle;

public interface DataStream extends Lifecycle {
    public int available();

    public byte[] read(int count);

    public byte readByte();

    public short readShort();

    public int readInt();

    public long readLong();

    public float readFloat();

    public double readDouble();

    public int write(byte... data);

    public void writeByte(byte value);

    public void writeShort(short value);

    public void writeInt(int value);

    public void writeLong(long value);

    public void writeFloat(float value);

    public void writeDouble(double value);

    public void flush();
}
