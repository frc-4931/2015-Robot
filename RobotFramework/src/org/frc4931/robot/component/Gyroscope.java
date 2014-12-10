/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

/**
 * A gyroscope is a device that measures angular velocity about a single axis. A
 * gyroscope can indirectly provide angular displacement by integrating the
 * velocity with respect to time. Negative values are assumed to be
 * counter-clockwise and positive values are clockwise.
 * 
 * @author Zach Anderson
 *
 */
public interface Gyroscope {
	/**
	 * Gets the rate of change in {@link #getAngle()} of this {@link Gyroscope}
	 * in degrees per second.
	 * 
	 * @return the angular velocity of this {@link Gyroscope}
	 */
	public double getRate();

	/**
	 * Gets the angular displacement of this {@link Gyroscope} since the last
	 * reset in continuous degrees.
	 * 
	 * @return the angular displacement of this {@link Gyroscope}
	 */
	public double getAngle();

	/**
	 * Gets the angular displacement of this {@link Gyroscope} since the last
	 * reset in degrees in the range [0, 360).
	 * 
	 * @return the heading of this {@link Gyroscope}
	 */
	public default double getHeading() {
		return getAngle() % 360;
	}

	/**
	 * Resets this {@link Gyroscope} so that {@link #getAngle()} and
	 * {@link #getHeading()} will return 0 at the current orientation. Does not
	 * affect {@link #getRawAngle()} or {@link #getRawHeading()}.
	 */
	public void reset();

	/**
	 * Gets the raw angular displacement of this {@link Gyroscope} since it was
	 * initialized. Unaffected by {@link #reset()}.
	 * 
	 * @return the raw angular displacement of this {@link Gyroscope}
	 */
	public double getRawAngle();

	/**
	 * Gets the raw angular displacement of this {@link Gyroscope} since it was
	 * initialized in the range [0,359). Unaffected by {@link #reset()}.
	 * 
	 * @return the raw heading of this {@link Gyroscope}
	 */
	public default double getRawHeading() {
		return getRawAngle() % 360;
	}
}
