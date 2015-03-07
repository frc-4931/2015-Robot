/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.executor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.frc4931.acommand.CommandID;
import org.frc4931.acommand.CommandState;

/**
 * 
 */
public class EventLog implements AutoCloseable {

    private static final byte VERSION = 1;

    private static final byte HEADER = 1;
    private static final byte SWITCH_EVENT = 2;
    private static final byte MESSAGE_EVENT = 3;
    private static final byte EXCEPTION_EVENT = 4;
    private static final byte COMMAND_EVENT = 5;
    private static final byte COMMAND_TYPE = 6;

    private final File outFile;
    private final FileChannel channel;
    private final MappedByteBuffer buffer;
    private final long capacity;
    private final Map<Integer, CommandID> commandsById = new HashMap<>();

    /**
     * Constructs a new {@link MappedWriter} to write to the file at the specified path.
     * 
     * @param pathSupplier the supplier of the {@link Path path} that references the file to which this writer will write; may not
     *            be null
     * @param size the maximum number of bytes that can be written
     * @param startTime the start time of the match
     * @throws IOException if an I/O error occurs
     */
    public EventLog(Supplier<Path> pathSupplier, long size, LocalDateTime startTime) throws IOException {
        outFile = pathSupplier.get().toFile();
        channel = new RandomAccessFile(outFile, "rw").getChannel();
        capacity = size;
        buffer = channel.map(MapMode.READ_WRITE, 0, capacity);
        // Write the header ...
        writeHeader(startTime);
    }

    private void writeHeader(LocalDateTime startTime) {
        buffer.put(HEADER);
        // Write the version of this format ...
        buffer.put(VERSION);
        // Write the starting date in seconds since epoch ...
        buffer.putLong(startTime.toEpochSecond(ZonedDateTime.now().getOffset()));
    }

    private void writeString(String str) {
        if (str.isEmpty()) {
            buffer.putShort((short) 0);
        } else {
            buffer.putShort((short) str.length());
            buffer.put(str.getBytes(StandardCharsets.UTF_8));
        }
    }

    // private String readString() {
    // int len = buffer.getShort();
    // if ( len == 0 ) return "";
    // byte[] bytes = new byte[len];
    // buffer.get(bytes, 0, len);
    // return new String(bytes, 0, len);
    // }

    private void writeBoolean(boolean value) {
        buffer.put((byte) (value ? 1 : 0));
    }

    // private boolean readBoolean() {
    // return buffer.get() == 1;
    // }

    public void writeChangeInSwitch(int elapsedTimeInMillis, String switchName, boolean currentState) {
        buffer.put(SWITCH_EVENT);
        buffer.putInt(elapsedTimeInMillis);
        writeString(switchName);
        writeBoolean(currentState);
    }

    public void writeMessage(int elapsedTimeInMillis, String message, Throwable error) {
        if (error == null) {
            buffer.put(MESSAGE_EVENT);
            buffer.putInt(elapsedTimeInMillis);
            writeString(message);
        } else {
            buffer.put(EXCEPTION_EVENT);
            buffer.putInt(elapsedTimeInMillis);
            writeString(message);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            error.printStackTrace(pw);
            writeString(sw.toString()); // stack trace as a string
        }
    }

    public void writeChangeInCommandState(int elapsedTimeInMillis, CommandID commandId, CommandState state) {
        if (!commandsById.containsKey(commandId.asInt())) {
            commandsById.put(commandId.asInt(), commandId);
            buffer.put(COMMAND_TYPE);
            buffer.putInt(commandId.asInt());
            writeString(commandId.getName());
        }
        buffer.put(COMMAND_EVENT);
        buffer.putInt(elapsedTimeInMillis);
        buffer.putInt(commandId.asInt());
        buffer.put((byte) state.ordinal());
    }
    
    public int remaining() {
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
