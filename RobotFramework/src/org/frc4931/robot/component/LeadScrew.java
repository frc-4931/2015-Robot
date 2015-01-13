/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import org.frc4931.utils.Operations;

public final class LeadScrew {
    public enum Position {
        UNKNOWN, LOW, STEP, TOTE, TOTE_ON_STEP
    }

    public enum Direction {
        STOPPED, DOWN, UP
    }

    private final Motor motor;
    private final Switch low;
    private final Switch step;
    private final Switch tote;
    private final Switch toteOnStep;
    private Position lastPosition;
    private Direction lastDirection;

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

    public void update() {
        Position newPosition = getPosition();
        if (newPosition != Position.UNKNOWN && newPosition != lastPosition) {
            lastPosition = getPosition();
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
        if (isLow()) {
            motor.stop();
        } else {
            moveDown(speed);
        }
        update();
    }

    public void moveTowardsStep(double speed) {
        if (isAtStep()) {
            motor.stop();
        } else {
            switch (lastPosition) {
                case LOW:
                    moveUp(speed);
                    break;
                case STEP:
                    if (lastDirection == Direction.DOWN) {
                        moveUp(speed);
                    } else if (lastDirection == Direction.UP) {
                        moveDown(speed);
                    }
                    break;
                case TOTE:
                case TOTE_ON_STEP:
                    moveDown(speed);
                    break;
            }
        }
        update();
    }

    public void moveTowardsTote(double speed) {
        if (isAtTote()) {
            motor.stop();
        } else {
            switch (lastPosition) {
                case LOW:
                case STEP:
                    moveUp(speed);
                    break;
                case TOTE:
                    if (lastDirection == Direction.DOWN) {
                        moveUp(speed);
                    } else if (lastDirection == Direction.UP) {
                        moveDown(speed);
                    }
                    break;
                case TOTE_ON_STEP:
                    moveDown(speed);
                    break;
            }
        }
        update();
    }

    public void moveTowardsToteOnStep(double speed) {
        if (isAtToteOnStep()) {
            motor.stop();
        } else {
            moveUp(speed);
        }
        update();
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

    public Direction getDirection() {
        switch (Operations.fuzzyCompare(motor.getSpeed(), 0)) {
            case -1:
                return Direction.DOWN;
            case 0:
                return Direction.STOPPED;
            case 1:
                return Direction.UP;
            default:
                return null;
        }
    }

    public void stop() {
        motor.stop();
        update();
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }
}
