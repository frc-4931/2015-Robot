/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * A lead screw bound by 4 switches along the range of motion:
 * <p>
 * <ul>
 * <li>Low (near the ground)</li>
 * <li>Step (at the height of the step</li>
 * <li>Tote (at the height of a tote</li>
 * <li>Tote on Step (at the height of a tote on top of the step</li>
 * </ul>
 *
 * @author Adam Gausmann
 * @see Motor
 * @see Switch
 */
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

    /**
     * Creates a new LeadScrew object with the specified motor and position switches.
     *
     * @param motor The motor used to control linear motion.
     * @param low The switch corresponding to the lowest position.
     * @param step The switch corresponding to the position at the step level.
     * @param tote The switch corresponding to the position at the tote level.
     * @param toteOnStep The switch corresponding to the position at the level of the tote on top of the step.
     */
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

    public boolean isNormalState() {
        int switchCount = 0;

        if (low.isTriggered()) {
            switchCount++;
        }
        if (step.isTriggered()) {
            switchCount++;
        }
        if (tote.isTriggered()) {
            switchCount++;
        }
        if (toteOnStep.isTriggered()) {
            switchCount++;
        }

        return switchCount <= 1;
    }

    /**
     * Tells whether the low switch is triggered.
     * @return true if the switch is triggered.
     */
    public boolean isLow() {
        return low.isTriggered() && !step.isTriggered() && !tote.isTriggered() && !toteOnStep.isTriggered();
    }

    /**
     * Tells whether the step switch is triggered.
     * @return true if the switch is triggered.
     */
    public boolean isAtStep() {
        return !low.isTriggered() && step.isTriggered() && !tote.isTriggered() && !toteOnStep.isTriggered();
    }

    /**
     * Tells whether the tote switch is triggered.
     * @return true if the switch is triggered.
     */
    public boolean isAtTote() {
        return !low.isTriggered() && !step.isTriggered() && tote.isTriggered() && !toteOnStep.isTriggered();
    }

    /**
     * Tells whether the tote-on-step switch is triggered.
     * @return true if the switch is triggered.
     */
    public boolean isAtToteOnStep() {
        return !low.isTriggered() && !step.isTriggered() && !tote.isTriggered() && toteOnStep.isTriggered();
    }

    /**
     * Attempts to move the lead screw to the lowest position.
     * @param speed The speed to move the motor at.
     */
    public void moveTowardsLow(double speed) {
        update();

        if (isLow() || !isNormalState()) {
            motor.stop();
        } else {
            moveDown(speed);
        }
    }

    /**
     * Attempts to move the lead screw to the position above the step.
     * @param speed The speed to move the motor at.
     */
    public void moveTowardsStep(double speed) {
        update();
        if (isAtStep() || !isNormalState()) {
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

    /**
     * Attempts to move the lead screw to the position above the tote.
     * @param speed The speed to move the motor at.
     */
    public void moveTowardsTote(double speed) {
        update();
        if (isAtTote() || !isNormalState()) {
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

    /**
     * Attempts to move the lead screw to the position above the tote on the step.
     * @param speed The speed to move the motor at.
     */
    public void moveTowardsToteOnStep(double speed) {
        update();
        if (isAtToteOnStep() || !isNormalState()) {
            motor.stop();
        } else {
            moveUp(speed);
        }
    }

    /**
     * Returns the current position of the lead screw.
     * @return LOW, STEP, TOTE, or TOTE_ON_STEP if only its corresponding switch is triggered; UNKNOWN otherwise
     */
    public Position getPosition() {
        if (isLow()) {
            return Position.LOW;
        } else if (isAtStep()) {
            return Position.STEP;
        } else if (isAtTote()) {
            return Position.TOTE;
        } else if (isAtToteOnStep()) {
            return Position.TOTE_ON_STEP;
        } else {
            return Position.UNKNOWN;
        }
    }

    /**
     * Gets the direction of the controlled {@link Motor}.
     * Delegates {@link Motor#getDirection()}
     *
     * @return The direction the Motor is running, FORWARD, REVERSE, or STOPPED.
     */
    public Motor.Direction getDirection() {
        return motor.getDirection();
    }

    /**
     * Stops the controlled {@link Motor}
     */
    public void stop() {
        update();
        motor.stop();
    }

    /**
     * Gets the last switch the lead screw was recorded at.
     * @return Any value of {@link Position} except UNKNOWN.
     */
    public Position getLastPosition() {
        return lastPosition;
    }

    /**
     * Gets the last direction the {@link Motor} was running in.
     * @return Any value of {@link org.frc4931.robot.component.Motor.Direction}
     */
    public Motor.Direction getLastDirection() {
        return lastDirection;
    }
}
