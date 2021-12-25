import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day25Test {

    @Test
    public void simpleTest() throws IOException {
        Day25 day25 = new Day25("simple.txt");

        day25.turn();

        assertEquals("""
                ..vv>..
                .......
                >......
                v.....>
                >......
                .......
                ....v..
                """, day25 +"\n");

    }


    @Test
    public void sampleTest() throws IOException {
        Day25 day25 = new Day25("sample.txt");

        day25.turn();

        assertEquals("""
                ....>.>v.>
                v.v>.>v.v.
                >v>>..>v..
                >>v>v>.>.v
                .>v.v...v.
                v>>.>vvv..
                ..v...>>..
                vv...>>vv.
                >.v.v..v.v
                """, day25 + "\n");

        day25.turn();

        assertEquals("""
                >.v.v>>..v
                v.v.>>vv..
                >v>.>.>.v.
                >>v>v.>v>.
                .>..v....v
                .>v>>.v.v.
                v....v>v>.
                .vv..>>v..
                v>.....vv.
                """, day25 + "\n");

    }

    @Test
    public void sampleMoveTillBlocked() throws IOException {
        Day25 day25 = new Day25("sample.txt");

        int nbTurn = day25.moveTillBlocked();

        assertEquals(58, nbTurn);

    }
}
