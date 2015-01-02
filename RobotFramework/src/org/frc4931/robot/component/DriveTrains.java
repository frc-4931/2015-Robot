/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * Factory methods for creating new DriveTrain.
 */
public class DriveTrains {
    /**
     * Create a new drive train with four motors (2 left, 2 right)
     * @param leftFront front left motor; may not be null
     * @param leftBack back left motor; may not be null
     * @param rightFront front right motor; may not be null
     * @param rightBack back right motor; may not be null
     * @return the new drive train: never null
     */
    public static DriveTrain create( Motor leftFront, Motor leftBack, Motor rightFront, Motor rightBack){
        return new FourMotor(leftFront, leftBack, rightFront, rightBack);
    }

    private static final class FourMotor implements DriveTrain {

        private final Motor leftFront;
        private final Motor LeftBack;
        private final Motor rightFront;
        private final Motor rightBack;
        
        protected FourMotor( Motor leftFront, Motor leftBack, Motor rightFront, Motor rightBack){
            this.leftFront = leftFront;
            this.LeftBack = leftBack;
            this.rightFront = rightFront;
            this.rightBack = rightBack;
        }
        @Override
        public void drive(double leftMotor, double rightMotor) {
            this.leftFront.setSpeed(leftMotor);
            this.LeftBack.setSpeed(leftMotor);
            this.rightFront.setSpeed(rightMotor);
            this.rightBack.setSpeed(rightMotor);
        }
        
    }
}
