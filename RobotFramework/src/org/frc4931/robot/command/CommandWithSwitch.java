/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command;

import org.frc4931.robot.component.Switch;

/**
 * Base command that finishes itself when a switch is triggered.
 */
@Deprecated
public abstract class CommandWithSwitch extends CommandBase {
    private final Switch swtch;

    protected CommandWithSwitch(Switch swtch) {
        this.swtch = swtch;
    }

    protected CommandWithSwitch(String name, Switch swtch) {
        super(name);
        this.swtch = swtch;
    }

    protected CommandWithSwitch(double timeout, Switch swtch) {
        super(timeout);
        this.swtch = swtch;
    }

    protected CommandWithSwitch(String name, double timeout, Switch swtch) {
        super(name, timeout);
        this.swtch = swtch;
    }

    @Override
    protected final boolean isFinished() {
        return swtch.isTriggered();
    }
}
