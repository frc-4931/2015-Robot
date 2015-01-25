/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.function.IntSupplier;

import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.command.IllegalUseOfCommandException;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * 
 */
public class Logger implements LiveWindowSendable{
    private static final Logger INSTANCE = new Logger();
    public static Logger getInstance(){ return INSTANCE; }
    
    public static final int WRITE_FREQUENCY = (int) (1000.0/30.0); // milliseconds per writes
    public static final int RUNNING_TIME = 200;
    
    private final Stack<IntSupplier> suppliers = new Stack<>();
    private final Stack<String> names = new Stack<>();
    
    private Mode mode = Mode.LOCAL_FILE;
    private volatile boolean running = false;
    
    private long recordLength;
    private MappedWriter writer;
    private Thread writerThread;
    
    /**
     * Starts logging data using the current mode.  Will not do anything if {@link Logger}
     * is already running.
     */
    public void start() {
        if(running) return;
        running = true;

        // If we start in file write mode, we need to initialize the writer
        if(mode == Mode.LOCAL_FILE) {
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
                
                // Initialize the thread that does the actual output
                writerThread = new Thread(()->{
                    while(running) {
                        update();
                        try {
                            Thread.sleep(WRITE_FREQUENCY);
                        } catch (Exception e) {
                        }
                    }
                    if(mode==Mode.LOCAL_FILE){
                        try {
                            writer.close();
                        } catch (IOException e) {
                            System.err.println("Failed to close writer.");
                            e.printStackTrace();
                        }
                    }
                });
                writerThread.setName("writer");
                writerThread.start();
            } catch(IOException e){
                System.err.println("Failed to open log file.");
                e.printStackTrace();
            }
        }
    }
    
    private void update() {
        if(mode==Mode.LOCAL_FILE) {
            if(writer.getRemaining()<recordLength){
                System.err.println("Insuffient space to write next all of next record, closing file");
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            writer.writeInt((int)(Robot.time()/1e6));
            suppliers.forEach((supplier)->writer.writeShort((short)supplier.getAsInt()));
        } else if(mode==Mode.NETWORK_TABLES) {
            SmartDashboard.putData("Logger Data",this);
        }
    }
    
    /**
     * Stops logging data, kills the thread, closes files, and frees resources.
     */
    public void stop() {
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
        else throw new IllegalUseOfCommandException("Cannot change modes while running");
    }

    //TODO Consider registering byte arrays instead of specific integer primitives
    /**
     * Registers the value of the specified {@link IntSupplier} to be logged
     * @param supplier the {@link IntSupplier} of the value to be logged
     * @param name the name of this data point
     */
    public void register(IntSupplier supplier, String name) {
        names.push(name);
        suppliers.push(supplier);
    }

    /**
     * Registers a {@link Switch} to be logged.
     * @param swtch the {@link Switch} to be logged
     * @param name the name of the {@link Switch}
     */
    public void registerSwitch(Switch swtch, String name) {
        names.push(name);
        suppliers.push(()->swtch.isTriggered() ? 1 : 0);
    }
    
    /**
     * Registers a {@link Motor} to be logged.
     * @param motor the {@link Motor} to be logged
     * @param name the name of the {@link Motor}
     */
    public void registerMotor(Motor motor, String name) {
        names.push(name+" speed");
        suppliers.push(()->(short)(motor.getSpeed()*1000));
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
        return "DataLogger";
    }
    
    @Override
    public void startLiveWindowMode() {}
    
    @Override
    public void stopLiveWindowMode() {}
    // End SmartDashboard
    
    public enum Mode{
        LOCAL_FILE, NETWORK_TABLES
    }
    
}
