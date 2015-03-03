/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.AngleSensor;
import org.frc4931.robot.component.CurrentSensor;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

/**
 * Talon speed controller with position sensor
 * @author Nathan Brown
 */
public class HardwareTalonSRX {
    private final CANTalon motor;
    private final double ppd;
    
    HardwareTalonSRX(int id, double pulsesPerDegree){
        this.motor = new CANTalon(id);
        this.ppd = pulsesPerDegree;
        
        // Set up hard limits
        motor.enableLimitSwitch(false, true);
        motor.ConfigRevLimitSwitchNormallyOpen(true);
        
        // Disable soft limits, we are handling them in PIDMotorWithAngle
        motor.enableForwardSoftLimit(false);
        motor.enableReverseSoftLimit(false);
    }
    
    public Motor getMotor() {
        return new Motor() {
            @Override
            public void setSpeed(double speed) {
                motor.changeControlMode(ControlMode.PercentVbus);
                motor.set(speed);
            }
            
            @Override
            public short getSpeedAsShort() {
                return (short)(getSpeed() * 1000);
            }
            
            @Override
            public double getSpeed() {
                motor.changeControlMode(ControlMode.PercentVbus);
                return motor.get();
            }
            
            @Override
            public void stop() {
                motor.enableBrakeMode(true);
                motor.set(0);
            }
        };
    }
    
    public AngleSensor getAngleSensor() {
        return new AngleSensor() {
            double zero;
            
            @Override
            public double getAngle() {
                return (motor.getEncPosition() / ppd) - zero;
            }
            
            @Override
            public void reset() {
                motor.setPosition(0);
                zero = motor.getEncPosition() * ppd;
            }
        };
    }
    
    public Switch getHomeSwitch() {
        return motor::isRevLimitSwitchClosed;
    }
    
    public CurrentSensor getCurrentSensor() {
        return motor::getOutputCurrent;
    }
    
}
