/*
 * FRC 4931 (http://www.evilletech.com)
 * 
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.vision;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * A specialization of {@link USBCamera} that can be easily tested.
 */
public class MockCamera extends Camera {

    public static enum Content {
        SPACE, GRID
    }

    public static enum Size {
        LARGE(0), MEDIUM(1), SMALL(2);
        private int option;

        private Size(int option) {
            this.option = option;
        }

        public int getCode() {
            return option;
        }
    }

    private static final String IMAGE_PATH = "org/frc4931/robot/vision/";
    private static final String IMAGE1_SMALL = IMAGE_PATH + "sample1-160x120.jpg";
    private static final String IMAGE1_MEDIUM = IMAGE_PATH + "sample1-320x240.jpg";
    private static final String IMAGE1_LARGE = IMAGE_PATH + "sample1-640x480.jpg";
    private static final String IMAGE2_SMALL = IMAGE_PATH + "sample2-160x120.jpg";
    private static final String IMAGE2_MEDIUM = IMAGE_PATH + "sample2-320x240.jpg";
    private static final String IMAGE2_LARGE = IMAGE_PATH + "sample2-640x480.jpg";

    private static final Map<Content, Map<Size, String>> PATHS_BY_IMAGE_AND_SIZE;
    static {
        Map<Size, String> space = new HashMap<>();
        space.put(Size.SMALL, IMAGE1_SMALL);
        space.put(Size.MEDIUM, IMAGE1_MEDIUM);
        space.put(Size.LARGE, IMAGE1_LARGE);
        Map<Size, String> test = new HashMap<>();
        test.put(Size.SMALL, IMAGE2_SMALL);
        test.put(Size.MEDIUM, IMAGE2_MEDIUM);
        test.put(Size.LARGE, IMAGE2_LARGE);
        Map<Content, Map<Size, String>> map = new HashMap<>();
        map.put(Content.SPACE, space);
        map.put(Content.GRID, test);
        PATHS_BY_IMAGE_AND_SIZE = Collections.unmodifiableMap(map);
    }

    protected static byte[] image(Content content, Size size) throws IOException {
        assert content != null;
        assert size != null;
        String path = PATHS_BY_IMAGE_AND_SIZE.get(content).get(size);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (DataInputStream input = new DataInputStream(MockCamera.class.getClassLoader().getResourceAsStream(path))) {
            int nRead = 0;
            byte[] data = new byte[16384];
            while ((nRead = input.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        }
        return buffer.toByteArray();
    }

    private Content content = Content.SPACE;
    private Size size = Size.SMALL;

    public MockCamera() {
        super("Test camera");
    }

    public MockCamera(String name) {
        super(name);
    }

    @Override
    public synchronized void openCamera() {
        // do nothing!
    }

    @Override
    public synchronized void closeCamera() {
        // do nothing!
    }

    public Content getContent() {
        return content;
    }

    public Size getSize() {
        return size;
    }

    public void setContent(Content content) {
        assert content != null;
        this.content = content;
    }

    public void setSize(Size size) {
        assert size != null;
        this.size = size;
    }

    @Override
    public synchronized void getImage(Image image) {
        super.getImage(image);
    }

    @Override
    public synchronized void getImageData(ByteBuffer data) {
        try {
            byte[] imageData = image(content, size);
            data.put(imageData);
            data.limit(imageData.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Determine if the current image matches the image specified by the given {@link ByteBuffer}.
     * 
     * @param data the image to which the current image should be compared; may not be null
     * @return true if the images are identical, or false otherwise
     */
    public boolean matches(ByteBuffer data) {
        try {
            byte[] imageData = image(content, size);
            data.position(0);
            for (int i = 0; i != imageData.length; ++i) {
                byte expected = imageData[i];
                byte actual = data.get();
                if (actual != expected) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
