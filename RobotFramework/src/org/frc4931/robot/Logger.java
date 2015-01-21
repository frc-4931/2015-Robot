/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.State;
import java.nio.ShortBuffer;
import java.util.Iterator;
import java.util.Stack;
import java.util.function.IntSupplier;

import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * 
 */
public class Logger implements LiveWindowSendable{
    private static final Logger INSTANCE = new Logger();
    public static Logger getInstance(){ return INSTANCE; }
    
    private final Stack<IntSupplier> suppliers = new Stack<>();
    private final Stack<String> names = new Stack<>();
    
    private Thread updateThread;
    private volatile boolean running = false;
    private ShortBuffer buffer;
    private Mode mode = Mode.NETWORK_TABLES;
    
    
    //File Output
    private static final short FRAMES_PER_FLUSH = 15;
    private short sinceFlush = 0;
    private BufferedWriter writer;
    private Thread writerThread;
    
    public void start() {
        if(running) return;
        running = true;
        updateThread = new Thread(()->{while(running) update();});
        updateThread.setName("logger");
        updateThread.start();
    }
    
    public void stop() {
        running = false;
    }
    
    public void setMode(Mode mode){
        if(mode == Mode.LOCAL_FILE) {
            if(writer==null) {
                try {
                    writer = new BufferedWriter(new FileWriter("Log.csv"));
                    for(String name : names){
                        writer.write(name+", ");
                    }
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                writerThread = new Thread(()->{
                    while(running) {
                        // Wait for permission before we start writing
                        synchronized(this){
                            try {
                                wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        for(int i = 0; i < buffer.limit();i++){
                            try {
                                writer.write(buffer.get(i)+", ");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            writer.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sinceFlush++;
                        if(sinceFlush==FRAMES_PER_FLUSH) {
                            try {
                                writer.flush();
                                sinceFlush=0;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                writerThread.setName("writer");
                writerThread.start();
            }
        }
    }
    
    private void update() {
        if(mode==Mode.LOCAL_FILE){
            // Put all of the data into a buffer
            buffer.clear();
            suppliers.forEach((supplier)->buffer.put((short) supplier.getAsInt()));

            // Tell the writer that the next buffer is ready
            synchronized(writerThread) {
                // If the writer is waiting, flip the buffer and then tell writer to write
                // The buffer is flipped before the lock is released to writerThread
                // If not, we start updating the next buffer and this frame is lost
                if(writerThread.getState()==State.WAITING) {
                    buffer.flip();
                    writerThread.notify();
                }
            }
                
        }else if(mode==Mode.NETWORK_TABLES){
            SmartDashboard.putData("Logger Data",this);
        }
        
    }

    public void registerSwitch(Switch swtch, String name) {
        names.push(name);
        suppliers.push(()->swtch.isTriggered() ? 1 : 0);
    }
    
    public void registerMotor(Motor motor, String name) {
        names.push(name+" speed");
        suppliers.push(()->(short)(motor.getSpeed()*1000));
    }
    
    
    public static short bitmask(boolean[] values){
        if(values.length>15)
            throw new IllegalArgumentException("Cannot combine more than 15 booleans");
        short value = 0;
        for(int i = 0; i<values.length;i++)
            value = (short)(value | ((values[i] ? 1 : 0) << i));
        return value;
    }

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
    
    public enum Mode{
        LOCAL_FILE, NETWORK_TABLES
    }
    
}
