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
import org.frc4931.robot.controller.Controller;
import org.frc4931.robot.controller.Controller.Profile;

public class PIDMotorWithAngle implements MotorWithAngle {
    private final Motor motor;
    private final CurrentSensor motorCurrent;
    private final AngleSensor angleSensor;
    private final Switch home;
    private final Controller controller;
    private final double softLimit;
    private final double maxCurrent;
    
    /**
     * Constructs a new {@link PIDMotorWithAngle}.
     * @param motor the {@link Motor} that drives this {@link PIDMotorWithAngle}
     * @param motorCurrent the {@link CurrentSensor} attached to {@code motor}
     * @param angleSensor the {@link AngleSensor} attached to {@code motor}
     * @param home the {@link Switch} at the lowest point in the range of motion
     * @param maxCurrent the maximum current before {@code motor} stops
     * @param maxAngle the maximum displacement in degrees from {@code home} {@code motor} should move before it stops
     * @param profile the {@link Profile} governing how {@code motor} is controlled
     */
    public PIDMotorWithAngle(Motor motor, CurrentSensor motorCurrent, AngleSensor angleSensor, Switch home,
                             double maxCurrent, double maxAngle,
                             Profile profile) {
        this.motor = motor;
        this.motorCurrent = motorCurrent;
        this.angleSensor = angleSensor;
        this.home = home;
        this.softLimit = maxAngle;
        this.maxCurrent = maxCurrent;
        this.controller = new Controller(angleSensor::getAngle, this::updateMotor, profile);
        
    }
    
    @Override
    public void home(double awaySpeed, double towardSpeed) {
        controller.disable();
        
        // Move away from home until it isnt triggered or we hit something
        while(home.isTriggered() && motorCurrent.getCurrent() < maxCurrent) motor.setSpeed(awaySpeed);
        System.out.println("up");
        
        // Move back towards switch or until we hit something
        while(!home.isTriggered() && motorCurrent.getCurrent() < maxCurrent) motor.setSpeed(-1 * towardSpeed);
        
        System.out.println("Reset");
        angleSensor.reset();
        motor.stop();
        
        controller.setSetpoint(0);
        controller.enable();
    }
    
    // Catches the output from the PIDController
    private void updateMotor(double speed) {
        // If we are stalled against limit switch and trying to move past it, stop and update home
        if(home.isTriggered() && speed < 0) {
            speed = 0;
            angleSensor.reset();
        }
        
        // If we are against the soft limit and trying to move past it stop
        if(getAngle() >= softLimit && speed > 0) {
            speed = 0;
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
                           " e = " + Math.round(controller.getError()*100)/100.0);
        controller.enable();
        controller.setSetpoint(angle);
    }
    
    @Override
    public void stop() {
        controller.disable();
        motor.stop();
    }

    @Override
    public double getAngle() {
        return angleSensor.getAngle();
    }

    @Override
    public boolean isAt(double angle) {
        return controller.isAt(angle);
    }
}
