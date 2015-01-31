/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.mock;

import org.frc4931.robot.component.DataStream;

import java.nio.ByteBuffer;

/**
 * A mock implementation of {@link org.frc4931.robot.component.DataStream} that does not require hardware.
 */
public class MockDataStream implements DataStream {
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;
    private ByteBuffer flushBuffer;

    /**
     * Creates a new data stream.
     *
     * @param readCapacity  The capacity of the read buffer.
     * @param writeCapacity The capacity of the write buffer.
     * @param flushCapacity The capacity of the flush buffer.
     */
    public MockDataStream(int readCapacity, int writeCapacity, int flushCapacity) {
        readBuffer = ByteBuffer.allocate(readCapacity);
        writeBuffer = ByteBuffer.allocate(writeCapacity);
        flushBuffer = ByteBuffer.allocate(flushCapacity);
    }

    /**
     * Sets the limit on the read buffer.
     * Delegates {@link java.nio.Buffer#limit(int)}
     *
     * @param limit The new limit
     */
    public void setReadLimit(int limit) {
        readBuffer.limit(limit);
    }

    /**
     * Sets the limit on the write buffer.
     * Delegates {@link java.nio.Buffer#limit(int)}
     *
     * @param limit The new limit
     */
    public void setWriteLimit(int limit) {
        writeBuffer.limit(limit);
    }

    /**
     * Sets the limit on the flush buffer.
     * Delegates {@link java.nio.Buffer#limit(int)}
     *
     * @param limit The new limit
     */
    public void setFlushLimit(int limit) {
        flushBuffer.limit(limit);
    }

    /**
     * Clears the read buffer.
     * Delegates java.nio.Buffer#clear()
     */
    public void clearReadBuffer() {
        readBuffer.clear();
    }

    /**
     * Clears the write buffer.
     * Delegates java.nio.Buffer#clear()
     */
    public void clearWriteBuffer() {
        writeBuffer.clear();
    }

    /**
     * Clears the flush buffer.
     * Delegates java.nio.Buffer#clear()
     */
    public void clearFlushBuffer() {
        flushBuffer.clear();
    }

    /**
     * Puts data in the read buffer (for the class to access using read())
     *
     * @param data The data to put in the buffer.
     */
    public void put(byte[] data) {
        readBuffer.put(data);
    }

    /**
     * Puts a byte in the read buffer.
     *
     * @param value The byte to put.
     */
    public void putByte(byte value) {
        readBuffer.put(value);
    }

    /**
     * Puts a short in the read buffer.
     *
     * @param value The short to put.
     */
    public void putShort(short value) {
        readBuffer.putShort(value);
    }

    /**
     * Puts an int in the read buffer.
     *
     * @param value The int to put.
     */
    public void putInt(int value) {
        readBuffer.putInt(value);
    }

    /**
     * Puts a long in the read buffer.
     *
     * @param value The long to put.
     */
    public void putLong(long value) {
        readBuffer.putLong(value);
    }

    /**
     * Puts a float in the read buffer.
     *
     * @param value The float to put.
     */
    public void putFloat(float value) {
        readBuffer.putFloat(value);
    }

    /**
     * Puts a double in the read buffer.
     *
     * @param value The double to put.
     */
    public void putDouble(double value) {
        readBuffer.putDouble(value);
    }

    /**
     * Gets data flushed by the class.
     * <p>
     * @param count The number of bytes to retrieve.
     *
     * @return The bytes retrieved - may be less than requested number.
     */
    public byte[] get(int count) {
        byte[] result = new byte[count];
        flushBuffer.get(result);
        return result;
    }

    /**
     * Gets a flushed byte.
     *
     * @return The byte retrieved.
     */
    public byte getByte() {
        return flushBuffer.get();
    }

    /**
     * Gets a flushed short
     *
     * @return The short retrieved.
     */
    public short getShort() {
        return flushBuffer.getShort();
    }

    /**
     * Gets a flushed integer.
     *
     * @return The integer retrieved.
     */
    public int getInt() {
        return flushBuffer.getInt();
    }

    /**
     * Gets a flushed long.
     *
     * @return The long retrieved.
     */
    public long getLong() {
        return flushBuffer.getLong();
    }

    /**
     * Gets a flushed float.
     *
     * @return The float retrieved.
     */
    public float getFloat() {
        return flushBuffer.getFloat();
    }

    /**
     * Gets a flushed double.
     *
     * @return The double retrieved.
     */
    public double getDouble() {
        return flushBuffer.getDouble();
    }

    @Override
    public int available() {
        return readBuffer.remaining();
    }

    @Override
    public byte[] read(int count) {
        byte[] result = new byte[count];
        readBuffer.get(result);
        return result;
    }

    @Override
    public byte readByte() {
        return readBuffer.get();
    }

    @Override
    public short readShort() {
        return readBuffer.getShort();
    }

    @Override
    public int readInt() {
        return readBuffer.getInt();
    }

    @Override
    public long readLong() {
        return readBuffer.getLong();
    }

    @Override
    public float readFloat() {
        return readBuffer.getFloat();
    }

    @Override
    public double readDouble() {
        return readBuffer.getDouble();
    }

    @Override
    public int write(byte[] data) {
        writeBuffer.put(data);
        return data.length;
    }

    @Override
    public void writeByte(byte value) {
        writeBuffer.put(value);
    }

    @Override
    public void writeShort(short value) {
        writeBuffer.putShort(value);
    }

    @Override
    public void writeInt(int value) {
        writeBuffer.putInt(value);
    }

    @Override
    public void writeLong(long value) {
        writeBuffer.putLong(value);
    }

    @Override
    public void writeFloat(float value) {
        writeBuffer.putFloat(value);
    }

    @Override
    public void writeDouble(double value) {
        writeBuffer.putDouble(value);
    }

    @Override
    public void flush() {
        flushBuffer.put(writeBuffer.array());
    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {
        readBuffer.clear();
        writeBuffer.clear();
        flushBuffer.clear();
    }
}
