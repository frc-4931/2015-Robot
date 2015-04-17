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
    private final CANTalon talon;
    private final Motor motor;
    private final AngleSensor encoder;
    private final Switch homeSwitch;
    private final CurrentSensor current;
    
    private final double ppd;
    
    HardwareTalonSRX(int id, double pulsesPerDegree){
        this.talon = new CANTalon(id);
        this.ppd = pulsesPerDegree;
        
        // Set up hard limits
        talon.enableLimitSwitch(false, true);
        talon.ConfigRevLimitSwitchNormallyOpen(true);
        
        // Disable soft limits, we are handling them in PIDMotorWithAngle
        talon.enableForwardSoftLimit(false);
        talon.enableReverseSoftLimit(false);
        
        motor = new TalonMotor();
        encoder = new TalonEncoder();
        
        homeSwitch = talon::isRevLimitSwitchClosed;
        current = talon::getOutputCurrent;
    }
    
    public Motor getMotor() {
        return motor;
    }
    
    public AngleSensor getAngleSensor() {
        return encoder;
    }
    
    public Switch getHomeSwitch() {
        return homeSwitch;
    }
    
    public CurrentSensor getCurrentSensor() {
        return current;
    }
    
    private class TalonMotor implements Motor {
        public TalonMotor() {}

        @Override
        public void setSpeed(double speed) {
            talon.changeControlMode(ControlMode.PercentVbus);
            talon.set(speed);
        }
        
        @Override
        public short getSpeedAsShort() {
            return (short)(getSpeed() * 1000);
        }
        
        @Override
        public double getSpeed() {
            talon.changeControlMode(ControlMode.PercentVbus);
            return talon.get();
        }
        
        @Override
        public void stop() {
            talon.enableBrakeMode(true);
            talon.set(0);
        }
    }
    
    private class TalonEncoder implements AngleSensor {
        int zero;
        
        public TalonEncoder() {}
        
        @Override
        public double getAngle() {
            return (talon.getEncPosition() - zero) / ppd;
        }
        
        @Override
        public void reset() {
            zero = talon.getEncPosition();
        }
    }
}
