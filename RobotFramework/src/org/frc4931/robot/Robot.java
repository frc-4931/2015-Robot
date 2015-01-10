/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot;

import org.frc4931.robot.driver.LogitechAttack3D;
import org.frc4931.robot.driver.OperatorInterface;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
    @Override
    public void robotInit() {
        OperatorInterface.initialize(new LogitechAttack3D(0));
    }
}
