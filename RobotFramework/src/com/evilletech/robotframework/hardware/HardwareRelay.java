/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package com.evilletech.robotframework.hardware;

import com.evilletech.robotframework.api.Relay;

import edu.wpi.first.wpilibj.Relay.Value;

/**
 * Wrapper for the WPILib <code>Relay</code>, and which has no delay and thus is only
 * {@link com.evilletech.robotframework.api.Relay.State#ON} or {@link com.evilletech.robotframework.api.Relay.State#OFF}.
 * This class cannot be constructed directly, use <code>HardwareFactory</code> to get instances of it.
 * 
 * @author Zach Anderson
 * @see Relay
 * @see Hardware
 * @see edu.wpi.first.wpilibj.Relay
 */
class HardwareRelay implements Relay {

    private final edu.wpi.first.wpilibj.Relay relay;

    private State state;

    HardwareRelay(int channel) {
        relay = new edu.wpi.first.wpilibj.Relay(channel);
    }

    @Override
    public void on() {
        relay.set(Value.kOn);
        state = State.ON;
    }

    @Override
    public void off() {
        relay.set(Value.kOff);
        state = State.OFF;
    }

    @Override
    public State state() {
        Value value = relay.get();
        if (value == Value.kForward || value == Value.kOn) {
            state = State.ON;
        } else if (value == Value.kReverse || value == Value.kOff) {
            state = State.OFF;
        } else {
            state = State.UNKOWN;
        }
        return state;
    }
}
