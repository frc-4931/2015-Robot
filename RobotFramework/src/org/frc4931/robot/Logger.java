/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.IntSupplier;

import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Switch;
import org.frc4931.utils.Lifecycle;

import edu.wpi.first.wpilibj.command.IllegalUseOfCommandException;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * 
 */
public class Logger implements Lifecycle {
    private static final Logger INSTANCE = new Logger();
    public static Logger getInstance(){ return INSTANCE; }
    
    public static final int WRITE_FREQUENCY = (int) (1000.0/30.0); // milliseconds per writes
    public static final int RUNNING_TIME = 200;
    
    private final ArrayList<IntSupplier> suppliers = new ArrayList<>();
    private final ArrayList<String> names = new ArrayList<>();
    
    private Mode mode = Mode.LOCAL_FILE;
    
    private volatile boolean running = false;
    private Thread loggerThread;
    private LogWriter logger;
    
    /**
     * Starts logging data using the current mode.  Will not do anything if {@link Logger}
     * is already running.
     */
    @Override
    public void startup() {
        if(running) return;
        running = true;

        // Initialize appropriate writer
        if(mode == Mode.LOCAL_FILE) {
            logger = new LocalLogWriter(names, suppliers);
        }else{
            assert mode == Mode.NETWORK_TABLES;
            logger = new RemoteLogWriter(names, suppliers);
        }
        
         // Initialize the thread that does the actual output
        loggerThread = new Thread(()->{
            while(running) {
                logger.write();
                try {
                    Thread.sleep(WRITE_FREQUENCY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.close();
        });
        loggerThread.setName("writer");
        loggerThread.start();
    }
    
    /**
     * Stops logging data, kills the thread, closes files, and frees resources.
     */
    @Override
    public void shutdown() {
        running = false;
    }
    
    /**
     * Sets the mode of this {@link Logger}. Will throw an {@link IllegalUseOfCommandException} if {@link Logger}
     * is still running.
     * <li>{@link Mode#LOCAL_FILE} - The data is logged to a csv stored locally on the RIO.
     * <li>{@link Mode#NETWORK_TABLES} - The data is written to the {@link SmartDashboard}, no data is stored locally.
     * @param mode the new {@link Mode} of this {@link Logger}
     */
    public void setMode(Mode mode){
        if(!running)
            this.mode = mode;
        else throw new UnsupportedOperationException("Cannot change modes while running");
    }

    //TODO Consider registering byte arrays instead of specific integer primitives
    /**
     * Registers the value of the specified {@link IntSupplier} to be logged
     * @param supplier the {@link IntSupplier} of the value to be logged
     * @param name the name of this data point
     */
    public void register(IntSupplier supplier, String name) {
        if(running) throw new UnsupportedOperationException("Cannot register while running");
        names.add(name);
        suppliers.add(supplier);
    }

    /**
     * Registers a {@link Switch} to be logged.
     * @param swtch the {@link Switch} to be logged
     * @param name the name of the {@link Switch}
     */
    public void registerSwitch(Switch swtch, String name) {
        if(running) throw new UnsupportedOperationException("Cannot register while running");
        names.add(name);
        suppliers.add(()->swtch.isTriggered() ? 1 : 0);
    }
    
    /**
     * Registers a {@link Motor} to be logged.
     * @param motor the {@link Motor} to be logged
     * @param name the name of the {@link Motor}
     */
    public void registerMotor(Motor motor, String name) {
        if(running) throw new UnsupportedOperationException("Cannot register while running");
        names.add(name+" speed");
        suppliers.add(()->(short)(motor.getSpeed()*1000));
    }
    
    /**
     * Combines an array of {@code boolean}s into a single {@code short}.
     * @param values the {@code boolean}s to be combined
     * @return a {@code short} where the value of each byte represents a single boolean
     */
    public static short bitmask(boolean[] values){
        if(values.length>15)
            throw new IllegalArgumentException("Cannot combine more than 15 booleans");
        short value = 0;
        for(int i = 0; i<values.length;i++)
            value = (short)(value | ((values[i] ? 1 : 0) << i));
        return value;
    }

    public enum Mode{
        LOCAL_FILE, NETWORK_TABLES
    }
    
    public static interface LogWriter {
        /**
         * Writes the current status of the robot to a log.
         */
        public void write();
        
        /**
         * Frees the resources used by this {@link LogWriter}.
         */
        public void close();
    }
    
    public static class LocalLogWriter implements LogWriter {
        private final ArrayList<IntSupplier> suppliers;
       
        private long recordLength;
        private MappedWriter writer;
        
        public LocalLogWriter(ArrayList<String> names,
                               ArrayList<IntSupplier> suppliers){
            this.suppliers = suppliers;
            try {
                // Estimate minimum file size needed to run at WRITE_FREQUENCY for RUNNING_TIME
                // Reciprocal of WRITE_FREQUENCY is frames per millisecond, time is seconds
                int numWrites = (int) ((1.0 / WRITE_FREQUENCY) * (RUNNING_TIME * 1000));
                
                // Infrastructure for variable element length
                recordLength = Integer.BYTES;
                for(IntSupplier supplier : suppliers)
                    recordLength += Short.BYTES;
                
                long minSize = numWrites * recordLength;
                
                writer = new MappedWriter("robot.log", minSize + 1024); // Extra KB of room
                
                // Write the header
                writer.write("log".getBytes());
                
                // Write the number of elements
                writer.write((byte)(suppliers.size()+1));
                
                // Write the size of each element (Infrastructure for variable length element)
                writer.write((byte)Integer.BYTES);
                
                for(IntSupplier supplier : suppliers)
                    writer.write((byte)Short.BYTES);
                
                // Write length of each element name and the name itself
                writer.write((byte)4);
                writer.write(("Time").getBytes());
                
                for(String name : names){
                    writer.write((byte)name.length());
                    writer.write(name.getBytes());
                }
            } catch(IOException e){
                System.err.println("Failed to open log file.");
                e.printStackTrace();
            }
        }
        
        @Override
        public void write() {
            if(writer.getRemaining()<recordLength){
                System.err.println("Insuffient space to write next all of next record, closing file");
                writer.close();
            }
            writer.writeInt((int)(Robot.time()/1e6));
            suppliers.forEach((supplier)->writer.writeShort((short)supplier.getAsInt()));
        }
        
        @Override
        public void close() {
            writer.close();
        }
        
    }

    public static class RemoteLogWriter implements LogWriter, LiveWindowSendable {
        private final ArrayList<String> names;
        private final ArrayList<IntSupplier> suppliers;
        
        public RemoteLogWriter(ArrayList<String> names,
                               ArrayList<IntSupplier> suppliers){
            this.names = names;
            this.suppliers = suppliers;
        }

        @Override
        public void write() {
            SmartDashboard.putData("Logger", this);
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
            while(nameIterator.hasNext()&&supplierIterator.hasNext())
                table.putNumber(nameIterator.next(), supplierIterator.next().getAsInt());
        }

        @Override
        public String getSmartDashboardType() {
            return "DataLupdate();ogger";
        }
        
        @Override
        public void startLiveWindowMode() {}
        
        @Override
        public void stopLiveWindowMode() {}
        
        @Override
        public void close() {}
    }
}
