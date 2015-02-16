/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.AngleSensor;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.PIDController;

public class PIDMotorWithAngle implements MotorWithAngle {
    private final Motor motor;
    private final AngleSensor angleSensor;
    private final Switch home;
    private final PIDController pidController;
    private double tolerance;

    public PIDMotorWithAngle(Motor motor, AngleSensor angleSensor, Switch home, double tolerance) {
        this.motor = motor;
        this.angleSensor = angleSensor;
        this.home = home;
        pidController = new PIDController(1.0, 0.0, 0.0, angleSensor::getAngle, motor::setSpeed);
        pidController.setInputRange(0.0, 360.0);
        pidController.setOutputRange(-1.0, 1.0);
        pidController.setContinuous();
        this.tolerance = tolerance;
    }
    
    @Override
    public void home(double speed) {
        while(!home.isTriggered()) {
            motor.setSpeed(speed);
        }
        angleSensor.reset();
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    @Override
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public void setAngle(double angle) {
        pidController.setSetpoint(angle);
    }

    @Override
    public double getAngle() {
        return angleSensor.getAngle();
    }

    //TODO Rate of change?
    @Override
    public void setSpeed(double speed) {
        motor.setSpeed(speed);
    }

    @Override
    public double getSpeed() {
        return motor.getSpeed();
    }

    @Override
    public short getSpeedAsShort() {
        return 0;
    }
}
