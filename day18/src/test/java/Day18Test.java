import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day18Test {

    @Test
    public void sample1() throws IOException {
        Day18 day18 = new Day18("sample1.txt");

        Element finalSum = day18.finalSum();

        assertEquals("[[[[1,1],[2,2]],[3,3]],[4,4]]", finalSum.toString());
    }


    @Test
    public void sample2() throws IOException {
        Day18 day18 = new Day18("sample2.txt");

        Element finalSum = day18.finalSum();

        assertEquals("[[[[3,0],[5,3]],[4,4]],[5,5]]", finalSum.toString());
    }

    @Test
    public void sample3() throws IOException {
        Day18 day18 = new Day18("sample3.txt");

        Element finalSum = day18.finalSum();

        assertEquals("[[[[5,0],[7,4]],[5,5]],[6,6]]", finalSum.toString());
    }

    @Test
    public void sample4() throws IOException {
        Day18 day18 = new Day18("sample4.txt");

        Element finalSum = day18.finalSum();

        assertEquals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", finalSum.toString());
    }

    @Test
    public void sample5() throws IOException {
        Day18 day18 = new Day18("sample5.txt");

        Element finalSum = day18.finalSum();

        assertEquals("[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]", finalSum.toString());
        assertEquals(4140, finalSum.getMagnitude());
    }

    @Test
    public void sample5_largestMagnitude() throws IOException {
        Day18 day18 = new Day18("sample5.txt");

        Element largestMagnitude = day18.largestMagnitude();

        assertEquals("[[[[7,8],[6,6]],[[6,0],[7,7]]],[[[7,8],[8,8]],[[7,9],[0,6]]]]", largestMagnitude.toString());
        assertEquals(3993, largestMagnitude.getMagnitude());
    }

}
