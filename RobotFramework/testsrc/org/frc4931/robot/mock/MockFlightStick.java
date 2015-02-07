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

import org.frc4931.robot.component.Switch;
import org.frc4931.robot.driver.AnalogAxis;
import org.frc4931.robot.driver.DirectionalAxis;
import org.frc4931.robot.driver.FlightStick;
import org.frc4931.robot.driver.InputDevice;

/**
 * A implementation of {@link org.frc4931.robot.driver.FlightStick} for testing that does not require any hardware to use.
 * @see org.frc4931.robot.driver.FlightStick
 */
public class MockFlightStick implements FlightStick {

    public static MockFlightStick create() {
        return new MockFlightStick(null);
    }
    
    private final MockAnalogAxis pitch = MockAnalogAxis.create();
    private final MockAnalogAxis yaw = MockAnalogAxis.create();
    private final MockAnalogAxis roll = MockAnalogAxis.create();
    private final MockAnalogAxis throttle = MockAnalogAxis.create();
    private final ConcurrentMap<Integer,MockSwitch> buttons = new ConcurrentHashMap<>();
    private final Supplier<MockSwitch> buttonFactory;
    
    protected MockFlightStick(Supplier<MockSwitch> buttonFactory) {
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
    public Switch getTrigger() {
        return buttons.get(0);
    }

    @Override
    public Switch getThumb() {
        return buttons.get(1);
    }

    @Override
    public AnalogAxis getAxis(int axis) {
        return null;
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

    @Override
    public DirectionalAxis getDPad(int pad) {
        return null;
    }

}
