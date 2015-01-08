/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */

/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */

package org.frc4931.robot.subsystem;

import edu.wpi.first.wpilibj.command.Command;
import org.frc4931.robot.component.Solenoid;

import java.util.function.Supplier;

/**
 * A subsystem used to control the grabbing arms that load totes onto the Ramp.
 */
public final class LoaderArm extends SubsystemBase {
    private final Solenoid lifter;
    private final Solenoid grabber;

    /**
     * Creates a new LoaderArm subsystem using the lifting & grabbing Solenoids and the default command.
     * @param lifter The Solenoid used to raise and lower the arms; may not be null.
     * @param grabber The solenoid used to grab and release objects; may not be null.
     * @param defaultCommandSupplier the supplier for this subsystem's default command; may be null if there is no default command
     */
    public LoaderArm(Solenoid lifter, Solenoid grabber, Supplier<Command> defaultCommandSupplier) {
        super(defaultCommandSupplier);
        this.lifter = lifter;
        this.grabber = grabber;
    }

    /**
     * Raises the arms, including any objects held by them.
     */
    public void raise() {
        lifter.extend();
    }

    /**
     * Lowers the arms, including any objects held by them.
     */
    public void lower() {
        lifter.retract();
    }

    /**
     * Retracts the arms toward the center, grabbing anything inside of them.
     */
    public void grab() {
        grabber.retract();
    }

    /**
     * Extends the arms from the center, releasing anything inside of them.
     */
    public void release() {
        grabber.extend();
    }
}
