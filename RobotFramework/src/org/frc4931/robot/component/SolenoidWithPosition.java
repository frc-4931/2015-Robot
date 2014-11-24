/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;


/**
 * 
 */
public final class SolenoidWithPosition implements Solenoid {

    static enum Position {
        EXTENDED, RETRACTED, UNKNOWN;
    }

    private final Solenoid solenoid;
    private final Switch retractSwitch;
    private final Switch extendSwitch;

    private Position position = Position.UNKNOWN;

    public SolenoidWithPosition(Solenoid solenoid, Switch retractSwitch, Switch extendSwitch) {
        this.solenoid = solenoid;
        this.retractSwitch = retractSwitch;
        this.extendSwitch = extendSwitch;
        checkState();
    }

    /**
     * Get the current position of this solenoid.
     * 
     * @return the current position; never null
     */
    public Position getPosition() {
        checkState();
        return position;
    }

    /**
     * Tests if this <code>Solenoid</code> is extended.
     * 
     * @return {@code true} if this solenoid is fully extended, or {@code false} otherwise
     */
    public boolean isExtended() {
        return getPosition() == Position.EXTENDED;
    }

    /**
     * Tests if this <code>Solenoid</code> is retracted.
     * 
     * @return {@code true} if this solenoid is fully retracted, or {@code false} otherwise
     */
    public boolean isRetracted() {
        return getPosition() == Position.RETRACTED;
    }

    @Override
    public Direction getDirection() {
        return solenoid.getDirection();
    }

    @Override
    public void extend() {
        solenoid.extend();
        checkState();
    }

    @Override
    public void retract() {
        solenoid.retract();
        checkState();
    }

    protected void checkState() {
        if ( extendSwitch.isTriggered() ) {
            if ( retractSwitch.isTriggered() ) {
                // Both switches are triggered -- WTF???
                position = Position.UNKNOWN;
            } else {
                // Extended but not retracted ...
                position = Position.EXTENDED;
            }
        } else if ( retractSwitch.isTriggered() ) {
            // Retracted but not extended
            position = Position.RETRACTED;
        } else {
            // Neither switch is triggered ...
            position = Position.UNKNOWN;
        }
    }
    
    @Override
    public String toString() {
        return "SolenoidWithPosition: " + position + " (solenoid: " + solenoid + "; extendSwitch = " + extendSwitch + "; retractSwitch = " + retractSwitch + ")";
    }
}
