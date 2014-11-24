/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component.mock;

import org.frc4931.robot.component.Relay;

/**
 * A test implementation of {@link Relay} that does not require any hardware to use.
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

	private MockRelay(State initalState) {
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
    
	/**
	 * Set this switch to {@link org.frc4931.robot.component.Relay.State#SWITCHING_ON}.
	 */
    public void switchOn() {
        state = State.SWITCHING_ON;
    }
    
    /**
     * Set this switch to {@link org.frc4931.robot.component.Relay.State#SWITCHING_OFF}.
     */
    public void switchOff() {
        state = State.SWITCHING_OFF;
    }
    
    /**
     * Set this switch to {@link org.frc4931.robot.component.Relay.State#UNKOWN}.
     */
    public void unknown() {
        state = State.UNKOWN;
    }
}
