/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.mock;

import org.frc4931.robot.component.Switch;
import org.frc4931.robot.driver.AnalogAxis;
import org.frc4931.robot.driver.DirectionalAxis;
import org.frc4931.robot.driver.Gamepad;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class MockGamepad implements Gamepad {
    public static MockGamepad create() {
        return new MockGamepad(null);
    }

    private final MockAnalogAxis leftX = MockAnalogAxis.create();
    private final MockAnalogAxis leftY = MockAnalogAxis.create();
    private final MockAnalogAxis rightX = MockAnalogAxis.create();
    private final MockAnalogAxis rightY = MockAnalogAxis.create();
    private final MockAnalogAxis leftTrigger = MockAnalogAxis.create();
    private final MockAnalogAxis rightTrigger = MockAnalogAxis.create();
    private final Supplier<MockSwitch>  buttonFactory;
    private final ConcurrentMap<Integer, MockSwitch> buttons = new ConcurrentHashMap<>();

    protected MockGamepad(Supplier<MockSwitch> buttonFactory) {
        this.buttonFactory = buttonFactory != null ? buttonFactory : MockSwitch::createNotTriggeredSwitch;
    }

    @Override
    public MockAnalogAxis getLeftX() {
        return leftX;
    }

    @Override
    public MockAnalogAxis getLeftY() {
        return leftY;
    }

    @Override
    public MockAnalogAxis getRightX() {
        return rightX;
    }

    @Override
    public MockAnalogAxis getRightY() {
        return rightY;
    }

    @Override
    public MockAnalogAxis getLeftTrigger() {
        return leftTrigger;
    }

    @Override
    public MockAnalogAxis getRightTrigger() {
        return rightTrigger;
    }

    @Override
    public MockSwitch getLeftBumper() {
        return buttons.get(4);
    }

    @Override
    public MockSwitch getRightBumper() {
        return buttons.get(5);
    }

    @Override
    public MockSwitch getA() {
        return buttons.get(0);
    }

    @Override
    public MockSwitch getB() {
        return buttons.get(1);
    }

    @Override
    public MockSwitch getX() {
        return buttons.get(2);
    }

    @Override
    public MockSwitch getY() {
        return buttons.get(3);
    }

    @Override
    public MockSwitch getStart() {
        return buttons.get(8);
    }

    @Override
    public MockSwitch getSelect() {
        return buttons.get(9);
    }

    @Override
    public MockSwitch getLeftStick() {
        return buttons.get(6);
    }

    @Override
    public MockSwitch getRightStick() {
        return buttons.get(7);
    }

    @Override
    public MockAnalogAxis getAxis(int axis) {
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
    public MockDirectionalAxis getDPad(int pad) {
        return null;
    }
}
