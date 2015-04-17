/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.command.vision;

import org.frc4931.robot.command.OneShotCommand;
import org.frc4931.robot.subsystem.VisionSystem;

/**
 * Switches to the front camera.
 * @see org.frc4931.robot.subsystem.VisionSystem
 */
@Deprecated
public class UseFrontCamera extends OneShotCommand {

    private final VisionSystem vision;
    
    public UseFrontCamera(VisionSystem vision ) {
        assert vision != null;
        this.vision = vision;
        requires(this.vision);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        this.vision.useFrontCamera();
    }

    @Override
    protected void end() {
    }

}
