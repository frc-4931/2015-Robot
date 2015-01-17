/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.drive;

import org.frc4931.robot.command.TimedCommand;
import org.frc4931.robot.subsystem.DriveSystem;

public class TurnRightWithDuration extends TimedCommand {
    private final DriveSystem driveSystem;
    private final double turnSpeed;

    public TurnRightWithDuration(DriveSystem driveSystem, double turnSpeed, long duration) {
        super(duration);
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

    }
}
