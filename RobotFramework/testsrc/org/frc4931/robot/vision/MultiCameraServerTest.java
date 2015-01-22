/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.vision;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

import org.fest.assertions.Fail;
import org.frc4931.robot.vision.MockCamera.Content;
import org.frc4931.robot.vision.MockCamera.Size;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.wpi.first.wpilibj.CameraServer;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test the {@link MultiCameraServer} and WPILib's {@link CameraServer} (really, our custom {@link StoppableCameraServer}).
 */
public class MultiCameraServerTest {

    private static final int PORT = 1180; // See CameraServer.serve()

    private static MockCamera camera1;
    private static MockCamera camera2;
    private static CompositeCamera camera;
    private static RemoteDisplay display;
    private static ByteBuffer lastImage;

    @BeforeClass
    public static void beforeAll() throws IOException {
        // Set up the camera ...
        camera1 = new MockCamera("Camera1");
        camera1.setContent(Content.SPACE);
        camera1.setSize(Size.SMALL);

        camera2 = new MockCamera("Camera2");
        camera2.setContent(Content.GRID);
        camera2.setSize(Size.SMALL);
        
        camera = new CompositeCamera(camera1,camera2);

        // Start the server and start automatically capturing images from our camera(s) ...
        MultiCameraServer.startServer();
        MultiCameraServer.startAutomaticCapture(camera);

        // Set up the "remote display" that receives images from the server ...
        display = new RemoteDisplay(InetAddress.getByName("localhost"), PORT);
        display.setImageSize(Size.SMALL);
        display.setFramesPerSecond(500);
        display.connect();
    }

    @AfterClass
    public static void afterAll() throws IOException {
        try {
            display.disconnect();
        } finally {
            try {
                MultiCameraServer.stopAutomaticCapture();
            } finally {
                lastImage = null;
                MultiCameraServer.stopServer();
            }
        }
    }

    protected static void debug(Object msg) {
        System.out.println(msg);
    }

    protected static Consumer<ByteBuffer> toBuffer() {
        return (buffer) -> lastImage = buffer;
    }

    protected void assertConsumedMessageMatchesCameraImage() {
        MockCamera current = (MockCamera)camera.currentCamera();
        for ( int i=0; i!=500; ++i ) {
            if ( current.matches(lastImage) ) {
                System.out.println("Found correct image from camera '" + current.getName() + "' after " + (i+1) + " images");
                return;
            }
        }
        System.out.println("Failed to find correct image from camera '" + current.getName() + "' after " + 500 + " images");
        assertThat(current.matches(lastImage)).isEqualTo(true);
    }

    @Test
    public void shouldTransferImageFromSingleCamera() {
        consumeImages(1);
    }

    @Test
    public void shouldTransferMultipleImagesFromSingleCamera() {
        consumeImages(8);
    }

    @Test
    public void shouldTransferImagesAfterChangingCamera() {
        camera.switchToCamera(camera1.getName());
        consumeImages(8);
        camera.switchToCamera(camera2.getName());
        consumeImages(8);
    }
    
    protected void consumeImages( int count ) {
        try {
            for (int i = 0; i != count; ++i) {
                display.consumeImage(toBuffer());
                System.out.println("image[155] = " + lastImage.get(155));
            }
            assertConsumedMessageMatchesCameraImage();
        } catch ( IOException e ) {
            Fail.fail("Failed while consuming messages",e);
        }
    }

}
