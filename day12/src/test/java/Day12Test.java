import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class Day12Test {

    @Test
    public void sample1_step1() throws IOException {
        assertDay12("day12_sample_1.txt",Day12.Step.STEP_1,10);
    }

    @Test
    public void sample2_step1() throws IOException {
        assertDay12("day12_sample_2.txt",Day12.Step.STEP_1,19);
    }

    @Test
    public void sample3_step1() throws IOException {
        assertDay12("day12_sample_3.txt",Day12.Step.STEP_1,226);
    }

    @Test
    public void sample1_step2() throws IOException {
        assertDay12("day12_sample_1.txt",Day12.Step.STEP_2,36);
    }

    @Test
    public void sample2_step2() throws IOException {
        assertDay12("day12_sample_2.txt",Day12.Step.STEP_2,103);
    }

    @Test
    public void sample3_step2() throws IOException {
        assertDay12("day12_sample_3.txt",Day12.Step.STEP_2,3509);
    }


    private void assertDay12(String fileName, Day12.Step stepMode, int expectedPathCount) throws IOException {
        Day12 day12 = new Day12(fileName);
        List<Day12.Path> paths = day12.findPathFromStartToEnd(stepMode);

        Assertions.assertEquals(expectedPathCount, paths.size());
    }

}
