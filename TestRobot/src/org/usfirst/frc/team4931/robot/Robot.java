
package org.usfirst.frc.team4931.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    private Joystick oc;
    private int[] outs = new int[] {4, 10,  5, 11, 2, 8, 1, 7, 3, 9};

    @Override
    public void robotInit() {
        oc = new Joystick(2);
    }

    @Override
    public void teleopPeriodic() {
        for (int i : outs) {
//            try {
                oc.setOutput(i, true);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}
