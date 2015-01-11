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
        STILL, DOWN, UP
    }

    private final Motor motor;
    private final Switch low;
    private final Switch shelf;
    private final Switch tote;
    private final Switch toteOnShelf;
    private Position lastPosition;
    private int lastDirection;

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

    private int getDirection() {
        return Operations.fuzzyCompare(motor.getSpeed(), 0);
    }

    private void updateLast() {
        if (isLow()) {
            lastPosition = Position.LOW;
            lastDirection = getDirection();
        } else if (isAtShelf()) {
            lastPosition = Position.SHELF;
            lastDirection = getDirection();
        } else if (isAtTote()) {
            lastPosition = Position.TOTE;
            lastDirection = getDirection();
        } else if (isAtToteOnShelf()) {
            lastPosition = Position.TOTE_ON_SHELF;
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
                    if (lastDirection < 0) {
                        moveUp(speed);
                    } else if (lastDirection > 0) {
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
                    if (lastDirection < 0) {
                        moveUp(speed);
                    } else if (lastDirection > 0) {
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
}
