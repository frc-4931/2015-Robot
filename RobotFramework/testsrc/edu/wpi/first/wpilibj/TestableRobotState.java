/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.command.Scheduler;
import org.frc4931.robot.MockRobot;


/**
 * Some gritty code that manipulates some WPILib components and replaces the implementation of others so that the {@link Scheduler}
 * can be used within unit tests. This class is already referenced by {@link MockRobot} so that all initialization is done before
 * the robot's subsystems are needed.
 * 
 * @see MockRobot
 */
public class TestableRobotState extends RobotState {
    
    public static double MATCH_DURATION_IN_SECONDS = 180.0;
    
    public static enum RobotMode {
        AUTONOMOUS, TELEOP, TEST;
    }

    protected static long startTime;
    protected static RobotMode robotMode = RobotMode.TELEOP;
    protected static boolean enabled = true;
    
    static {
        RobotState.SetImplementation(new RobotState.Interface() {
            @Override
            public boolean isAutonomous() {
                return robotMode == RobotMode.AUTONOMOUS;
            }
            @Override
            public boolean isDisabled() {
                return !enabled;
            }
            @Override
            public boolean isEnabled() {
                return enabled;
            }
            @Override
            public boolean isOperatorControl() {
                return true;
            }
            @Override
            public boolean isTest() {
                return robotMode == RobotMode.TEST;
            }
        });
        HLUsageReporting.SetImplementation(new HLUsageReporting.Interface() {
            @Override
            public void reportScheduler() {
                // do nothing for now
            }

            @Override
            public void reportPIDController(int num) {
                // do nothing for now
            }

            @Override
            public void reportSmartDashboard() {
                // do nothing for now
            }
        });
        Timer.SetImplementation(new Timer.StaticInterface() {
            
            @Override
            public Timer.Interface newTimer() {
                return new Timer.Interface() {
                    private long m_startTime = getMsClock();
                    private double m_accumulatedTime = 0.0;
                    private boolean m_running = false;

                    private long getMsClock() {
                        return (long)(getFPGATime() / 1000.0);
                    }

                    /**
                     * Get the current time from the timer. If the clock is running it is derived from
                     * the current system clock the start time stored in the timer class. If the clock
                     * is not running, then return the time when it was last stopped.
                     *
                     * @return Current time value for this timer in seconds
                     */
                    @Override
                    public synchronized double get() {
                        if (m_running) {
                            return ((getMsClock() - m_startTime) + m_accumulatedTime) / 1000.0;
                        }
                            return m_accumulatedTime;
                    }

                    /**
                     * Reset the timer by setting the time to 0.
                     * Make the timer startTime the current time so new requests will be relative now
                     */
                    @Override
                    public synchronized void reset() {
                        m_accumulatedTime = 0;
                        m_startTime = getMsClock();
                    }

                    /**
                     * Start the timer running.
                     * Just set the running flag to true indicating that all time requests should be
                     * relative to the system clock.
                     */
                    @Override
                    public synchronized void start() {
                        m_startTime = getMsClock();
                        m_running = true;
                    }

                    /**
                     * Stop the timer.
                     * This computes the time as of now and clears the running flag, causing all
                     * subsequent time requests to be read from the accumulated time rather than
                     * looking at the system clock.
                     */
                    @Override
                    public synchronized void stop() {
                        final double temp = get();
                        m_accumulatedTime = temp;
                        m_running = false;
                    }

                    /**
                     * Check if the period specified has passed and if it has, advance the start
                     * time by that period. This is useful to decide if it's time to do periodic
                     * work without drifting later by the time it took to get around to checking.
                     *
                     * @param period The period to check for (in seconds).
                     * @return If the period has passed.
                     */
                    @Override
                    public synchronized boolean hasPeriodPassed(double period) {
                        if (get() > period) {
                            // Advance the start time by the period.
                            // Don't set it to the current time... we want to avoid drift.
                            m_startTime += period * 1000;
                            return true;
                        }
                        return false;
                    }
                };
            }
            
            /**
             * Return the approximate match time
             * The FMS does not send an official match time to the robots, but does send an approximate match time.
             * The value will count down the time remaining in the current period (auto or teleop).
             * Warning: This is not an official time (so it cannot be used to dispute ref calls or guarantee that a function
             * will trigger before the match ends)
             * The Practice Match function of the DS approximates the behaviour seen on the field.
             * @return Time remaining in current match period (auto or teleop) in seconds
             */
            @Override
            public double getMatchTime() {
                return MATCH_DURATION_IN_SECONDS;
            }
            
            @Override
            public double getFPGATimestamp() {
                return getFPGATime();
            }
            
            @Override
            public void delay(double seconds) {
                try {
                    Thread.sleep((long) (seconds * 1e3));
                } catch (final InterruptedException e) {
                }
            }
        });
    }
    
    /**
     * Return the system clock time in seconds. Return the time from the
     * FPGA hardware clock in seconds since the FPGA started.
     *
     * @return Robot running time in seconds.
     */
    protected static double getFPGATime() {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }
    
    public static void resetMatchTime() {
        startTime = System.currentTimeMillis();
    }
    
    public static void enableRobot() {
        resetMatchTime();
        enabled = true;
    }

    public static void disableRobot() {
        enabled = false;
    }
    
    public static void beginTeleopMode() {
        robotMode = RobotMode.TELEOP;
    }

    public static void beginAutonomousMode() {
        robotMode = RobotMode.AUTONOMOUS;
    }
    
    public static void beginTestMode() {
        robotMode = RobotMode.TEST;
    }

}
