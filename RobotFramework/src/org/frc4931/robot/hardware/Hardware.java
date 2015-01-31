/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.hardware;

import org.frc4931.robot.component.Accelerometer;
import org.frc4931.robot.component.DistanceSensor;
import org.frc4931.robot.component.Gyroscope;
import org.frc4931.robot.component.Motor;
import org.frc4931.robot.component.MotorWithAngle;
import org.frc4931.robot.component.Relay;
import org.frc4931.robot.component.Solenoid;
import org.frc4931.robot.component.Switch;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

/**
 * The factory for all devices that directly interface with robot hardware.
 * {@link Hardware} provides factory methods for all physical hardware
 * components. The methods are in nested classes, grouped by type.
 * 
 * @author Zach Anderson
 * @see Relay
 */
public class Hardware {
    public static final class Sensors {
        /**
         * Create a {@link HardwareGyroscope} on the specified channel.
         * 
         * @param channel
         *            the channel the gyroscope is plugged into
         * @return a {@link Gyroscope} on the specified channel
         */
        public static Gyroscope gyroscope(int channel) {
            return new HardwareGyroscope(channel);
        }
        
        public static final class Accelerometers {
            /**
             * Create a new {@link Accelerometer} with a range of +-2g's
             * @return an {@link Accelerometer}
             */
            public static Accelerometer accelerometer2G() {
                return new HardwareAccelerometer(2);
            }
            
            /**
             * Create a new {@link Accelerometer} with a range of +-4g's
             * @return an {@link Accelerometer}
             */
            public static Accelerometer accelerometer4G() {
                return new HardwareAccelerometer(4);
            }
            
            /**
             * Create a new {@link Accelerometer} with a range of +-8g's
             * @return an {@link Accelerometer}
             */
            public static Accelerometer accelerometer8G() {
                return new HardwareAccelerometer(8);
            }
        
            /**
            * Create a new {@link Accelerometer} with a range of +-16g's
            * @return an {@link Accelerometer}
            */
            public static Accelerometer accelerometer16G() {
                return new HardwareAccelerometer(16);
            }
        }
        
        public static final class Switches {
            /**
             * Create a generic normally closed switch on the specified channel.
             * 
             * @param channel
             *            the channel the switch is connected to
             * @return a switch on the specified channel
             */
            public static Switch normallyClosed(int channel) {
                DigitalInput input = new DigitalInput(channel);
                return new HardwareNormallyClosedSwitch(input);
            }

            /**
             * Create a generic normally open switch on the specified channel.
             * 
             * @param channel
             *            the channel the switch is connected to
             * @return a switch on the specified channel
             */
            public static Switch normallyOpen(int channel) {
                DigitalInput input = new DigitalInput(channel);
                return new HardwareNormallyOpenSwitch(input);
            }
        }

        public static final class Distance {
            /**
             * Create a {@link HardwareDigitalUltrasonic} using the specified
             * channels. See {@link HardwareDigitalUltrasonic} for explanation
             * why.
             * 
             * @param ping
             *            the digital output to ping on
             * @param echo
             *            the digital input to listen to
             * @return a {@link DistanceSensor} linked to the specified channels
             */
            public static DistanceSensor digitalUltrasonic(int ping, int echo) {
                return new HardwareDigitalUltrasonic(ping, echo);
            }

            /**
             * Create a {@link HardwareAnalogUltrasonic} using the specified
             * channel.
             * 
             * @param channel
             *            the channel the sensor is connected to
             * @return a {@link DistanceSensor} linked to the specified channel
             */
            public static DistanceSensor analogUltrasonic(int channel) {
                return new HardwareAnalogUltrasonic(channel);
            }
        }
    }

    /**
     * Create a relay on the specified channel.
     * 
     * @param channel
     *            the channel the relay is connected to
     * @return a relay on the specified channel
     */
    public static Relay relay(int channel) {
        edu.wpi.first.wpilibj.Relay relay = new edu.wpi.first.wpilibj.Relay(
                channel);
        return new HardwareRelay(relay);
    }

    public static final class Motors {
        /**
         * Create a motor driven by a Talon speed controller on the specified
         * channel.
         * 
         * @param channel
         *            the channel the controller is connected to
         * @return a motor on the specified channel
         */
        public static Motor talon(int channel) {
            return new HardwareMotor(new Talon(channel));
        }

        /**
         * Create a motor driven by a Jaguar speed controller on the specified
         * channel.
         * 
         * @param channel
         *            the channel the controller is connected to
         * @return a motor on the specified channel
         */
        public static Motor jaguar(int channel) {
            return new HardwareMotor(new Jaguar(channel));
        }

        /**
         * Create a motor driven by a Victor speed controller on the specified
         * channel.
         * 
         * @param channel
         *            the channel the controller is connected to
         * @return a motor on the specified channel
         */
        public static Motor victor(int channel) {
            return new HardwareMotor(new Victor(channel));
        }
        
        /**
         * Creates a motor controlled by a TalonSRX speed controller on a specified port.
         * @param port
         *          the port the motor controller is connected to
         * @return a motor with a specified port
         */
        public static MotorWithAngle talonSRX(int port){
            return new HardwareTalonSRX(port);
        }
    }

    public static final class Solenoids {
        /**
         * Create a solenoid that uses the specified channels on the default
         * module.
         * 
         * @param extendChannel
         *            the channel that extends the solenoid
         * @param retractChannel
         *            the channel that retracts the solenoid
         * @param initialDirection
         *            the initial direction for the solenoid; may not be null
         * @return a solenoid on the specified channels
         */
        public static Solenoid doubleSolenoid(int extendChannel,
                int retractChannel, Solenoid.Direction initialDirection) {
            DoubleSolenoid solenoid = new DoubleSolenoid(extendChannel,
                    retractChannel);
            return new HardwareDoubleSolenoid(solenoid, initialDirection);
        }

        /**
         * Create a solenoid that uses the specified channels on the given
         * module.
         * 
         * @param module
         *            the module for the channels
         * @param extendChannel
         *            the channel that extends the solenoid
         * @param retractChannel
         *            the channel that retracts the solenoid
         * @param initialDirection
         *            the initial direction for the solenoid; may not be null
         * @return a solenoid on the specified channels
         */
        public static Solenoid doubleSolenoid(int module,
                int extendChannel, int retractChannel,
                Solenoid.Direction initialDirection) {
            DoubleSolenoid solenoid = new DoubleSolenoid(module,
                    extendChannel, retractChannel);
            return new HardwareDoubleSolenoid(solenoid, initialDirection);
        }
    }
}
