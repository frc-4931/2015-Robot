/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;


/**
 * 
 */
public class Counter {
    private final int max;
    private int value = 0;
    
    public Counter(int max) {
        this.max = max;
    }
    
    public int get() {
        System.out.println(value);
        return value;
    }
    
    public void reset() {
        value = 0;
    }
    
    public void increase() {
        if (value >= max) {
            value = 0;
        }
        value++;
    }
}
