/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * Provides easy methods for creation and writing to of a memory mapped file.
 */
public class MappedWriter {
    private final File outFile;
    private final FileChannel channel;
    private final MappedByteBuffer buffer;
    
    /**
     * Constructs a new {@link MappedWriter} to write to the specified file.
     * @param filename the path to the file to be written to
     * @param size the maximum number of bytes that can be written
     * @throws IOException if an I/O error occurs
     */
    public MappedWriter(String filename, long size) throws IOException{
        outFile = new File(filename);
        channel = new RandomAccessFile(outFile, "rw").getChannel();
        buffer = channel.map(MapMode.READ_WRITE, 0, size);
    }
    
    /**
     * Writes the specified {@code byte} to the next position in the file.
     * @param data the {@code byte} to write
     */
    public void write(byte data) {
        buffer.put(data);
    }
    
    /**
     * Writes the specified {@code byte}s to the next {@code data.length} positions in
     * the file.
     * @param data the {@code byte}s to write
     */
    public void write(byte[] data) {
        buffer.put(data);
    }
    
    /**
     * Writes the specified {@code short} to the next 2 positions in the file.
     * @param data the {@code short} to write
     */
    public void writeShort(short data) {
        buffer.putShort(data);
    }
    
    /**
     * Writes the specified {@code int} to the next 4 positions in the file.
     * @param d the {@code int} to write
     */
    public void writeInt(int d) {
        buffer.putInt(d);
    }
    
    /**
     * Gets the number of bytes left in this file.
     * @return the number of writable bytes left
     */
    public long getRemaining(){
        return buffer.remaining();
    }
    
    /**
     * Writes the terminator, forces the OS to update the file
     * and closes the {@code Channel}.
     */
    public void close(){
        // Write terminator
        buffer.putInt(0xFFFFFFFF);
        buffer.force();
        try{
            channel.close();
        } catch (IOException e) {
            System.err.println("Failed to close channel");
            e.printStackTrace();
        }
    }
}
