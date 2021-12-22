import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day22Test {

    @Test
    public void sampleTestStep1() throws IOException {
        Day22 day22 = new Day22(-50, 50);

        day22.processFile("sample.txt");

        long cubesOn = day22.countCubesOn();

        assertEquals(590784, cubesOn);
    }

    @Test
    public void sampleTestStep2() throws IOException {
        Day22 day22 = new Day22(-100000, 100000);

        day22.processFile("sample2.txt");

        long cubesOn = day22.countCubesOn();

        assertEquals(2758514936282235L, cubesOn);
    }
}
