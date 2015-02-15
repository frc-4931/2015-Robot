/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.system;

import org.frc4931.robot.component.Switch;

/**
 * 
 */
// TODO A better name
public class KickerSwitchSystem {
    public final Kicker kicker;
    public final Switch canCapture;
    
    public KickerSwitchSystem(Kicker kicker, Switch canCapture) {
        this.kicker = kicker;
        this.canCapture = canCapture;
    }
}
