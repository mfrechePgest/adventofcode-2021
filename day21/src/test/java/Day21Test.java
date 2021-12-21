import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day21Test {


    @Test
    public void play1Turn() {
        Step1 day21 = new Step1(4,8);

        day21.playOneTurn(true);

        assertEquals(3, day21.getNbDiceRolls());
        assertEquals(10, day21.getPlayer1Score());

        day21.playOneTurn(false);
        assertEquals(6, day21.getNbDiceRolls());
        assertEquals(10, day21.getPlayer1Score());
        assertEquals(3, day21.getPlayer2Score());
    }

    @Test
    public void sampleTest() {
        Step1 day21 = new Step1(4,8);

        day21.playUntilOneReach1000();

        assertEquals(745, day21.getPlayer2Score());
        assertEquals(993, day21.getNbDiceRolls());
    }

    @Test
    public void sampleTestSplitUniverses() {
        Step2 day21 = new Step2(4,8);

        long[] result = day21.playSplittingUniverses();

        System.out.println("result = " + Arrays.toString(result));

        assertEquals(444356092776315L, result[0]);
        assertEquals(341960390180808L, result[1]);
    }
}
