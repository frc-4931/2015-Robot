/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.robot.component;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.frc4931.robot.mock.MockDataStream;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class RIODuinoTest {
    private MockDataStream dataStream;
    private RIODuino rioDuino;

    @Before
    public void beforeEach() {
        dataStream = new MockDataStream(1024, 1024, 1024);
        rioDuino = new RIODuino(dataStream);
    }

    @Test
    public void shouldWriteStackIndicatorInfoToStream() {
        dataStream.resetBuffers();
        rioDuino.sendStackIndicatorInfo((byte) 13, RIODuino.LightColor.BLUE);
        dataStream.testBuffers();

        byte data = dataStream.getByte();
        byte colorData = (byte) (data & 0x0F);
        byte heightData = (byte) (data >> 4 & 0x0F);

        assertThat(colorData).isEqualTo(RIODuino.LightColor.BLUE.getData());
        assertThat(heightData).isEqualTo((byte) 13);
    }
}
