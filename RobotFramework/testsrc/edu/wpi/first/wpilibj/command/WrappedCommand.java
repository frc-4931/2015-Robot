/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package edu.wpi.first.wpilibj.command;

import junit.framework.AssertionFailedError;


/**
 * A command that wraps another command and monitors what was called on it.
 */
@Deprecated
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
    synchronized boolean run() {
        checkNeverAgain();
        super.run();
        return delegate.run();
    }

    @Override
    protected void initialize() {
        initialized = true;
    }

    @Override
    protected void execute() {
        ++executeCount;
    }

    @Override
    protected boolean isFinished() {
        finished = delegate.isFinished();
        return finished;
    }

    @Override
    protected void end() {
        ended = true;
        delegate.end();
    }

    @Override
    protected void interrupted() {
        checkNeverAgain();
        interrupted = true;
        delegate.interrupted();
    }

    @Override
    public synchronized void cancel() {
        super.cancel();
        delegate.cancel();
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
