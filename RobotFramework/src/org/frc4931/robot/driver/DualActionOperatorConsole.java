package org.frc4931.robot.driver;

import org.frc4931.robot.component.SwitchWithIndicator;

public class DualActionOperatorConsole extends OperatorConsole {
    public DualActionOperatorConsole(int port) {
        super(port);
    }

    @Override
    public SwitchWithIndicator getGrabberToFull() {
        return getButton(5);
    }

    @Override
    public SwitchWithIndicator getGrabberToHalf() {
        return getButton(6);
    }

    @Override
    public SwitchWithIndicator getKickerToFull() {
        return getButton(7);
    }

    @Override
    public SwitchWithIndicator getKickerToTransfer() {
        return getButton(8);
    }

    @Override
    public SwitchWithIndicator getRampToggle() {
        return getButton(1);
    }

    @Override
    public SwitchWithIndicator getCounterUp() {
        return getButton(4);
    }

    @Override
    public SwitchWithIndicator getCounterReset() {
        return getButton(3);
    }

    @Override
    public AnalogAxis getTopFader() {
        return getAxis(1);
    }

    @Override
    public AnalogAxis getBottomFader() {
        return getAxis(3);
    }

    @Override
    public AnalogAxis getLeftTrim() {
        return getAxis(0);
    }

    @Override
    public AnalogAxis getMiddleTrim() {
        return getAxis(0);
    }

    @Override
    public AnalogAxis getRightTrim() {
        return getAxis(2);
    }
}
