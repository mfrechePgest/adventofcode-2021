import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Day13Test {

    @Test
    public void sample_step1() throws IOException {
        // GIVEN
        Day13 day13 = new Day13("day13_sample.txt");

        // WHEN
        day13.foldOnce();

        // THEN
        Assertions.assertEquals(17, day13.getDotCount());
    }

    @Test
    public void sample_step2() throws IOException {
        // GIVEN
        Day13 day13 = new Day13("day13_sample.txt");

        // WHEN
        day13.foldAll();

        // THEN
        Assertions.assertEquals("""
                        #####
                        #...#
                        #...#
                        #...#
                        #####
                        .....
                        .....
                        """,
                day13.toStringColorized(false));
    }

}
