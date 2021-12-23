import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Day23Test {

    @Test
    public void testSample1_step1() throws IOException {
        Day23 day23 = new Day23("sample.txt", Step.STEP_1);

        Situation bestPath = day23.getBestPath();

        System.out.println("bestPath = " + bestPath);
        assertEquals(12521, bestPath.cost());
    }

    @Test
    public void testSample1_step2() throws IOException {
        Day23 day23 = new Day23("sample.txt", Step.STEP_2);

        Situation bestPath = day23.getBestPath();

        System.out.println("bestPath = " + bestPath);
        assertEquals(44169, bestPath.cost());
    }

    @Test
    public void testIsFinished() throws IOException {
        Day23 day23 = new Day23("input.txt", Step.STEP_1);

        assertFalse(day23.initialSituation.isFinished());
    }
}
