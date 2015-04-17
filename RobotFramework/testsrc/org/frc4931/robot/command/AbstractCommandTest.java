/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.WrappedCommand;
import org.frc4931.robot.MockRobot;
import org.frc4931.robot.RobotManager.Systems;
import org.junit.After;
import org.junit.Before;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Base class for all commands that want to test running a command against the robot subsystems. Each test automatically starts
 * out with the robot in the {@link MockRobot#resetToStartingPosition() starting position}, though individual tests may want to
 * start with preconditions to move the robot into a different state and then {@link #runCommandAnd create and run a command} to
 * verify that the robot has responded correctly.
 */
@Deprecated
public abstract class AbstractCommandTest {

    // The subsystems will be registered automatically with the Scheduler ...
    protected final static MockRobot robot = new MockRobot();

    private WrappedCommand lastCommand;

    @Before
    public void beforeEach() {
        robot.resetToStartingPosition();
        robot.enableRobot();
        lastCommand = null;
    }

    @After
    public void afterEach() {
        Scheduler.getInstance().removeAll();
        lastCommand = null;
    }

    /**
     * Run the supplied function a specified number of times.
     * 
     * @param count the number of times to run the function; must be non-negative
     * @param runnable the function to run; may not be null
     */
    protected void repeat(int count, Runnable runnable) {
        assert count >= 0;
        assert runnable != null;
        for (int i = 0; i < count; i++) {
            runnable.run();
        }
    }

    /**
     * Get the mock robot used in the tests.
     * 
     * @return the mock robot; never null
     */
    protected MockRobot robot() {
        return robot;
    }

    /**
     * Create a new command of the appropriate type being tested, given the supplied robot {@link Systems}.
     * 
     * @param systems the systems on the {@link #robot() mock robot}; never null
     * @return the new command; may not be null
     */
    protected abstract Command createCommand(Systems systems);

    /**
     * Create a command using {@link #createCommand(Systems)}, add it to the scheduler, and run the scheduler as many times
     * as required until the command is finished or is interrupted.
     * <p>
     * This method also asserts that once the command is interrupted or finished, that it is never called again. Also, if the
     * command is a {@link OneShotCommand}, then this method asserts that the command is executed only once.
     */
    protected void runCommand() {
        runCommandAnd();
    }

    /**
     * Create a command using {@link #createCommand(Systems)}, add it to the scheduler, and run the scheduler as many times
     * as required until the command is finished or is interrupted. The given function(s) are called each time the command's
     * execute() method is called.
     * <p>
     * This method also asserts that once the command is interrupted or finished, that it is never called again. Also, if the
     * command is a {@link OneShotCommand}, then this method asserts that the command is executed only once.
     * 
     * @param afterEachExecution the function(s) that should be run after each execution of the function until it is complete; may
     *            be null
     */
    protected void runCommandAnd(PostExecution... afterEachExecution) {
        Command command = createCommand(robot.systems());
        assertThat(command).isNotNull();
        lastCommand = new WrappedCommand(command);
        Scheduler.getInstance().add(lastCommand);
        while (!lastCommand.wasEnded() && !lastCommand.wasInterrupted()) {
            Scheduler.getInstance().run();
            if (afterEachExecution != null && afterEachExecution.length != 0) {
                if (lastCommand.wasInitialized() && lastCommand.executedCount() > 1) {
                    // the command was initialized and executed at least once, so call the function(s) ...
                    for (PostExecution function : afterEachExecution) {
                        function.check();
                    }
                }
            }
        }
        assertLastCommandEnded();
        if (command instanceof OneShotCommand) {
            assertLastCommandExecutedExactlyOnce();
        }
        assertLastCommandNotCalledInFuture();
    }

    /**
     * The function interface used in the {@link AbstractCommandTest#runCommandAnd} method.
     */
    @FunctionalInterface
    public static interface PostExecution {
        /**
         * Check any post-conditions after the command was executed just once.
         */
        public void check();
    }

    protected boolean lastCommandInitialized() {
        return lastCommand.wasInitialized();
    }

    protected boolean lastCommandEnded() {
        return lastCommand.wasEnded();
    }

    protected boolean lastCommandInterrupted() {
        return lastCommand.wasInterrupted();
    }

    protected int lastCommandExecutedCount() {
        return lastCommand.executedCount();
    }

    protected void assertLastCommandInitialized() {
        assertThat(lastCommand.wasInitialized()).isEqualTo(true);
    }

    protected void assertLastCommandEnded() {
        assertThat(lastCommand.wasEnded()).isEqualTo(true);
        assertThat(lastCommand.wasInterrupted()).isEqualTo(false);
    }

    protected void assertLastCommandInterrupted() {
        assertThat(lastCommand.wasInterrupted()).isEqualTo(true);
        assertThat(lastCommand.wasEnded()).isEqualTo(false);
    }

    protected void assertLastCommandExecutedExactly(int count) {
        assertThat(lastCommand.executedCount()).isEqualTo(count);
    }

    protected void assertLastCommandExecutedAtLeast(int count) {
        assertThat(lastCommand.executedCount()).isGreaterThanOrEqualTo(count);
    }

    protected void assertLastCommandExecutedExactlyOnce() {
        assertThat(lastCommand.executedCount()).isEqualTo(1);
    }

    protected void assertLastCommandNotExecuted() {
        assertThat(lastCommand.executedCount()).isEqualTo(0);
    }

    protected void assertLastCommandNotCalledInFuture() {
        lastCommand.assertNotCalledInFuture();
    }
}
