/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.vision;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

import org.frc4931.robot.vision.Camera.Size;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Acts as the display for a remote device consuming images from the {@link CameraServer}'s M-JPEG server.
 */
public class RemoteDisplay {

    private volatile Size desiredSize = Size.SMALL;
    private volatile int framesPerSecond = 5;
    private final InetAddress address;
    private final int port;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public RemoteDisplay(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        this.framesPerSecond = framesPerSecond;
    }

    public void setImageSize(Size size) {
        assert size != null;
        this.desiredSize = size;
    }

    public void setFramesPerSecond(int framesPerSecond) {
        assert framesPerSecond > 0;
        this.framesPerSecond = framesPerSecond;
    }

    public void connect() throws IOException {
        if (socket == null) {
            socket = new Socket(address, port);
            input = new DataInputStream(socket.getInputStream());
            // Write the request that is needed by the server ...
            output = new DataOutputStream(socket.getOutputStream());
            output.writeInt(framesPerSecond); // frames per second
            output.writeInt(-1); // compression; use hardware
            output.writeInt(desiredSize.getCode()); // image size (0=large,1=medium,2=small)
            output.flush();
        }
    }

    public void disconnect() throws IOException {
        if (socket != null) {
            if (input != null) {
                try {
                    input.close();
                } finally {
                    input = null;
                    try {
                        output.close();
                    } finally {
                        output = null;
                        try {
                            socket.close();
                        } finally {
                            socket = null;
                        }
                    }
                }
            }
        }
    }

    public void consumeImage(Consumer<ByteBuffer> imageConsumer) throws IOException {
        // Read 4 bytes that StoppableCameraServer.send() includes ...
        byte[] wpiHeader = new byte[4];
        input.read(wpiHeader);
        assertThat(wpiHeader[0]).isEqualTo(CameraServer.MAGIC_HEADER[0]);
        assertThat(wpiHeader[1]).isEqualTo(CameraServer.MAGIC_HEADER[1]);
        assertThat(wpiHeader[2]).isEqualTo(CameraServer.MAGIC_HEADER[2]);
        assertThat(wpiHeader[3]).isEqualTo(CameraServer.MAGIC_HEADER[3]);

        // and the length of the image ...
        int length = input.readInt();

        // read the image ...
        byte[] imageBytes = new byte[length];
        int offset = 0;
        int numRead = 0;
        while (offset < imageBytes.length && (numRead = input.read(imageBytes, offset, imageBytes.length - offset)) >= 0) {
            offset += numRead;
        }
        ByteBuffer result = ByteBuffer.wrap(imageBytes);
        imageConsumer.accept(result);
    }
}
