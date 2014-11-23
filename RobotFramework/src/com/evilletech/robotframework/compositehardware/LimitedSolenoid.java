package com.evilletech.robotframework.compositehardware;

import com.evilletech.robotframework.api.Solenoid;
import com.evilletech.robotframework.api.Switch;

/**
 * A <code>Solenoid</code> with physical sensors for position awareness. This
 * class is a composition of one <code>Solenoid</code> and two <code>Switch</code>es.
 * 
 * @author Zach Anderson
 * 
 */
public class LimitedSolenoid implements Solenoid {

    private final Solenoid solenoid;
    private final Switch retractSwitch;
    private final Switch extendSwitch;

    private Action action = Action.OFF;
    private Position position = Position.UNKNOWN;

    public LimitedSolenoid(Solenoid solenoid, Switch retractSwitch,
            Switch extendSwitch) {
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
        switch( action ) {
            case EXTENDING:
                // Extend until the extended switch is triggered ...
                if ( extendSwitch.isTriggered() ) {
                    action = Action.OFF;
                    position = Position.EXTENDED;
                } else if ( !solenoid.isExtending() ) {
                    // The solenoid is no longer extending ...
                    action = Action.OFF;
                    if ( retractSwitch.isTriggered() ) {
                        // Somehow it became retracted ...
                        position = Position.RETRACTED;
                    } else {
                        position = solenoid.position();
                    }
                }
                break;
            case RETRACTING:
                // Retract until the extended switch is triggered ...
                if ( retractSwitch.isTriggered() ) {
                    action = Action.OFF;
                    position = Position.RETRACTED;
                } else if ( !solenoid.isRetracting() ) {
                    // The solenoid is no longer retracting ...
                    action = Action.OFF;
                    if ( extendSwitch.isTriggered() ) {
                        // Somehow it became extended ...
                        position = Position.EXTENDED;
                    } else {
                        position = solenoid.position();
                    }
                }
                break;
            case OFF:
                action = Action.OFF;
                if ( position == Position.UNKNOWN ) {
                    if ( extendSwitch.isTriggered() ) {
                        position = Position.EXTENDED;
                    } else if ( retractSwitch.isTriggered() ) {
                        position = Position.RETRACTED;
                    } else {
                        position = solenoid.position();
                    }
                }
        }
    }
}
