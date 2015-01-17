/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.mock;

import org.frc4931.robot.driver.AnalogAxis;

/**
 * A implementation of {@link AnalogAxis} for testing that does not require any hardware to use.
 * @see AnalogAxis
 */
public class MockAnalogAxis implements AnalogAxis {

    public static MockAnalogAxis create() {
        return create(0.0);
    }

    public static MockAnalogAxis create(double initialValue) {
        return new MockAnalogAxis(initialValue);
    }
    
    private double value;
    
    protected MockAnalogAxis( double initialValue ) {
        this.value = initialValue;
    }
    
    @Override
    public double read() {
        return value;
    }
    
    public void setValue( double value ) {
        this.value = value;
    }

}
