/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.executor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

/**
 * Provides easy methods for creation and writing to of a memory mapped file.
 */
public final class MappedWriter implements AutoCloseable {
    
    private final File outFile;
    private final FileChannel channel;
    private final MappedByteBuffer buffer;
    private final long capacity;
    
    /**
     * Constructs a new {@link MappedWriter} to write to the file at the specified path.
     * @param pathSupplier the supplier of the {@link Path path} that references the file to which this writer will write; may not be null
     * @param size the maximum number of bytes that can be written
     * @throws IOException if an I/O error occurs
     */
    public MappedWriter(Supplier<Path> pathSupplier, long size) throws IOException{
        outFile = pathSupplier.get().toFile();
        channel = new RandomAccessFile(outFile, "rw").getChannel();
        capacity = size;
        buffer = channel.map(MapMode.READ_WRITE, 0, capacity);
    }
    
    /**
     * Constructs a new {@link MappedWriter} to write to the specified file.
     * @param filename the path to the file to be written to
     * @param size the maximum number of bytes that can be written
     * @throws IOException if an I/O error occurs
     */
    public MappedWriter(String filename, long size) throws IOException{
        this(()->Paths.get(filename),size);
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
    @Override
    public void close(){
        try {
            // Write terminator
            buffer.putInt(0xFFFFFFFF);
        } finally {
            try {
                // And always force the buffer ...
                buffer.force();
            } finally {
                try{
                    // And always close the channel ...
                    channel.close();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to close channel",e);
                }
            }
        }
    }
}
