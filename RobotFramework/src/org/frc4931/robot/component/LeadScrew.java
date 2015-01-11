/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */

/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */

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
        UNKNOWN, LOW, SHELF, TOTE, TOTE_ON_SHELF
    }

    public enum Direction {
        STOPPED, DOWN, UP
    }

    private final Motor motor;
    private final Switch low;
    private final Switch shelf;
    private final Switch tote;
    private final Switch toteOnShelf;
    private Position lastPosition;
    private Direction lastDirection;

    public LeadScrew(Motor motor, Switch low, Switch shelf, Switch tote, Switch toteOnShelf) {
        this.motor = motor;
        this.low = low;
        this.shelf = shelf;
        this.tote = tote;
        this.toteOnShelf = toteOnShelf;
        lastPosition = Position.UNKNOWN;
        lastDirection = getDirection();
    }

    private void moveDown(double speed) {
        motor.setSpeed(-Math.abs(speed));
    }

    private void moveUp(double speed) {
        motor.setSpeed(Math.abs(speed));
    }

    private void updateLast() {
        Position newPosition = getPosition();
        if (newPosition != Position.UNKNOWN && newPosition != lastPosition) {
            lastPosition = getPosition();
            lastDirection = getDirection();
        }
    }

    public boolean isLow() {
        return low.isTriggered();
    }

    public boolean isAtShelf() {
        return shelf.isTriggered();
    }

    public boolean isAtTote() {
        return tote.isTriggered();
    }

    public boolean isAtToteOnShelf() {
        return toteOnShelf.isTriggered();
    }

    public void moveTowardsLow(double speed) {
        if (isLow()) {
            motor.stop();
        } else {
            moveDown(speed);
        }
        updateLast();
    }

    public void moveTowardsShelf(double speed) {
        if (isAtShelf()) {
            motor.stop();
        } else {
            switch (lastPosition) {
                case LOW:
                    moveUp(speed);
                    break;
                case SHELF:
                    if (lastDirection == Direction.DOWN) {
                        moveUp(speed);
                    } else if (lastDirection == Direction.UP) {
                        moveDown(speed);
                    }
                    break;
                case TOTE:
                case TOTE_ON_SHELF:
                    moveDown(speed);
                    break;
            }
        }
        updateLast();
    }

    public void moveTowardsTote(double speed) {
        if (isAtTote()) {
            motor.stop();
        } else {
            switch (lastPosition) {
                case LOW:
                case SHELF:
                    moveUp(speed);
                    break;
                case TOTE:
                    if (lastDirection == Direction.DOWN) {
                        moveUp(speed);
                    } else if (lastDirection == Direction.UP) {
                        moveDown(speed);
                    }
                    break;
                case TOTE_ON_SHELF:
                    moveDown(speed);
                    break;
            }
        }
        updateLast();
    }

    public void moveTowardsToteOnShelf(double speed) {
        if (isAtToteOnShelf()) {
            motor.stop();
        } else {
            moveUp(speed);
        }
        updateLast();
    }

    public Position getPosition() {
        if (isLow() && !isAtShelf() && !isAtTote() && !isAtToteOnShelf()) {
            return Position.LOW;
        } else if (!isLow() && isAtShelf() && !isAtTote() && !isAtToteOnShelf()) {
            return Position.SHELF;
        } else if (!isLow() && !isAtShelf() && isAtTote() && !isAtToteOnShelf()) {
            return Position.TOTE;
        } else if (!isLow() && !isAtShelf() && !isAtTote() && isAtToteOnShelf()) {
            return Position.TOTE_ON_SHELF;
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

    public Position getLastPosition() {
        return lastPosition;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }
}
