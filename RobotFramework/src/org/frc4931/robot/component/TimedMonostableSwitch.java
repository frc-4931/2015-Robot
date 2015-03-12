/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.robot.Executor;
import org.frc4931.robot.Executor.Executable;

/**
 * 
 */
// TODO robot.component is for the api, but this doesnt fit robot.hardware either
public class TimedMonostableSwitch implements MonostableSwitch, Executable {
    private final long reset;
    private long offTime;
    private volatile boolean shouldTrigger = false;
    private boolean triggered = false;
    
    private TimedMonostableSwitch(double reset) {
        this.reset = (long)(reset*1000);
    }
    
    @Override
    public void trigger() {
        shouldTrigger = true;
    }
    
    @Override
    public void execute(long time) {
        if(shouldTrigger) {
            triggered = true;
            offTime = time + reset;
            shouldTrigger = false;
        }
        if(triggered && time >= offTime) triggered = false;
    }

    @Override
    public boolean isTriggered() {
        // Should trigger removes the time it takes to process a state change
        // Slightly increases time switch takes to reset
        return triggered || shouldTrigger;
    }
    
    /**
     * Creates a {@link TimedMonostableSwitch} that has been registered with the active {@link Executor}
     * @param reset how long before this {@link MonostableSwitch} resets (seconds)
     * @return a {@link MonostableSwitch}
     */
    public static MonostableSwitch getRegistered(double reset) {
        TimedMonostableSwitch swtch = new TimedMonostableSwitch(reset);
        Executor.getInstance().register(swtch);
        return swtch;
    }
    
    /**
     * Creates a {@link TimedMonostableSwitch} that has not been registered with an {@link Executor}, used if
     * you want to manually update the {@link TimedMonostableSwitch}.
     * @param reset how long before this {@link MonostableSwitch} resets (seconds)
     * @return a {@link TimedMonostableSwitch}
     */
    public static TimedMonostableSwitch getUnregistered(double reset) {
        return new TimedMonostableSwitch(reset);
    }
}
