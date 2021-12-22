import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day22Test {

    @Test
    public void miniSampleTestStep1() throws IOException {
        Day22 day22 = new Day22();

        day22.processFile("minisample.txt");

        long cubesOn = day22.countCubesOn(-50, 50);

        assertEquals(39, cubesOn);
    }


    @Test
    public void miniSampleTruncatedStep1_1() throws IOException {
        Day22 day22 = new Day22();

        day22.processFile("minisample_truncated1.txt");

        long cubesOn = day22.countCubesOn(-50, 50);

        assertEquals(46, cubesOn);
    }

    @Test
    public void miniSampleTruncatedStep1_2() throws IOException {
        Day22 day22 = new Day22();

        day22.processFile("minisample_truncated2.txt");

        long cubesOn = day22.countCubesOn(-50, 50);

        assertEquals(38, cubesOn);
    }

    @Test
    public void sampleTestStep1() throws IOException {
        Day22 day22 = new Day22();

        day22.processFile("sample.txt");

        long cubesOn = day22.countCubesOn(-50, 50);

        assertEquals(590784, cubesOn);
    }

    @Test
    public void sampleTestStep2() throws IOException {
        Day22 day22 = new Day22();

        day22.processFile("sample2.txt");

        long cubesOn = day22.countCubesOn();

        assertEquals(2758514936282235L, cubesOn);
    }
}
