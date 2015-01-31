/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.utils.Lifecycle;

/**
 * A interface used to send and recieve data
 */
public interface DataStream extends Lifecycle {
    /**
     * Returns the number of bytes available to read.
     *
     * @return The number of bytes currently available.
     */
    public int available();

    /**
     * Reads a number of bytes from the source.
     *
     * @param count The number of bytes to request
     * @return An array of bytes read. The array length may be less than the number requested.
     */
    public byte[] read(int count);

    /**
     * Reads a byte of data from the source.
     *
     * @return The byte read.
     */
    public byte readByte();

    /**
     * Reads a short integer from the source.
     *
     * @return The short integer read.
     */
    public short readShort();

    /**
     * Reads an integer from the source.
     *
     * @return The integer read.
     */
    public int readInt();

    /**
     * Reads a long integer from the source.
     *
     * @return The long integer read.
     */
    public long readLong();

    /**
     * Reads a floating-point number from the source.
     *
     * @return The floating-point number read.
     */
    public float readFloat();

    /**
     * Reads a double floating-point number from the source.
     *
     * @return The double floating-point number read.
     */
    public double readDouble();

    /**
     * Writes an array of bytes to the destination.
     *
     * @param data The data to write.
     * @return The number of bytes actually written (which may be less than the number passed in).
     */
    public int write(byte[] data);

    /**
     * Writes a byte to the destination.
     *
     * @param value The byte to write.
     */
    public void writeByte(byte value);

    /**
     * Writes a short integer to the destination.
     *
     * @param value The short integer to write.
     */
    public void writeShort(short value);

    /**
     * Writes an integer to the destination.
     *
     * @param value The integer to write.
     */
    public void writeInt(int value);

    /**
     * Writes a long integer to the destination.
     *
     * @param value The long integer to write.
     */
    public void writeLong(long value);

    /**
     * Writes a floating-point number to the destination.
     *
     * @param value The floating-point number to write.
     */
    public void writeFloat(float value);

    /**
     * Writes a double floating-point number to the destination.
     *
     * @param value The double floating-point number to write.
     */
    public void writeDouble(double value);

    /**
     * Where implemented, sends then clears the write buffer.
     */
    public void flush();
}
