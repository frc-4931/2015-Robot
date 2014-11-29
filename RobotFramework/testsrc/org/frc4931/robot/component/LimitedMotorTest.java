/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import static org.fest.assertions.Assertions.assertThat;

import org.frc4931.robot.component.LimitedMotor.Position;
import org.frc4931.robot.component.Motor.Direction;
import org.frc4931.robot.component.mock.MockMotor;
import org.frc4931.robot.component.mock.MockSwitch;
import org.junit.Before;
import org.junit.Test;

public class LimitedMotorTest {
	private MockMotor motor;
	private MockSwitch highSwitch;
	private MockSwitch lowSwitch;
	private LimitedMotor limitedMotor;

	@Before
	public void beforeEach() {
		// Start out stopped in an unknown position
		motor = MockMotor.stopped();
		highSwitch = MockSwitch.notTriggered();
		lowSwitch = MockSwitch.notTriggered();
		limitedMotor = new LimitedMotor(motor, highSwitch, lowSwitch);
	}

	@Test
	public void shouldBeInitializedAsStoppedAtUnknownPosition() {
		assertStopped();
		assertPositionUnknown();
	}

	@Test
	public void shouldRunInReverseUntilLow() {
		limitedMotor.setLow(0.5);

		assertMovingLow();

		lowSwitch.setTriggered();
		// A command will be responsible for stopping the motor
		limitedMotor.stop();

		assertStopped();
		assertLow();
	}

	@Test
	public void shouldRunForwardUntilHigh() {
		limitedMotor.setHigh(0.5);

		assertMovingHigh();
		assertPositionUnknown();

		highSwitch.setTriggered();
		// A command will be responsible for stopping the motor
		limitedMotor.stop();

		assertHigh();
		assertStopped();
	}

	@Test
	public void shouldMoveFromLowToHigh() {
		lowSwitch.setTriggered();

		assertLow();
		assertStopped();

		limitedMotor.setHigh(0.5);
		lowSwitch.setNotTriggered();

		assertPositionUnknown();
		assertMovingHigh();

		highSwitch.setTriggered();
		// A command will be responsible for stopping the motor
		limitedMotor.stop();

		assertHigh();
		assertStopped();
	}

	@Test
	public void shouldMoveFromHighToLow() {
		highSwitch.setTriggered();

		assertHigh();
		assertStopped();

		limitedMotor.setLow(0.5);
		highSwitch.setNotTriggered();

		assertPositionUnknown();
		assertMovingLow();

		lowSwitch.setTriggered();
		// A command will be responsible for stopping the motor
		limitedMotor.stop();

		assertLow();
		assertStopped();
	}

	private void assertLow() {
		assertThat(limitedMotor.getPosition()).isEqualTo(Position.LOW);
		assertThat(limitedMotor.isLow()).isTrue();
		assertThat(limitedMotor.isHigh()).isFalse();
	}

	private void assertHigh() {
		assertThat(limitedMotor.getPosition()).isEqualTo(Position.HIGH);
		assertThat(limitedMotor.isLow()).isFalse();
		assertThat(limitedMotor.isHigh()).isTrue();
	}

	private void assertPositionUnknown() {
		assertThat(limitedMotor.getPosition()).isEqualTo(Position.UNKNOWN);
	}

	private void assertMovingLow() {
		assertThat(limitedMotor.getDirection()).isEqualTo(Direction.REVERSE);
		assertThat(motor.getSpeed()).isLessThan(0.0);
	}

	private void assertMovingHigh() {
		assertThat(limitedMotor.getDirection()).isEqualTo(Direction.FORWARD);
		assertThat(motor.getSpeed()).isGreaterThan(0.0);
	}

	private void assertStopped() {
		assertThat(limitedMotor.getDirection()).isEqualTo(Direction.STOPPED);
		assertThat(motor.getSpeed()).isEqualTo(0.0);
	}
}
