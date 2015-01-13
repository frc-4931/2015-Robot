/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

public final class LeadScrew {
    public enum Position {
        UNKNOWN, LOW, STEP, TOTE, TOTE_ON_STEP
    }

    private final Motor motor;
    private final Switch low;
    private final Switch step;
    private final Switch tote;
    private final Switch toteOnStep;
    private Position lastPosition;
    private Motor.Direction lastDirection;

    public LeadScrew(Motor motor, Switch low, Switch step, Switch tote, Switch toteOnStep) {
        this.motor = motor;
        this.low = low;
        this.step = step;
        this.tote = tote;
        this.toteOnStep = toteOnStep;
        lastPosition = Position.UNKNOWN;
        lastDirection = getDirection();
    }

    private void moveDown(double speed) {
        motor.setSpeed(-Math.abs(speed));
    }

    private void moveUp(double speed) {
        motor.setSpeed(Math.abs(speed));
    }

    private void update() {
        Position newPosition = getPosition();
        if (newPosition != Position.UNKNOWN && newPosition != lastPosition) {
            lastPosition = newPosition;
            lastDirection = getDirection();
        }
    }

    public boolean isLow() {
        return low.isTriggered();
    }

    public boolean isAtStep() {
        return step.isTriggered();
    }

    public boolean isAtTote() {
        return tote.isTriggered();
    }

    public boolean isAtToteOnStep() {
        return toteOnStep.isTriggered();
    }

    public void moveTowardsLow(double speed) {
        update();
        if (isLow()) {
            motor.stop();
        } else {
            moveDown(speed);
        }
    }

    public void moveTowardsStep(double speed) {
        update();
        if (isAtStep()) {
            motor.stop();
        } else {
            if (lastPosition == Position.LOW) {
                moveUp(speed);
            } else if (lastPosition == Position.STEP) {
                if (lastDirection == Motor.Direction.REVERSE) {
                    moveUp(speed);
                } else if (lastDirection == Motor.Direction.FORWARD) {
                    moveDown(speed);
                }
            } else if (lastPosition == Position.TOTE || lastPosition == Position.TOTE_ON_STEP) {
                moveDown(speed);
            }
        }
    }

    public void moveTowardsTote(double speed) {
        update();
        if (isAtTote()) {
            motor.stop();
        } else {
            if (lastPosition == Position.LOW || lastPosition == Position.STEP) {
                moveUp(speed);
            } else if (lastPosition == Position.TOTE) {
                if (lastDirection == Motor.Direction.REVERSE) {
                    moveUp(speed);
                } else if (lastDirection == Motor.Direction.FORWARD) {
                    moveDown(speed);
                }
            } else if (lastPosition == Position.TOTE_ON_STEP) {
                moveDown(speed);
            }
        }
    }

    public void moveTowardsToteOnStep(double speed) {
        update();
        if (isAtToteOnStep()) {
            motor.stop();
        } else {
            moveUp(speed);
        }
    }

    public Position getPosition() {
        if (isLow() && !isAtStep() && !isAtTote() && !isAtToteOnStep()) {
            return Position.LOW;
        } else if (!isLow() && isAtStep() && !isAtTote() && !isAtToteOnStep()) {
            return Position.STEP;
        } else if (!isLow() && !isAtStep() && isAtTote() && !isAtToteOnStep()) {
            return Position.TOTE;
        } else if (!isLow() && !isAtStep() && !isAtTote() && isAtToteOnStep()) {
            return Position.TOTE_ON_STEP;
        } else {
            return Position.UNKNOWN;
        }
    }

    public Motor.Direction getDirection() {
        return motor.getDirection();
    }

    public void stop() {
        update();
        motor.stop();
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    public Motor.Direction getLastDirection() {
        return lastDirection;
    }
}
