/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.MotorWithPosition;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * Talon speed controller with position sensor
 * @author Nathan Brown
 */
class HardwareTalonSRX implements MotorWithPosition{
    private CANTalon motor;
    
    @Override
    public double getPosition(){
        return motor.getPosition();
    }

    @Override
    public void setPosition(double pos) {
        motor.setPosition(pos);
    }

    @Override
    public void setSpeed(double speed) {
        motor.set(speed);
    }

    @Override
    public double getSpeed() {
        return motor.get();
    }

    @Override
    public short getSpeedAsShort() {
        return (short)(motor.get()*1000);
    }

}
