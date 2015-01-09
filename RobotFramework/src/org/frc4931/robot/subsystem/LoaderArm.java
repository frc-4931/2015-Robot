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

import java.util.function.Supplier;

import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A subsystem used to control the grabbing arms that load totes onto the Ramp.
 */
public final class LoaderArm extends SubsystemBase {
    private final Solenoid lifter;
    private final Solenoid grabber;
    private final Switch capturable;
    private final Switch captured;

    /**
     * Creates a new LoaderArm subsystem using the lifting & grabbing Solenoids and the default command.
     * @param lifter The Solenoid used to raise and lower the arms; may not be null.
     * @param grabber The solenoid used to grab and release objects; may not be null.
     * @param capturable The switch used to identify wether a tote is able to be capture; may not be null.
     * @param captured The switch used to identify wether a tote has been successfully captured; may not be null.
     * @param defaultCommandSupplier the supplier for this subsystem's default command; may be null if there is no default command
     */
    public LoaderArm(Solenoid lifter, Solenoid grabber, Switch capturable, Switch captured, Supplier<Command> defaultCommandSupplier) {
        super(defaultCommandSupplier);
        this.lifter = lifter;
        this.grabber = grabber;
        this.capturable = capturable;
        this.captured = captured;
        assert this.lifter != null;
        assert this.grabber != null;
        assert this.capturable != null;
        assert this.captured != null;
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
   /**
    * Returns wether the arm is able to capture a tote.
    * @return true if the tote can be captured.
    */
    public boolean canCapture(){
        return this.capturable.isTriggered();
    }
    /**
     * Returns wether the arm has captured a tote.
     * @return true it the tote has been captured.
     */
    public boolean didCapture(){
        return this.captured.isTriggered();
    }
}
