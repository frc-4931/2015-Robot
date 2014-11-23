/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package com.evilletech.robotframework.compositehardware;

import com.evilletech.robotframework.api.Solenoid;
import com.evilletech.robotframework.api.Switch;

/**
 * A {@link Solenoid} with physical sensors for position awareness. This
 * class is a composition of one {@link Solenoid} and two {@link Switch}es.
 * 
 * @author Zach Anderson
 */
public final class LimitedSolenoid implements Solenoid {

    private final Solenoid solenoid;
    private final Switch retractSwitch;
    private final Switch extendSwitch;

    private Action action = Action.OFF;
    private Position position = Position.UNKNOWN;

    public LimitedSolenoid(Solenoid solenoid, Switch retractSwitch, Switch extendSwitch) {
        this.solenoid = solenoid;
        this.retractSwitch = retractSwitch;
        this.extendSwitch = extendSwitch;
        checkState();
    }

    @Override
    public Position position() {
        checkState();
        return position;
    }

    @Override
    public Action action() {
        checkState();
        return action;
    }

    @Override
    public void extend() {
        solenoid.extend();
        action = Action.EXTENDING;
        checkState();
    }

    @Override
    public void retract() {
        solenoid.retract();
        action = Action.RETRACTING;
        checkState();
    }

    protected void checkState() {
        switch (action) {
            case EXTENDING:
                // Extend until the extended switch is triggered ...
                if (extendSwitch.isTriggered()) {
                    action = Action.OFF;
                    position = Position.EXTENDED;
                } else if (!solenoid.isExtending()) {
                    // The solenoid is no longer extending ...
                    action = solenoid.action();
                    if (retractSwitch.isTriggered()) {
                        // Somehow it became retracted ...
                        position = Position.RETRACTED;
                    } else {
                        position = solenoid.position();
                    }
                }
                break;
            case RETRACTING:
                // Retract until the extended switch is triggered ...
                if (retractSwitch.isTriggered()) {
                    action = Action.OFF;
                    position = Position.RETRACTED;
                } else if (!solenoid.isRetracting()) {
                    // The solenoid is no longer retracting ...
                    action = solenoid.action();
                    if (extendSwitch.isTriggered()) {
                        // Somehow it became extended ...
                        position = Position.EXTENDED;
                    } else {
                        position = solenoid.position();
                    }
                }
                break;
            case OFF:
                if (position == Position.UNKNOWN) {
                    if (extendSwitch.isTriggered()) {
                        position = Position.EXTENDED;
                    } else if (retractSwitch.isTriggered()) {
                        position = Position.RETRACTED;
                    } else {
                        position = solenoid.position();
                    }
                }
        }
    }
}
