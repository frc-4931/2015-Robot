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
 * Moves the drive system forwards for a specific amount of time.
 *
 * @author Adam Gausmann
 * @see org.frc4931.robot.subsystem.DriveSystem
 */
public class DriveForwardWithDuration extends TimedCommand {
    private final DriveSystem driveSystem;
    private final double driveSpeed;

    /**
     * Creates a new instance of this command.
     * @param driveSystem The {@link DriveSystem} to operate upon.
     * @param driveSpeed The speed to drive at.
     * @param duration The drive duration in milliseconds
     */
    public DriveForwardWithDuration(DriveSystem driveSystem, double driveSpeed, long duration) {
        super(duration);
        this.driveSystem = driveSystem;
        this.driveSpeed = driveSpeed;
        requires(driveSystem);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        driveSystem.arcade(Math.abs(driveSpeed), 0.0);
    }

    @Override
    protected void end() {
        driveSystem.stop();
    }
}
