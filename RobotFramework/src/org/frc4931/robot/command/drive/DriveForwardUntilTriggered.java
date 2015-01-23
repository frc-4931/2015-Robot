/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.drive;

import org.frc4931.robot.command.CommandWithSwitch;
import org.frc4931.robot.component.Switch;
import org.frc4931.robot.subsystem.DriveSystem;

/**
 * Moves the drive system forwards until a switch is triggered.
 *
 * @author Adam Gausmann
 * @see org.frc4931.robot.subsystem.DriveSystem
 * @see org.frc4931.robot.component.Switch
 */
public class DriveForwardUntilTriggered extends CommandWithSwitch {
    private final DriveSystem driveSystem;
    private final double speed;

    /**
     * Creates a new instance of this command
     *
     * @param driveSystem The {@link DriveSystem} to operate upon.
     * @param speed The speed to drive at.
     * @param swtch The {@link Switch} that will stop the drive system when triggered.
     */
    public DriveForwardUntilTriggered(DriveSystem driveSystem, double speed, Switch swtch) {
        super(swtch);
        this.driveSystem = driveSystem;
        this.speed = speed;
        requires(driveSystem);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        driveSystem.arcade(Math.abs(speed), 0.0);
    }

    @Override
    protected void end() {
        driveSystem.stop();
    }
}
