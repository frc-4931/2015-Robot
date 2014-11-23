package com.evilletech.robotframework.mockhardware;

import com.evilletech.robotframework.api.Relay;

/**
 * A test implementation of <code>Relay</code> that does not require any
 * hardware to use.
 * 
 * @author Zach Anderson
 * @see Relay
 */
public class MockRelay implements Relay {
    
    /**
     * Create a mock relay that is in the ON state.
     * 
     * @return the mock relay; never null
     */
    public static MockRelay withOn() {
        return new MockRelay(State.ON);
    }
    
    /**
     * Create a mock relay that is in the ON state.
     * 
     * @return the mock relay; never null
     */
    public static MockRelay withOff() {
        return new MockRelay(State.ON);
    }
    
    /**
     * Create a mock relay that is in the ON state.
     * 
     * @return the mock relay; never null
     */
    public static MockRelay withUnknown() {
        return new MockRelay(State.UNKOWN);
    }

    private State state;

	MockRelay(State initalState) {
		state = initalState;
	}
	
	@Override
	public State state() {
	    return state;
	}

	@Override
    public void on() {
		state = State.ON;
	}

	@Override
    public void off() {
		state = State.OFF;
	}
    
    public void switchOn() {
        state = State.SWITCHING_ON;
    }
    
    public void switchOff() {
        state = State.SWITCHING_OFF;
    }
    
    public void unknown() {
        state = State.UNKOWN;
    }
}
