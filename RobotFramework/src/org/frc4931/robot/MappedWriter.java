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
    long remaining;
    
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
        remaining = buffer.capacity();
    }
    
    /**
     * Writes the specified {@code byte} to the next position in the file.
     * @param data the {@code byte} to write
     * @throws NoMoreRoomInFileException if there is insufficient room in the file
     */
    public void write(byte data) throws NoMoreRoomInFileException{
        // Save room for terminator (4 bytes)
        if(remaining<Byte.BYTES+4) throw new NoMoreRoomInFileException();
        buffer.put(data);
        remaining-=Byte.BYTES;
    }
    
    /**
     * Writes the specified {@code byte}s to the next {@code data.length} positions in
     * the file.
     * @param data the {@code byte}s to write
     * @throws NoMoreRoomInFileException if there is insufficient room in the file
     */
    public void write(byte[] data) throws NoMoreRoomInFileException{
        // Save room for terminator (4 bytes)
        if(remaining<data.length+4) throw new NoMoreRoomInFileException();
        buffer.put(data);
        remaining-=data.length;
    }
    
    /**
     * Writes the specified {@code short} to the next 2 positions in the file.
     * @param data the {@code short} to write
     * @throws NoMoreRoomInFileException if there is insufficient room in the file
     */
    public void writeShort(short data) throws NoMoreRoomInFileException{
        // Save room for terminator (4 bytes)
        if(remaining<Short.BYTES+4) throw new NoMoreRoomInFileException();
        buffer.putShort(data);
        remaining-=Short.BYTES;
    }
    
    /**
     * Writes the specified {@code int} to the next 4 positions in the file.
     * @param d the {@code int} to write
     * @throws NoMoreRoomInFileException if there is insufficient room in the file
     */
    public void writeInt(int d) throws NoMoreRoomInFileException {
        // Save room for terminator (4 bytes)
        if(remaining<Integer.BYTES+4) throw new NoMoreRoomInFileException();
        buffer.putInt(d);
        remaining-=Integer.BYTES;
    }
    
    /**
     * Gets the number of bytes left in this file.
     * @return the number of writable bytes left
     */
    public long getRemaining(){
        return remaining;
    }
    
    /**
     * Writes the terminator, forces the OS to update the file
     * and closes the {@code Channel}.
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException {
        // Write terminator
        buffer.putInt(0xFFFFFFFF);
        buffer.force();
        channel.close();
    }
    
    public static class NoMoreRoomInFileException extends RuntimeException {}
}