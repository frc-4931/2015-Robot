/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.executor;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.frc4931.acommand.CommandID;
import org.frc4931.acommand.CommandState;
import org.frc4931.executor.Executor.Executable;
import org.frc4931.robot.component.Switch;
import org.frc4931.utils.FileUtil;

/**
 * A public interface for recording events.
 */
final class EventLogger implements Executable {
    
    protected static int ESTIMATED_BYTES_PER_SECOND = 16 * 1024;
    
    static interface EventStream {
        void writeChangeInSwitch(int elapsedTimeInMillis, String switchName, boolean currentState);
        void writeMessage(int elapsedTimeInMillis, String message, Throwable error);
        void writeChangeInCommandState(int elapsedTimeInMillis, CommandID commandId, CommandState state);
        void close();
    }
    
    private final List<Executable> switchCheckers = new ArrayList<>();
    private final Set<Switch> registeredSwitches = new HashSet<>();
    private final EventStream stream;

    public EventLogger(ExecutorOptions options) {
        assert options != null;
        String logPrefix = options.getLogFilenamePrefix();
        if (logPrefix != null) {
            stream = new LocalEventStream(options);
        } else {
            stream = new RemoteEventStream(options);
        }
    }

    public void shutdown() {
    }

    @Override
    public void run( int elapsedTimeInMillis ) {
        for (Executable checker : switchCheckers) {
            checker.run(elapsedTimeInMillis);
        }
    }

    /**
     * Registers a {@link Switch} to be monitored so that changes in its {@link Switch#isTriggered() triggered state} are
     * automatically recorded in the event log.
     * 
     * @param name the name of the {@link Switch}; may not be null
     * @param swtch the {@link Switch} to be logged; may not be null
     */
    public synchronized void registerSwitch(String name, Switch swtch) {
        assert name != null;
        assert swtch != null;
        if (!registeredSwitches.contains(swtch)) {
            registeredSwitches.add(swtch);
            String switchName = name;
            switchCheckers.add(new Executable() {
                boolean state = swtch.isTriggered();

                @Override
                public void run(int elapsedTimeInMillis) {
                    if (swtch.isTriggered() != state) {
                        state = !state;
                        // The state has changed, so log it ...
                        recordChangeInSwitch(elapsedTimeInMillis, switchName, state);
                    }
                }
            });
        }
    }

    protected void recordChangeInSwitch(int currentTimeInNanos, String switchName, boolean currentState) {
        stream.writeChangeInSwitch(currentTimeInNanos,switchName,currentState);
    }

    public void record(int currentTimeInNanos, String message, Throwable error) {
        stream.writeMessage(currentTimeInNanos,message,error);
    }

    public void record(int currentTimeInNanos, CommandID commandId, CommandState state) {
        stream.writeChangeInCommandState(currentTimeInNanos,commandId,state);
    }
    
    protected static final class ConsoleEventStream implements EventStream {
        private final EventStream delegate;
        protected ConsoleEventStream(EventStream delegate ) {
            this.delegate = delegate;
        }
        
        private StringBuilder withElapsedTime( int elapsedTime ) {
            float seconds = elapsedTime / 1000;
            int minutes = 0;
            while ( seconds > 60 ) {
                ++minutes;
                seconds -= 60;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(minutes);
            sb.append(":");
            sb.append(seconds);
            sb.append(", ");
            return sb;
        }
        
        @Override
        public void writeChangeInCommandState(int elapsedTimeInMillis, CommandID commandId, CommandState state) {
            try {
                try {
                    delegate.writeChangeInCommandState(elapsedTimeInMillis,commandId,state);
                } finally {
                    StringBuilder sb = withElapsedTime(elapsedTimeInMillis);
                    sb.append(commandId.getName()).append(", ");
                    sb.append(state.toString());
                    System.out.println(sb);
                }
            } catch ( Throwable t ) {
                // Only write this to the console, since it failed when writing to the delegate ...
                System.out.println("Failed to log event:");
                t.printStackTrace(System.out);
            }
        }
        
        @Override
        public void writeChangeInSwitch(int elapsedTimeInMillis, String switchName, boolean currentState) {
            try {
                try {
                    delegate.writeChangeInSwitch(elapsedTimeInMillis,switchName,currentState);
                } finally {
                    StringBuilder sb = withElapsedTime(elapsedTimeInMillis);
                    sb.append(switchName).append(", ");
                    sb.append(currentState?"on":"off");
                    System.out.println(sb);
                }
            } catch ( Throwable t ) {
                // Only write this to the console, since it failed when writing to the delegate ...
                System.out.println("Failed to log event:");
                t.printStackTrace(System.out);
            }
        }
        
        @Override
        public void writeMessage(int elapsedTimeInMillis, String message, Throwable error) {
            try {
                try {
                    delegate.writeMessage(elapsedTimeInMillis,message,error);
                } finally {
                    StringBuilder sb = withElapsedTime(elapsedTimeInMillis);
                    if ( message != null ) {
                        sb.append(message);
                    } else if ( error != null ){
                        sb.append(error.getMessage());
                    }
                    System.out.println(sb);
                    if ( error != null ) error.printStackTrace(System.out);
                }
            } catch ( Throwable t ) {
                // Only write this to the console, since it failed when writing to the delegate ...
                System.out.println("Failed to log event:");
                t.printStackTrace(System.out);
            }
        }
        @Override
        public void close() {
        }
    }
    
    protected static final class LocalEventStream implements EventStream {
        private EventLog writer;
        private final String filePrefix;
        private final long fileSize;
        private final LocalDateTime started;

        protected LocalEventStream( ExecutorOptions options ) {
            filePrefix = options.getLogFilenamePrefix();
            fileSize = options.getTotalEstimatedDurationInSeconds() * ESTIMATED_BYTES_PER_SECOND;
            started = LocalDateTime.now();
        }
        
        private Path newFilename() {
            return FileUtil.namedWithTimestamp(LocalTime.now(), filePrefix, "-events.log");
        }
        
        protected void open() {
            try {
                writer = new EventLog(this::newFilename, fileSize, started);
            } catch (IOException e) {
                System.err.println("Failed to open event log file.");
                e.printStackTrace();
            }
        }
        
        @Override
        public void writeChangeInCommandState(int elapsedTimeInMillis, CommandID commandId, CommandState state) {
            checkRemaining();
            writer.writeChangeInCommandState(elapsedTimeInMillis, commandId, state);
        }
        
        @Override
        public void writeChangeInSwitch(int elapsedTimeInMillis, String switchName, boolean currentState) {
            checkRemaining();
            writer.writeChangeInSwitch(elapsedTimeInMillis, switchName, currentState);
        }
        
        @Override
        public void writeMessage(int elapsedTimeInMillis, String message, Throwable error) {
            checkRemaining();
            writer.writeMessage(elapsedTimeInMillis, message, error);
        }
        @Override
        public void close() {
            writer.close();
        }
        
        private void checkRemaining() {
            if ( writer.remaining() < ESTIMATED_BYTES_PER_SECOND ) {
                close();
                open();
            }
        }
    }
    
    protected static final class RemoteEventStream implements EventStream {
        protected RemoteEventStream( ExecutorOptions options ) {
            
        }
        
        @Override
        public void writeChangeInCommandState(int elapsedTimeInMillis, CommandID commandId, CommandState state) {
        }
        
        @Override
        public void writeChangeInSwitch(int elapsedTimeInMillis, String switchName, boolean currentState) {
        }
        
        @Override
        public void writeMessage(int elapsedTimeInMillis, String message, Throwable error) {
        }
        
        @Override
        public void close() {
        }
    }

}
