/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.executor;

import java.util.concurrent.TimeUnit;

/**
 * 
 */
public interface ExecutorOptions {

    public static final int WRITE_FREQUENCY = (int) (1000.0/30.0); // milliseconds per writes
    public static final int RUNNING_TIME = 200;
    

    int getFrequency();
    
    TimeUnit getFrequencyUnits();
    
    int getTotalEstimatedDurationInSeconds();

    String getLogFilenamePrefix();

}
