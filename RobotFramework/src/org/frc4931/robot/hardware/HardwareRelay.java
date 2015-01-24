/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.Relay;

import edu.wpi.first.wpilibj.Relay.Value;

/**
 * Wrapper for the WPILib <code>Relay</code>, and which has no delay and thus is only
 * {@link org.frc4931.robot.component.Relay.State#ON} or {@link org.frc4931.robot.component.Relay.State#OFF}.
 * This class cannot be constructed directly, use <code>HardwareFactory</code> to get instances of it.
 * 
 * @author Zach Anderson
 * @see Relay
 * @see Hardware
 * @see edu.wpi.first.wpilibj.Relay
 */
final class HardwareRelay implements Relay {

    private final edu.wpi.first.wpilibj.Relay relay;

    private State state;

    HardwareRelay(edu.wpi.first.wpilibj.Relay relay) {
        this.relay = relay;
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
