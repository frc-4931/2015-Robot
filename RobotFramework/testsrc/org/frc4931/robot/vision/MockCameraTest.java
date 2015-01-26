/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.vision;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.frc4931.robot.vision.Camera.Size;
import org.frc4931.robot.vision.MockCamera.Content;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 
 */
public class MockCameraTest {

    private MockCamera camera;
    
    @Before
    public void beforeEach() {
        camera = new MockCamera();
    }
    
    protected void assertCameraMatchesExpectedImage( Content content, Size size ) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(MockCamera.image(content,size));
        assertThat(camera.matches(buffer)).isEqualTo(true);
    }
    
    protected void assertCameraDoesNotMatchImage( Content content, Size size ) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(MockCamera.image(content,size));
        assertThat(camera.matches(buffer)).isEqualTo(false);
    }
    
    @Test
    public void shouldUseCorrectSmallSpaceImage() throws IOException {
        camera.setContent(Content.SPACE);
        camera.setSize(Size.SMALL);
        assertCameraMatchesExpectedImage(Content.SPACE,Size.SMALL);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.LARGE);
        assertCameraDoesNotMatchImage(Content.GRID,Size.SMALL);
        assertCameraDoesNotMatchImage(Content.GRID,Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.GRID,Size.LARGE);
    }
    
    @Test
    public void shouldUseCorrectMediumSpaceImage() throws IOException {
        camera.setContent(Content.SPACE);
        camera.setSize(Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.SMALL);
        assertCameraMatchesExpectedImage(Content.SPACE,Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.LARGE);
        assertCameraDoesNotMatchImage(Content.GRID,Size.SMALL);
        assertCameraDoesNotMatchImage(Content.GRID,Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.GRID,Size.LARGE);
    }
    
    @Test
    public void shouldUseCorrectLargeSpaceImage() throws IOException {
        camera.setContent(Content.SPACE);
        camera.setSize(Size.LARGE);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.SMALL);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.MEDIUM);
        assertCameraMatchesExpectedImage(Content.SPACE,Size.LARGE);
        assertCameraDoesNotMatchImage(Content.GRID,Size.SMALL);
        assertCameraDoesNotMatchImage(Content.GRID,Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.GRID,Size.LARGE);
    }
    
    @Test
    public void shouldUseCorrectSmallGridImage() throws IOException {
        camera.setContent(Content.GRID);
        camera.setSize(Size.SMALL);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.SMALL);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.LARGE);
        assertCameraMatchesExpectedImage(Content.GRID,Size.SMALL);
        assertCameraDoesNotMatchImage(Content.GRID,Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.GRID,Size.LARGE);
    }
    
    @Test
    public void shouldUseCorrectMediumGridImage() throws IOException {
        camera.setContent(Content.GRID);
        camera.setSize(Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.SMALL);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.LARGE);
        assertCameraDoesNotMatchImage(Content.GRID,Size.SMALL);
        assertCameraMatchesExpectedImage(Content.GRID,Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.GRID,Size.LARGE);
    }
    
    @Test
    public void shouldUseCorrectLargeGridImage() throws IOException {
        camera.setContent(Content.GRID);
        camera.setSize(Size.LARGE);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.SMALL);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.MEDIUM);
        assertCameraDoesNotMatchImage(Content.SPACE,Size.LARGE);
        assertCameraDoesNotMatchImage(Content.GRID,Size.SMALL);
        assertCameraDoesNotMatchImage(Content.GRID,Size.MEDIUM);
        assertCameraMatchesExpectedImage(Content.GRID,Size.LARGE);
    }

    @Ignore
    @Test
    public void shouldFindDifferentBytesAtSamePositionInEachContent() throws IOException {
        byte[] space = MockCamera.image(Content.SPACE,Size.SMALL);
        byte[] grid = MockCamera.image(Content.GRID,Size.SMALL);
        for ( int i=0; i!=14000; ++i) {
            if ( space[i] != grid[i] ) {
                System.out.println("space[" + i + "]=" + space[i] + "    grid[" + i + "]=" + grid[i]);
            }
        }
    }

}
