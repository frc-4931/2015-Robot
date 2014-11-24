/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * A relay is a device that can be turned on and off. Note that a switch has one of 5 possible states:
 * <ol>
 * <li>ON - the switch is in the "on" position;</li>
 * <li>OFF - the switch is in the "off" position;</li>
 * <li>SWITCHING_ON - the switch was in the "off" position but has been changed and is not yet in the "on" position;</li>
 * <li>SWITCHING_OFF - the switch was in the "on" position but has been changed and is not yet in the "off" position; and</li>
 * <li>UNKNOWN - the switch position is not known</li>
 * </ol>
 * <p>
 * Not all Relay implementations use the switching states. Those relays that have no delay will only use ON or OFF. Those
 * relay implementations that may not know their position upon startup may also use the UNKNOWN state.
 * 
 * @author Zach Anderson
 * 
 */
public interface Relay {

    static enum State {
        SWITCHING_ON, ON, SWITCHING_OFF, OFF, UNKOWN
    }

    State state();

    /**
     * Turn on this relay.
     */
    void on();

    /**
     * Turn off this relay.
     */
    void off();

    /**
     * Check whether this relay is on.
     * 
     * @return {@code true} if this relay is on; or {@code false} otherwise
     */
    default boolean isOn() {
        return state() == State.ON;
    }

    /**
     * Check whether this relay is off.
     * 
     * @return {@code true} if this relay is off; or {@code false} otherwise
     */
    default boolean isOff() {
        return state() == State.OFF;
    }

    /**
     * Check if this relay is switching on.
     * 
     * @return {@code true} if this relay is in the process of switching from off to on; or {@code false} otherwise
     */
    default boolean isSwitchingOn() {
        return state() == State.SWITCHING_ON;
    }

    /**
     * Check if this relay is switching off.
     * 
     * @return {@code true} if this relay is in the process of switching from on to off; or {@code false} otherwise
     */
    default boolean isSwitchingOff() {
        return state() == State.SWITCHING_OFF;
    }

}
