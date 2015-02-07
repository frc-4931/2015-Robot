/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.driver;

import org.frc4931.robot.component.Switch;

public interface InputDevice {
    public AnalogAxis getAxis(int axis);
    public Switch getButton(int button);
    public DirectionalAxis getDPad(int pad);
}
