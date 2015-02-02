/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.executor;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntSupplier;

import org.frc4931.executor.Executor.Executable;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Switch;
import org.frc4931.utils.FileUtil;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The metrics logging service.
 */
class MetricLogger implements Executable {

    private final List<IntSupplier> suppliers = new ArrayList<>();
    private final List<String> names = new ArrayList<>();
    private final DataLogWriter logger;

    protected MetricLogger(ExecutorOptions options) {
        assert options != null;
        String logPrefix = options.getLogFilenamePrefix();
        if (logPrefix != null) {
            logger = new LocalDataLogWriter(options, names, suppliers);
        } else {
            logger = new RemoteDataLogWriter(options, names, suppliers);
        }
    }

    @Override
    public void run(int elapsedTimeInMillis) {
        logger.write(elapsedTimeInMillis);
    }

    /**
     * Stops logging data, kills the thread, closes files, and frees resources.
     */
    public synchronized void shutdown() {
        logger.close();
    }

    /**
     * Registers the value of the specified {@link IntSupplier} to be logged
     * 
     * @param name the name of this data point
     * @param supplier the {@link IntSupplier} of the value to be logged
     * @throws IllegalArgumentException if the {@code supplier} parameter is null
     */
    public synchronized void register(String name, IntSupplier supplier) {
        if (supplier == null) throw new IllegalArgumentException("The supplier may not be null");
        names.add(name);
        suppliers.add(supplier);
    }

    /**
     * Registers a {@link Switch} to be logged.
     * 
     * @param name the name of the {@link Switch}
     * @param swtch the {@link Switch} to be logged
     * @throws IllegalArgumentException if the {@code swtch} parameter is null
     */
    public synchronized void registerSwitch(String name, Switch swtch) {
        if (swtch == null) throw new IllegalArgumentException("The supplier may not be null");
        names.add(name);
        suppliers.add(() -> swtch.isTriggered() ? 1 : 0);
    }

    /**
     * Registers a {@link Motor} to be logged.
     * 
     * @param name the name of the {@link Motor}
     * @param motor the {@link Motor} to be logged
     * @throws IllegalArgumentException if the {@code motor} parameter is null
     */
    public synchronized void registerMotor(String name, Motor motor) {
        names.add(name + " speed");
        suppliers.add(() -> (short) (motor.getSpeed() * 1000));
    }

    /**
     * Combines an array of {@code boolean}s into a single {@code short}.
     * 
     * @param values the {@code boolean}s to be combined
     * @return a {@code short} where the value of each byte represents a single boolean
     */
    public static short bitmask(boolean[] values) {
        if (values.length > 15)
            throw new IllegalArgumentException("Cannot combine more than 15 booleans");
        short value = 0;
        for (int i = 0; i < values.length; i++)
            value = (short) (value | ((values[i] ? 1 : 0) << i));
        return value;
    }

    protected static interface DataLogWriter extends AutoCloseable {
        /**
         * Writes the current status of the robot to a log.
         * @param elapsedTimeInMillis the elapsed match time in milliseconds
         */
        public void write( int elapsedTimeInMillis);

        /**
         * Frees the resources used by this {@link DataLogWriter}.
         */
        @Override
        public void close();
    }

    protected static class LocalDataLogWriter implements DataLogWriter {
        private final List<IntSupplier> suppliers;
        private final List<String> names;
        private final ExecutorOptions options;
        private long recordLength;
        private MappedWriter writer;

        public LocalDataLogWriter(ExecutorOptions options, List<String> names,
                List<IntSupplier> suppliers) {
            this.suppliers = suppliers;
            this.names = names;
            this.options = options;
        }
        
        private Path newFilename() {
            return FileUtil.namedWithTimestamp(LocalTime.now(), options.getLogFilenamePrefix(), "-metrics.log");
        }
        
        protected void open() {
            try {
                // Estimate minimum file size needed to run at WRITE_FREQUENCY for RUNNING_TIME
                // Reciprocal of WRITE_FREQUENCY is frames per millisecond, time is seconds
                long frequencyInMillis = options.getFrequencyUnits().toMillis(options.getFrequency());
                long runningTime = options.getTotalEstimatedDurationInSeconds();
                int numWrites = (int) ((1.0 / frequencyInMillis) * (runningTime * 1000));

                // Infrastructure for variable element length
                recordLength = Integer.BYTES;
                recordLength += (Short.BYTES * suppliers.size());

                long minSize = numWrites * recordLength;

                writer = new MappedWriter(this::newFilename, minSize + 1024); // Extra KB of room

                // Write the header
                writer.write("log".getBytes());

                // Write the number of elements
                writer.write((byte) (suppliers.size() + 1));

                // Write the size of each element (Infrastructure for variable length element)
                writer.write((byte) Integer.BYTES);

                for (IntSupplier supplier : suppliers) {
                    assert supplier != null;
                    writer.write((byte) Short.BYTES);
                }

                // Write length of each element name and the name itself
                writer.write((byte) 4);
                writer.write(("Time").getBytes());

                for (String name : names) {
                    writer.write((byte) name.length());
                    writer.write(name.getBytes());
                }
            } catch (IOException e) {
                System.err.println("Failed to open metrics log file.");
                e.printStackTrace();
            }
        }

        @Override
        public void write( int elapsedTimeInMillis) {
            if (writer.getRemaining() < recordLength) {
                try {
                    writer.close();
                    open();
                } catch ( Throwable t ) {
                    System.err.println("Insuffient space to write next all of next record, and error while closing file and opening new file");
                    t.printStackTrace(System.err);
                }
            }
            writer.writeInt(elapsedTimeInMillis);
            suppliers.forEach((supplier) -> writer.writeShort((short) supplier.getAsInt()));
        }

        @Override
        public void close() {
            writer.close();
        }

    }

    protected static class RemoteDataLogWriter implements DataLogWriter, LiveWindowSendable {
        private final List<String> names;
        private final List<IntSupplier> suppliers;

        public RemoteDataLogWriter(ExecutorOptions options, List<String> names,
                List<IntSupplier> suppliers) {
            this.names = names;
            this.suppliers = suppliers;
        }

        @Override
        public void write( int elapsedTimeInMillis) {
            SmartDashboard.putData("Metrics", this);
        }

        // SmartDashboard stuff
        private ITable table;

        @Override
        public void initTable(ITable subtable) {
            table = subtable;
            updateTable();
        }

        @Override
        public ITable getTable() {
            return table;
        }

        @Override
        public void updateTable() {
            Iterator<String> nameIterator = names.iterator();
            Iterator<IntSupplier> supplierIterator = suppliers.iterator();
            while (nameIterator.hasNext() && supplierIterator.hasNext())
                table.putNumber(nameIterator.next(), supplierIterator.next().getAsInt());
        }

        @Override
        public String getSmartDashboardType() {
            return "DataLupdate();ogger";
        }

        @Override
        public void startLiveWindowMode() {
        }

        @Override
        public void stopLiveWindowMode() {
        }

        @Override
        public void close() {
        }
    }
}
