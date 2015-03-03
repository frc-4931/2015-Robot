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
import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.PIDController;

public class PIDMotorWithAngle implements MotorWithAngle {
    private final Motor motor;
    private final CurrentSensor motorCurrent;
    private final AngleSensor angleSensor;
    private final Switch home;
    private final PIDController pidController;
    private final double softLimit;
    private final double maxCurrent;
    private final double tolerance;

    /**
     * Constructs a new {@link PIDMotorWithAngle}.
     * @param motor the {@link Motor} that drives this {@link PIDMotorWithAngle}
     * @param motorCurrent the {@link CurrentSensor} attached to {@code motor}
     * @param angleSensor the {@link AngleSensor} attached to {@code motor}
     * @param home the {@link Switch} at the lowest point in the range of motion
     * @param tolerance the minimum acceptable error in degrees
     * @param maxCurrent the maximum current before {@code motor} stops
     * @param p the p constant of the pid controller controlling {@code motor}
     * @param i the i constant of the pid controller controlling {@code motor}
     * @param d the d constant of the pid controller controlling {@code motor}
     * @param maxTowardSpeed the maximum speed {@code motor} should move toward {@code home} in the range (0.0, 1.0]
     * @param maxAwaySpeed the maximum speed {@code motor} should move away from {@code home} in the range (0.0, 1.0]
     * @param maxAngle the maximum displacement in degrees from {@code home} {@code motor} should move before it stops
     */
    public PIDMotorWithAngle(Motor motor, CurrentSensor motorCurrent, AngleSensor angleSensor, Switch home,
                             double tolerance, double maxCurrent,
                             double p, double i, double d,
                             double maxTowardSpeed, double maxAwaySpeed,
                             double maxAngle) {
        this.motor = motor;
        this.motorCurrent = motorCurrent;
        this.angleSensor = angleSensor;
        this.home = home;
        this.softLimit = maxAngle;
        this.maxCurrent = maxCurrent;
        this.tolerance = tolerance;
        
        pidController = new PIDController(p, i, d, angleSensor::getAngle, this::updateMotor, 0.005);
        pidController.setAbsoluteTolerance(tolerance);
        pidController.setInputRange(0, maxAngle);
        pidController.setOutputRange(-1 * maxTowardSpeed, maxAwaySpeed);
        pidController.disable();
    }
    
    @Override
    public void home(double awaySpeed, double towardSpeed) {
        pidController.disable();
        
        // Move away from home until it isnt triggered or we hit something
        while(home.isTriggered() && motorCurrent.getCurrent() < maxCurrent) motor.setSpeed(awaySpeed);
        
        // Move back towards switch or until we hit something
        while(!home.isTriggered() && motorCurrent.getCurrent() < maxCurrent) motor.setSpeed(-1 * towardSpeed);
        
        motor.stop();
        angleSensor.reset();
        
        pidController.setSetpoint(0);
        pidController.enable();
    }
    
    // Catches the output from the PIDController
    private void updateMotor(double speed) {
        // If we are stalled against limit switch and trying to move past it, stop
        if(home.isTriggered() && speed < 0) speed = 0;
        
        // If we are against the soft limit and trying to move past it, stop and update home
        if(getAngle() >= softLimit && speed > 0) {
            speed = 0;
            angleSensor.reset();
        }
        
        // If we are stalled, stop
        if(motorCurrent.getCurrent() > maxCurrent) {
            System.out.println("STALLED");
            speed = 0;
        }
        
        motor.setSpeed(speed);
    }
    
    @Override
    public void setAngle(double angle) {
        System.out.println(Math.round(getAngle()*100)/100.0 + " -> " + angle +
                           " e = " + Math.round(pidController.getError()*100)/100.0);
        pidController.enable();
        pidController.setSetpoint(angle);
    }
    
    @Override
    public void stop() {
        pidController.disable();
        motor.stop();
    }

    @Override
    public double getAngle() {
        return angleSensor.getAngle();
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }
}
