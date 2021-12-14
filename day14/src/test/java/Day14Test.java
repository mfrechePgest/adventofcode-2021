import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Day14Test {


    @Test
    public void sample1_step1() throws IOException {
        // GIVEN
        Day14 day14 = new Day14("day14_sample.txt");

        // WHEN
        for (int i = 0; i < 10; i++) {
            day14.stepOverStrings();
        }
        long result = day14.result();

        // THEN
        Assertions.assertEquals(1588L, result);
    }


    @Test
    public void sample1_step2() throws IOException {
        // GIVEN
        Day14 day14 = new Day14("day14_sample.txt");

        // WHEN
        for (int i = 0; i < 40; i++) {
            day14.stepOverStrings();
        }
        long result = day14.result();

        // THEN
        Assertions.assertEquals(2188189693529L, result);
    }
}
