/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.mock;

import org.frc4931.robot.driver.DirectionalAxis;

public class MockDirectionalAxis implements DirectionalAxis {
    public static MockDirectionalAxis create() {
        return new MockDirectionalAxis(0);
    }

    public static MockDirectionalAxis create(int direction) {
        return new MockDirectionalAxis(direction);
    }

    private int direction;

    protected MockDirectionalAxis(int direction) {
        this.direction = direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public int getDirection() {
        return direction;
    }
}
