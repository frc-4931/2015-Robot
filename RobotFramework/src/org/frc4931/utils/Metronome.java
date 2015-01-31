/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.utils;

import java.util.concurrent.TimeUnit;

/**
 * A class that can be used to perform an action at a regular interval. To use, set up a {@code Metronome}
 * instance and perform a repeated event (perhaps using a loop), calling {@link #pause()} before each event.
 */
public final class Metronome {

    private final long periodInMillis;
    private long next;
    private long pauseTime;
    
    public Metronome( long period, TimeUnit unit ) {
        assert period >= 0;
        periodInMillis = unit.toMillis(period);
        next = System.currentTimeMillis();
    }
    
    /**
     * Pause until the next click of the metronome.
     * @return true if the pause completed normally, or false if it was interrupted
     */
    public boolean pause() {
        next = next + periodInMillis;
        pauseTime = next - System.currentTimeMillis();
        if ( pauseTime > 0 ) {
            try {
                Thread.sleep(pauseTime);
            } catch ( InterruptedException e ) {
                // Somebody interrupted our thread while we were sleeping, so handle it and continue ...
                Thread.interrupted();
                return false;
            }
        }
        return true;
    }
}
