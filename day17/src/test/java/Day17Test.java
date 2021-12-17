import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day17Test {

    @Test
    public void sampleTest() {
        Day17 day17 = new Day17("target area: x=20..30, y=-10..-5");

        Integer result = day17.findBestThrow();

        Assertions.assertEquals(45, result);
    }

    @Test
    public void sampleTest2() {
        Day17 day17 = new Day17("target area: x=20..30, y=-10..-5");

        Integer result = day17.findEveryThrowInTarget();

        Assertions.assertEquals(112, result);
    }

}
