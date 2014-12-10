/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component.mock;

import org.frc4931.robot.component.Gyroscope;

/**
 * A implementation of {@link Gyroscope} for testing that does not require any
 * hardware to use.
 * 
 * @author Zach Anderson
 * @see Gyroscope
 */
public final class MockGyroscope implements Gyroscope {
	private double angle = 0;
	private double velocity = 0;
	private double zeroPoint = 0;

	// XXX I don't see the benefit of this over a public constructor, other than
	// constancy with the other classes.
	public static MockGyroscope getNew() {
		return new MockGyroscope();
	}

	private MockGyroscope() {
	}

	@Override
	public double getAngle() {
		return angle - zeroPoint;
	}

	@Override
	public void reset() {
		zeroPoint = angle;
	}

	@Override
	public double getRawAngle() {
		return angle;
	}

	@Override
	public double getRate() {
		return velocity;
	}

	public void setVelocity(double v) {
		velocity = v;
	}

	public void setDisplacement(double d) {
		angle = d;
	}

}
