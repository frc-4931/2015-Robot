/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.mock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import org.frc4931.robot.driver.Joystick;

/**
 * A implementation of {@link Joystick} for testing that does not require any hardware to use.
 * @see Joystick
 */
public class MockJoystick extends Joystick {

    public static MockJoystick create() {
        return new MockJoystick(null);
    }
    
    private final MockAnalogAxis pitch = MockAnalogAxis.create();
    private final MockAnalogAxis yaw = MockAnalogAxis.create();
    private final MockAnalogAxis roll = MockAnalogAxis.create();
    private final MockAnalogAxis throttle = MockAnalogAxis.create();
    private final ConcurrentMap<Integer,MockSwitch> buttons = new ConcurrentHashMap<>();
    private final Supplier<MockSwitch> buttonFactory;
    
    protected MockJoystick(  Supplier<MockSwitch> buttonFactory ) {
        this.buttonFactory = buttonFactory != null ? buttonFactory : MockSwitch::createNotTriggeredSwitch;
    }
    
    @Override
    public MockAnalogAxis getPitch() {
        return pitch;
    }

    @Override
    public MockAnalogAxis getYaw() {
        return yaw;
    }

    @Override
    public MockAnalogAxis getRoll() {
        return roll;
    }

    @Override
    public MockAnalogAxis getThrottle() {
        return throttle;
    }

    @Override
    public MockSwitch getButton(int buttonNumber) {
        MockSwitch button = buttons.get(buttonNumber);
        if ( button == null ) {
            button = buttonFactory.get();
            MockSwitch existing = buttons.putIfAbsent(buttonNumber, button);
            if ( existing != null ) button = existing;
        }
        return button;
    }

}
