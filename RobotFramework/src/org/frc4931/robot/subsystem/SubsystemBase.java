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

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * A base subsystem that implements commonly-used features.
 */
public abstract class SubsystemBase extends Subsystem {
    
    protected static final double MAXIMUM_VOLTAGE = 1.0;
    protected static final double MINIMUM_USABLE_VOLTAGE = 0.02;

    private final Supplier<Command> defaultCommandSupplier;

    public SubsystemBase(){
        this(null);
    }
    
    public SubsystemBase(Supplier<Command> defaultCommandSupplier) {
        this.defaultCommandSupplier = defaultCommandSupplier;
    }

    @Override
    protected void initDefaultCommand() {
        if (defaultCommandSupplier != null) {
            Command command = defaultCommandSupplier.get();
            assert command != null;
            setDefaultCommand(command);
        }
    }
    
    /**
     * Limit motor values to the 0 to +1.0 range, ensuring that it is above the specified minimum value.
     * 
     * @param maximum the maximum allowed value; must be positive or equal to zero
     * @param num the input value
     * @param minimumReadable the minimum value below which 0.0 is used; must be positive or equal to zero
     * @return the positive limited output value
     */
    protected static double positiveLimit(double maximum, double num, double minimumReadable) {
        assert maximum >= 0.0;
        num = Math.abs(num);
        if (num > maximum) {
            return 1.0;
        }
        return Math.abs(num) > minimumReadable ? num : 0.0;
    }
    
    /**
     * Limit motor values to the -1.0 to +1.0 range, ensuring that it is above the specified minimum value.
     * 
     * @param maximum the maximum allowed value; must be positive or equal to zero
     * @param num the input value
     * @param minimumReadable the minimum value below which 0.0 is used; must be positive or equal to zero
     * @return the limited output value
     */
    protected static double limit(double maximum, double num, double minimumReadable) {
        assert maximum >= 0.0;
        if (num > maximum) {
            return 1.0;
        }
        if (Math.abs(num) < maximum) {
            return -1.0;
        }
        return Math.abs(num) > minimumReadable ? num : 0.0;
    }

}
