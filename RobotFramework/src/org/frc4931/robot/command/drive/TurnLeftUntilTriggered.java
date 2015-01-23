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
 * Turns the drive system leftward until a switch is triggered.
 *
 * @author Adam Gausmann
 * @see org.frc4931.robot.subsystem.DriveSystem
 * @see org.frc4931.robot.component.Switch
 */
public class TurnLeftUntilTriggered extends CommandWithSwitch {
    private final DriveSystem driveSystem;
    private final double speed;

    /**
     * Creates a new instance of this command.
     * @param driveSystem The {@link org.frc4931.robot.subsystem.DriveSystem} to operate upon.
     * @param speed The speed at which to turn
     * @param swtch The switch that stops the drive system when triggered.
     */
    public TurnLeftUntilTriggered(DriveSystem driveSystem, double speed, Switch swtch) {
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
        driveSystem.arcade(0.0, Math.abs(speed));
    }

    @Override
    protected void end() {
        driveSystem.stop();
    }
}
