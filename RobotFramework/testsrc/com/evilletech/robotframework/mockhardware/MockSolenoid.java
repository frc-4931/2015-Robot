package com.evilletech.robotframework.mockhardware;

import com.evilletech.robotframework.api.Solenoid;

/**
 * A test implementation of <code>Solenoid</code> that can be used without any
 * hardware. This class should be instantiated through <code>MockFactory</code>.
 * 
 * @author Zach Anderson
 * @see Solenoid
 * @see MockFactory
 */
public class MockSolenoid implements Solenoid {

    /**
     * Create a stopped solenoid in an extended position.
     * 
     * @return the solenoid
     */
    public static MockSolenoid extended() {
        return new MockSolenoid(Action.OFF, Position.EXTENDED);
    }

    /**
     * Create a stopped solenoid in a retracted position.
     * 
     * @return the solenoid
     */
    public static MockSolenoid retracted() {
        return new MockSolenoid(Action.OFF, Position.RETRACTED);
    }

    /**
     * Create an extending in an unknown position.
     * 
     * @return the solenoid
     */
    public static MockSolenoid extending() {
        return new MockSolenoid(Action.EXTENDING, Position.UNKNOWN);
    }

    /**
     * Create a retracting in an unknown position.
     * 
     * @return the solenoid
     */
    public static MockSolenoid retracting() {
        return new MockSolenoid(Action.RETRACTING, Position.UNKNOWN);
    }

    /**
     * Create a stopped solenoid in an unknown position.
     * 
     * @return the solenoid
     */
    public static MockSolenoid unknown() {
        return new MockSolenoid(Action.OFF, Position.UNKNOWN);
    }

    private Action action = Action.OFF;
    private Position position = Position.UNKNOWN;

    MockSolenoid(Action action, Position position) {
        this.action = action;
        this.position = position;
    }

    @Override
    public void extend() {
        action = Action.EXTENDING;
        position = Position.UNKNOWN;
    }

    @Override
    public void retract() {
        action = Action.RETRACTING;
        position = Position.UNKNOWN;
    }
    
    @Override
    public Action action() {
        return action;
    }
    
    @Override
    public Position position() {
        return position;
    }
    
    public void stop( Position position ) {
        this.position = position;
        action = Action.OFF;
    }
}
