import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChamberTest {

    @Test
    public void moveFrom0To1() {
        Chamber c = new Chamber(0, 2);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(1);

        assertEquals(1, res.size());
        assertEquals(2, res.get(0));
    }


    @Test
    public void moveFrom1To0() {
        Chamber c = new Chamber(1, 2);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(0);

        assertEquals(1, res.size());
        assertEquals(2, res.get(0));
    }

    @Test
    public void moveFrom1To3() {
        Chamber c = new Chamber(1, 2);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(3);

        assertEquals(2, res.size());
        assertEquals(3, res.get(0));
        assertEquals(4, res.get(1));
    }

    @Test
    public void moveFrom0To2() {
        Chamber c = new Chamber(0, 2);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(2);

        assertEquals(2, res.size());
        assertEquals(2, res.get(0));
        assertEquals(3, res.get(1));
    }

    @Test
    public void moveFrom1ToEmpty2() {
        Chamber c = new Chamber(1, 2);
        c.add(Amphipod.C);
        c.add(Amphipod.C);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(2);

        assertEquals(1, res.size());
        assertEquals(3, res.get(0));

        int cost = c.canMoveToOtherChamber(2, new Situation(
                Arrays.asList(new Chamber(0, 2),
                        c,
                        new Chamber(2, 2),
                        new Chamber(3, 2)
                ),
                new Parkings(), 0, null)
        );
        assertEquals(500, cost);
    }

    @Test
    public void moveFrom1To2() {
        Chamber c = new Chamber(1, 2);
        c.add(Amphipod.A);
        c.add(Amphipod.C);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(2);

        assertEquals(1, res.size());
        assertEquals(3, res.get(0));

        Chamber c2 = new Chamber(2, 2);
        c2.add(Amphipod.C);
        int cost = c.canMoveToOtherChamber(2, new Situation(
                Arrays.asList(new Chamber(0, 2),
                        c,
                        c2,
                        new Chamber(3, 2)
                ),
                new Parkings(), 0, null));
        assertEquals(400, cost);
    }

    @Test
    public void moveFromHalf1To2() {
        Chamber c = new Chamber(1, 2);
        c.add(Amphipod.C);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(2);

        assertEquals(1, res.size());
        assertEquals(3, res.get(0));

        Chamber c2 = new Chamber(2, 2);
        c2.add(Amphipod.C);
        int cost = c.canMoveToOtherChamber(2, new Situation(
                Arrays.asList(new Chamber(0, 2),
                        c,
                        c2,
                        new Chamber(3, 2)
                ),
                new Parkings(), 0, null));
        assertEquals(500, cost);
    }

    @Test
    public void moveFrom0ToParking2() {
        Chamber c = new Chamber(0, 2);
        c.add(Amphipod.A);
        c.add(Amphipod.A);

        List<Integer> res = c.hasToMoveThroughToParking(2);

        assertEquals(1, res.size());
        assertEquals(2, res.get(0));

        assertEquals(2, c.canMoveToParking(2, new Parkings()));
    }

    @Test
    public void moveFrom0ToParking6() {
        Chamber c = new Chamber(0, 2);
        c.add(Amphipod.A);
        c.add(Amphipod.A);

        List<Integer> res = c.hasToMoveThroughToParking(6);

        assertEquals(5, res.size());
        assertEquals(2, res.get(0));
        assertEquals(3, res.get(1));
        assertEquals(4, res.get(2));
        assertEquals(5, res.get(3));
        assertEquals(6, res.get(4));

        assertEquals(9, c.canMoveToParking(6, new Parkings()));
    }

    @Test
    public void moveFrom0ToParking6_withD() {
        Chamber c = new Chamber(0, 2);
        c.add(Amphipod.D);
        c.add(Amphipod.D);

        List<Integer> res = c.hasToMoveThroughToParking(6);

        assertEquals(5, res.size());
        assertEquals(2, res.get(0));
        assertEquals(3, res.get(1));
        assertEquals(4, res.get(2));
        assertEquals(5, res.get(3));
        assertEquals(6, res.get(4));

        assertEquals(9000, c.canMoveToParking(6, new Parkings()));
    }

    @Test
    public void moveFrom2ToParking1() {
        Chamber c = new Chamber(2, 2);

        List<Integer> res = c.hasToMoveThroughToParking(1);

        assertEquals(3, res.size());
        assertEquals(3, res.get(0));
        assertEquals(2, res.get(1));
        assertEquals(1, res.get(2));
    }
}
