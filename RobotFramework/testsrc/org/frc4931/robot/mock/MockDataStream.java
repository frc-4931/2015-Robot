/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.mock;

import org.frc4931.robot.component.DataStream;

import java.util.PriorityQueue;
import java.util.Queue;

public class MockDataStream implements DataStream {
    private Queue<Byte> readData;
    private Queue<Byte> writtenData;

    public MockDataStream() {
        readData = new PriorityQueue<>();
        writtenData = new PriorityQueue<>();
    }

    public void addReadData(byte... data) {
        for (byte b : data) {
            readData.add(b);
        }
    }

    public byte[] getWrittenData(int count) {
        byte[] data = new byte[Math.min(count, writtenData.size())];

        for (int i = 0; i < data.length; i++) {
            data[i] = writtenData.remove();
        }

        return data;
    }

    @Override
    public int available() {
        return 0;
    }

    @Override
    public byte[] read(int count) {
        return new byte[0];
    }

    @Override
    public byte readByte() {
        return 0;
    }

    @Override
    public short readShort() {
        return 0;
    }

    @Override
    public int readInt() {
        return 0;
    }

    @Override
    public long readLong() {
        return 0;
    }

    @Override
    public float readFloat() {
        return 0;
    }

    @Override
    public double readDouble() {
        return 0;
    }

    @Override
    public int write(byte... data) {
        return 0;
    }

    @Override
    public void writeByte(byte value) {

    }

    @Override
    public void writeShort(short value) {

    }

    @Override
    public void writeInt(int value) {

    }

    @Override
    public void writeLong(long value) {

    }

    @Override
    public void writeFloat(float value) {

    }

    @Override
    public void writeDouble(double value) {

    }

    @Override
    public void flush() {

    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {

    }
}
