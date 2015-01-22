/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.drive;

import org.frc4931.robot.command.TimedCommand;
import org.frc4931.robot.subsystem.DriveSystem;

/**
 * Turns the drive system leftward for a certain amount of time.
 *
 * @author Adam Gausmann
 * @see org.frc4931.robot.subsystem.DriveSystem
 */
public class TurnLeftWithDuration extends TimedCommand {
    private final DriveSystem driveSystem;
    private final double turnSpeed;

    /**
     * Creates a new instance of this command
     * @param driveSystem The {@link org.frc4931.robot.subsystem.DriveSystem} to operate upon.
     * @param turnSpeed The speed at which to turn.
     * @param timeout The command timeout in seconds.
     */
    public TurnLeftWithDuration(DriveSystem driveSystem, double turnSpeed, double timeout) {
        super(timeout);
        this.driveSystem = driveSystem;
        this.turnSpeed = turnSpeed;
        requires(driveSystem);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        driveSystem.arcade(0.0, Math.abs(turnSpeed));
    }

    @Override
    protected void end() {
        driveSystem.stop();
    }
}
