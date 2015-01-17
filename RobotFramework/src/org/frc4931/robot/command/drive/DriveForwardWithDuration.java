/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.drive;

import org.frc4931.robot.command.TimedCommand;
import org.frc4931.robot.subsystem.DriveSystem;

public class DriveForwardWithDuration extends TimedCommand {
    private final DriveSystem driveSystem;
    private final double driveSpeed;

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
