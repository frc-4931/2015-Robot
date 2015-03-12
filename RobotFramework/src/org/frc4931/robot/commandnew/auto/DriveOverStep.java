/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import org.frc4931.robot.commandnew.Command;
import org.frc4931.robot.component.Accelerometer;
import org.frc4931.robot.system.DriveInterpreter;

/**
 * 
 */
public class DriveOverStep extends Command {
    private static final double SPEED = 0.5;
    private static final double ACCELERATION_PULSE_THREASHOLD = 0.6; //g's
    private static final int NUM_PULSES_BEFORE_START = 5;
    private static final int MILLIS_WITHOUT_PULSE_BEFORE_STOP = 650;
    
    private final DriveInterpreter drive;
    private final Accelerometer accel;
    private long lastPeak = 0;
    private int numPulses = 0;
    
    public DriveOverStep(DriveInterpreter drive, Accelerometer accel) {
        super(drive);
        this.drive = drive;
        this.accel = accel;
    }
 
    @Override
    public boolean execute() {
        if(lastPeak == 0) {
            if(Math.abs(accel.getYacceleration() + 1) > ACCELERATION_PULSE_THREASHOLD){
                numPulses++;
            }
            if(numPulses>NUM_PULSES_BEFORE_START){
                lastPeak = System.currentTimeMillis();
            }
            drive.arcade(SPEED, 0);
            return false;
        } else if(System.currentTimeMillis() - lastPeak < MILLIS_WITHOUT_PULSE_BEFORE_STOP) {
            drive.arcade(SPEED, 0);
            return false;
        }
        return true;
    }

    @Override
    public void end() {
        drive.stop();
    }

}
