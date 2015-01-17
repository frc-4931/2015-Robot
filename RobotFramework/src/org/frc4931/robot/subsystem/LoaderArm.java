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

import org.frc4931.robot.component.LimitedMotor;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Switch;
import org.frc4931.utils.Operations;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A subsystem used to control the grabbing arms that load totes onto the Ramp.
 */
public final class LoaderArm extends SubsystemBase {

    private final LimitedMotor lifter;
    private final Solenoid grabber;
    private final Switch capturable;
    private final Switch captured;

    /**
     * Creates a new LoaderArm subsystem using the lifting & grabbing Solenoids. Assumes no default command.
     * @param lifter The Solenoid used to raise and lower the arms; may not be null.
     * @param grabber The solenoid used to grab and release objects; may not be null.
     * @param capturable The switch used to identify wether a tote is able to be capture; may not be null.
     * @param captured The switch used to identify wether a tote has been successfully captured; may not be null.
     */
    public LoaderArm(LimitedMotor lifter, Solenoid grabber, Switch capturable, Switch captured) {
        this(lifter, grabber, capturable, captured, null);
    }
    
    /**
     * Creates a new LoaderArm subsystem using the lifting & grabbing Solenoids and the default command.
     * @param lifter The Solenoid used to raise and lower the arms; may not be null.
     * @param grabber The solenoid used to grab and release objects; may not be null.
     * @param capturable The switch used to identify wether a tote is able to be capture; may not be null.
     * @param captured The switch used to identify wether a tote has been successfully captured; may not be null.
     * @param defaultCommandSupplier the supplier for this subsystem's default command; may be null if there is no default command
     */
    public LoaderArm(LimitedMotor lifter, Solenoid grabber, Switch capturable, Switch captured, Supplier<Command> defaultCommandSupplier) {
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
     * Raises the arms, including any objects held by them, at the given speed.
     * @param speed the speed to raise the arm; must be greater than 0 and less than or equal to 1.
     */
    public void raise( double speed ) {
        speed = Operations.positiveLimit(MAXIMUM_VOLTAGE, speed, MINIMUM_USABLE_VOLTAGE);
        lifter.moveTowardsHigh(speed);
    }

    /**
     * Lowers the arms, including any objects held by them, at the given speed
     * @param speed the speed to raise the arm; must be greater than 0 and less than or equal to 1.
     */
    public void lower( double speed ) {
        speed = Operations.positiveLimit(MAXIMUM_VOLTAGE, speed, MINIMUM_USABLE_VOLTAGE);
        lifter.moveTowardsLow(speed);
    }
    
    /**
     * Determine if the arms are raised.
     * @return true if the arms are in the raised position, or false otherwise.
     */
    public boolean isRaised() {
        return lifter.isHigh();
    }
    
    /**
     * Determine if the arms are lowered.
     * @return true if the arms are in the lower position, or false otherwise.
     */
    public boolean isLowered() {
        return lifter.isLow();
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
