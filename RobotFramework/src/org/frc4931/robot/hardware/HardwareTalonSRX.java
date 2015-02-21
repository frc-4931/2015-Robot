/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

/**
 * Talon speed controller with position sensor
 * @author Nathan Brown
 */
class HardwareTalonSRX implements MotorWithAngle{
    private final CANTalon motor;
    private final Switch home;
    private double tolerance;
    private final double ppd;
    private final double maxCurrent;
    
    HardwareTalonSRX(int port, Switch home, double tolerance, double pulsesPerDegree, double maxCurrent){
        this.motor = new CANTalon(port);
        this.home = home;
        this.tolerance = tolerance;
        this.ppd = pulsesPerDegree;
        this.maxCurrent = maxCurrent;
        motor.enableLimitSwitch(false, true);
    }
    
    @Override
    public double getTolerance() {
        return tolerance;
    }
    
    @Override
    public double getAngle(){
        motor.changeControlMode(ControlMode.Position);
        return motor.get()/ppd;
    }

    @Override
    public void setAngle(double angle) {
        motor.changeControlMode(ControlMode.Position);
        motor.set(angle*ppd);
    }

    @Override
    public void setSpeed(double speed) {
        motor.changeControlMode(ControlMode.PercentVbus);
        motor.set(speed);
    }

    @Override
    public double getSpeed() {
        motor.changeControlMode(ControlMode.PercentVbus);
        return motor.get();
    }

    @Override
    public short getSpeedAsShort() {
        return (short)(getSpeed()*1000);
    }

    @Override
    public void home(double speed) {
        motor.changeControlMode(ControlMode.PercentVbus);
        while(motor.getFaultRevLim() == 0 && Math.abs(motor.getOutputCurrent()) < maxCurrent){
            motor.set(speed);
        }
        motor.set(0);
        motor.setPosition(0);
        motor.changeControlMode(ControlMode.Position);
    }
}
