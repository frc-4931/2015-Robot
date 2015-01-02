/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component.mock;

import org.frc4931.robot.component.DistanceSensor;

/**
 * A implementation of {@link DistanceSensor} for testing that does not require
 * any hardware to use.
 * 
 * @author Zach Anderson
 * @see DistanceSensor
 */
public final class MockDistanceSensor implements DistanceSensor {
    private double distance;

    public MockDistanceSensor(double initialDistance) {
        distance = initialDistance;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    public void setDistance(double d) {
        distance = d;
    }
}
