import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class Day24Test {

    @Test
    public void smallestTest() throws IOException {
        Day24 day24 = new Day24("smallest.txt");

        FourDimensions fd = day24.apply("1234");

        assertEquals(0, fd.w());
        assertEquals(-1, fd.x());
        assertEquals(0, fd.y());
        assertEquals(0, fd.z());
    }

    @Test
    public void smallTest() throws IOException {
        Day24 day24 = new Day24("small.txt");

        FourDimensions fd = day24.apply("1234");

        assertEquals(0, fd.w());
        assertEquals(2, fd.x());
        assertEquals(0, fd.y());
        assertEquals(0, fd.z());
    }

    @Test
    public void smallTest2() throws IOException {
        Day24 day24 = new Day24("small.txt");

        FourDimensions fd = day24.apply("1334");

        assertEquals(0, fd.w());
        assertEquals(3, fd.x());
        assertEquals(0, fd.y());
        assertEquals(1, fd.z());
    }

    @Test
    public void sampleTest() throws IOException {
        Day24 day24 = new Day24("sample.txt");

        FourDimensions result = day24.apply("13579246899999");

        System.out.println("result = " + result);

    }

}
