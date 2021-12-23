import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChamberTest {

    @Test
    public void moveFrom0To1() {
        Chamber c = new Chamber(0);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(1);

        assertEquals(1, res.size());
        assertEquals(2, res.get(0));
    }


    @Test
    public void moveFrom1To0() {
        Chamber c = new Chamber(1);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(0);

        assertEquals(1, res.size());
        assertEquals(2, res.get(0));
    }

    @Test
    public void moveFrom1To3() {
        Chamber c = new Chamber(1);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(3);

        assertEquals(2, res.size());
        assertEquals(3, res.get(0));
        assertEquals(4, res.get(1));
    }

    @Test
    public void moveFrom0To2() {
        Chamber c = new Chamber(0);

        List<Integer> res = c.hasToMoveThroughToOtherChamber(2);

        assertEquals(2, res.size());
        assertEquals(2, res.get(0));
        assertEquals(3, res.get(1));
    }

    @Test
    public void moveFrom0ToParking2() {
        Chamber c = new Chamber(0);
        c.add(Amphipod.A);
        c.add(Amphipod.A);

        List<Integer> res = c.hasToMoveThroughToParking(2);

        assertEquals(1, res.size());
        assertEquals(2, res.get(0));

        assertEquals(2, c.canMove(2, new Parkings()));
    }

    @Test
    public void moveFrom0ToParking6() {
        Chamber c = new Chamber(0);
        c.add(Amphipod.A);
        c.add(Amphipod.A);

        List<Integer> res = c.hasToMoveThroughToParking(6);

        assertEquals(5, res.size());
        assertEquals(2, res.get(0));
        assertEquals(3, res.get(1));
        assertEquals(4, res.get(2));
        assertEquals(5, res.get(3));
        assertEquals(6, res.get(4));

        assertEquals(9, c.canMove(6, new Parkings()));
    }

    @Test
    public void moveFrom0ToParking6_withD() {
        Chamber c = new Chamber(0);
        c.add(Amphipod.D);
        c.add(Amphipod.D);

        List<Integer> res = c.hasToMoveThroughToParking(6);

        assertEquals(5, res.size());
        assertEquals(2, res.get(0));
        assertEquals(3, res.get(1));
        assertEquals(4, res.get(2));
        assertEquals(5, res.get(3));
        assertEquals(6, res.get(4));

        assertEquals(9000, c.canMove(6, new Parkings()));
    }

    @Test
    public void moveFrom2ToParking1() {
        Chamber c = new Chamber(2);

        List<Integer> res = c.hasToMoveThroughToParking(1);

        assertEquals(3, res.size());
        assertEquals(3, res.get(0));
        assertEquals(2, res.get(1));
        assertEquals(1, res.get(2));
    }
}
