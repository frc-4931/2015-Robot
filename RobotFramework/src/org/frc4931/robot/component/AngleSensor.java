/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;


/**
 * 
 */
public interface AngleSensor {
    
   public double getAngle();
   
   public void reset();
   
   /**
    * Inverts the specified {@link AngleSensor} so that negative angles become positive angles.
    * @param sensor the {@link AngleSensor} to invert
    * @return an {@link AngleSensor} that reads the opposite of the original sensor
    */
   public static AngleSensor invert(AngleSensor sensor) {
       return new AngleSensor() {
           @Override
           public void reset() {
               sensor.reset();
           }
        
           @Override
           public double getAngle() {
               return -sensor.getAngle();
           }
       };
   }
   
}
