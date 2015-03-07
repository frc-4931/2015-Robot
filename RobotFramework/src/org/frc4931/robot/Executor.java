/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import org.frc4931.utils.Metronome;

/**
 * 
 */
public class Executor {
    private static final Executor INSTANCE = new Executor();
    public static Executor getInstance() { return INSTANCE; }
    
    private final Metronome met = new Metronome(5, TimeUnit.MILLISECONDS);
    private final Queue<Executable> queue = new LinkedList<>();
    private volatile boolean running;
    
    public void start() {
        Thread t = new Thread(this::update);
        t.setName("Executor");
        running = true;
        t.start();
    }
    
    private void update() {
        while(running) {
            if(!queue.isEmpty()) {
                Executable u = queue.poll();
                u.execute(RobotManager.time());
                queue.offer(u);
            }
            met.pause();
        }
    }
    
    public void register(Executable r) {
        queue.offer(r);
    }
    
    public static interface Executable {
        public void execute(long time);
    }
}
