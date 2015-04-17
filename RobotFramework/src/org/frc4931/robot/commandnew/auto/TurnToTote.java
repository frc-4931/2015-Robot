/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.commandnew.auto;

import java.util.function.Supplier;

import org.frc4931.robot.commandnew.Command;
import org.frc4931.robot.system.DriveInterpreter;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.MeasurementType;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * 
 */
public class TurnToTote extends Command {
    private static final int WIDTH = 640;
    private static final int TOLERANCE = 25;
    
    private final NIVision.Range TOTE_HUE_RANGE = new NIVision.Range(24, 49);     //Default hue range for yellow tote
    private final NIVision.Range TOTE_SAT_RANGE = new NIVision.Range(67, 255);    //Default saturation range for yellow tote
    private final NIVision.Range TOTE_VAL_RANGE = new NIVision.Range(49, 255);    //Default value range for yellow tote

    private final DriveInterpreter drive;
    private final Supplier<Image> camera;
    
    private Image binary;
    
    public TurnToTote(DriveInterpreter drive, Supplier<Image> camera) {
        super(drive);
        this.drive = drive;
        this.camera = camera;
        
        binary = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
    }
    @Override
    public void initialize() {
    }

    // TODO Implement
    @Override
    public boolean execute() {
        Image image = camera.get();
        NIVision.imaqColorThreshold(binary, image, 255, NIVision.ColorMode.HSV, TOTE_HUE_RANGE, TOTE_SAT_RANGE, TOTE_VAL_RANGE);

        int blobs = NIVision.imaqCountParticles(binary, 1);
        double runningMax = 0;
        int index = 0;
        for(int i = 0; i < blobs; i++) {
            double width = NIVision.imaqMeasureParticle(binary, i, 0, MeasurementType.MT_AREA);
            if(width > runningMax){
                runningMax = width;
                index = i;
            }
        }
        SmartDashboard.putNumber("NumBlobs", blobs);
        if(runningMax > 10000) {
            double blobCenter = NIVision.imaqMeasureParticle(binary, index, 0, MeasurementType.MT_CENTER_OF_MASS_X);
            double error = blobCenter - (WIDTH/2);
            SmartDashboard.putNumber("Blob Center", blobCenter);
            SmartDashboard.putNumber("Error", error);
            if(Math.abs(error) < TOLERANCE) {
                return true;
//                if (runningMax < 750000){
//                    CameraServer.getInstance().setImage(binary);
//                    drive.arcade(0.5, 0);
//                } else {
//                    drive.stop();
//                    return true;
//                }
            }else {
                double magnitude = Math.min(0.9, (0.7 + (Math.abs(error) / 2750.0)));
                drive.arcade(0.2, magnitude * Math.signum(error));
                SmartDashboard.putNumber("Turn",  magnitude* Math.signum(error));
            }
        } else {
            drive.arcade(0, 0.75);
        }
        CameraServer.getInstance().setImage(binary);

        return false;
    }

    @Override
    public void end() {
        drive.stop();
    }

}
