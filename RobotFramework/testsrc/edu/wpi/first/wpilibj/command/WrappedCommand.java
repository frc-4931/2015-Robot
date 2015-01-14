package edu.wpi.first.wpilibj.command;

import junit.framework.AssertionFailedError;


/**
 * A command that wraps another command and monitors what was called on it.
 */
public class WrappedCommand extends Command {

    private final Command delegate;
    private boolean neverAgain = false;
    private boolean initialized = false;
    private boolean ended = false;
    private boolean interrupted = false;
    private boolean finished = false;
    private int executeCount = 0;

    public WrappedCommand(Command command) {
        this.delegate = command;
    }

    @Override
    protected void initialize() {
        checkNeverAgain();
        initialized = true;
        delegate.initialize();
    }

    @Override
    protected void execute() {
        checkNeverAgain();
        ++executeCount;
        delegate.execute();
    }

    @Override
    protected boolean isFinished() {
        checkNeverAgain();
        finished = delegate.isFinished();
        return finished;
    }

    @Override
    protected void end() {
        checkNeverAgain();
        ended = true;
        delegate.end();
    }

    @Override
    protected void interrupted() {
        checkNeverAgain();
        interrupted = true;
        delegate.interrupted();
    }
    
    protected void checkNeverAgain() {
        if ( neverAgain ) {
            throw new AssertionFailedError("Unexpectedly called a method on " + getClass().getSimpleName() + ": " + delegate);
        }
    }

    public void reset() {
        neverAgain = false;
        initialized = false;
        ended = false;
        interrupted = false;
        finished = false;
        executeCount = 0;
    }

    public boolean wasInitialized() {
        return initialized;
    }

    public boolean wasEnded() {
        return ended;
    }

    public boolean wasInterrupted() {
        return interrupted;
    }

    public int executedCount() {
        return executeCount;
    }
    
    public void assertNotCalledInFuture() {
        neverAgain = true;
    }
}
