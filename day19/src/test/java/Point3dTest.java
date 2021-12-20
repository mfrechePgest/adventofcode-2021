import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class Point3dTest {

    @Test
    public void compareDistances() {
        Point3d p1Ref1 = new Point3d(-618, -824, -621);
        Point3d p2Ref1 = new Point3d(-537, -823, -458);

        Point3d p1Ref2 = new Point3d(686, 422, 578);
        Point3d p2Ref2 = new Point3d(605, 423, 415);

        double distRef1 = p1Ref1.dist2(p2Ref1);
        double distRef2 = p1Ref2.dist2(p2Ref2);

        assertEquals(distRef1, distRef2);
    }


    @Test
    public void transform() {
        Point3d p1Ref1 = new Point3d(-618, -824, -621);
        Point3d p1Ref2 = new Point3d(686, 422, 578);

        Point3d p2Ref1 = new Point3d(-537, -823, -458);
        Point3d p2Ref2 = new Point3d(605, 423, 415);

        List<Function<Point3d, Point3d>> func = new PointPair(p1Ref1, p2Ref1).findDiffPoint(new PointPair(p1Ref2, p2Ref2));


        Point3d p3Ref1 = new Point3d(-447, -329, 318);
        List<Point3d> sums = func.stream().map(f -> f.apply(p3Ref1)).toList();
//        Point3d sum1 = func.get(0).apply(p3Ref1);
//        Point3d sum2 = func.get(1).apply(p3Ref1);

        Point3d p3Ref2 = new Point3d(515, 917, -361);


        assertTrue(sums.contains(p3Ref2));
    }


    @Test
    public void transformReversed() {
        Point3d p1Ref1 = new Point3d(-618, -824, -621);
        Point3d p1Ref2 = new Point3d(686, 422, 578);

        Point3d p2Ref1 = new Point3d(-537, -823, -458);
        Point3d p2Ref2 = new Point3d(605, 423, 415);

        List<Function<Point3d, Point3d>> func = new PointPair(p2Ref1, p1Ref1).findDiffPoint(new PointPair(p1Ref2, p2Ref2));

        Point3d p3Ref1 = new Point3d(-447, -329, 318);
        List<Point3d> sums = func.stream().map(f -> f.apply(p3Ref1)).toList();
//        Point3d sum1 = func.get(0).apply(p3Ref1);
//        Point3d sum2 = func.get(1).apply(p3Ref1);

        Point3d p3Ref2 = new Point3d(515, 917, -361);


        assertTrue(sums.contains(p3Ref2));
    }

    @Test
    public void rotation() {
        List<Point3d> lstPointsScan0 = Arrays.asList(
                new Point3d(-1, -1, 1),
                new Point3d(-2, -2, 2),
                new Point3d(-3, -3, 3),
                new Point3d(-2, -3, 1),
                new Point3d(5, 6, -4),
                new Point3d(8, 0, 7)
        );

        List<Point3d> lstPointsScan1 = Arrays.asList(
                new Point3d(1, -1, 1),
                new Point3d(2, -2, 2),
                new Point3d(3, -3, 3),
                new Point3d(2, -1, 3),
                new Point3d(-5, 4, -6),
                new Point3d(-8, -7, 0)
        );


        Function<Point3d, Point3d> func = Day19.findFunctionTransformBeacon(lstPointsScan0, lstPointsScan1);

        assertNotNull(func);
        for (int i = 0; i < lstPointsScan0.size(); i++) {
            assertEquals(lstPointsScan1.get(i), func.apply(lstPointsScan0.get(i)));
        }
    }

    @Test
    public void rotation2() {
        List<Point3d> lstPointsScan0 = Arrays.asList(
                new Point3d(-1, -1, 1),
                new Point3d(-2, -2, 2),
                new Point3d(-3, -3, 3),
                new Point3d(-2, -3, 1),
                new Point3d(5, 6, -4),
                new Point3d(8, 0, 7)
        );

        List<Point3d> lstPointsScan1 = Arrays.asList(
                new Point3d(-1, -1, -1),
                new Point3d(-2, -2, -2),
                new Point3d(-3, -3, -3),
                new Point3d(-1, -3, -2),
                new Point3d(4, 6, 5),
                new Point3d(-7, 0, 8)
        );


        Function<Point3d, Point3d> func = Day19.findFunctionTransformBeacon(lstPointsScan0, lstPointsScan1);

        assertNotNull(func);
        for (int i = 0; i < lstPointsScan0.size(); i++) {
            assertEquals(lstPointsScan1.get(i), func.apply(lstPointsScan0.get(i)));
        }
    }

    @Test
    public void rotation3() {
        List<Point3d> lstPointsScan0 = Arrays.asList(
                new Point3d(-1, -1, 1),
                new Point3d(-2, -2, 2),
                new Point3d(-3, -3, 3),
                new Point3d(-2, -3, 1),
                new Point3d(5, 6, -4),
                new Point3d(8, 0, 7)
        );

        List<Point3d> lstPointsScan1 = Arrays.asList(
                new Point3d(1, 1, -1),
                new Point3d(2, 2, -2),
                new Point3d(3, 3, -3),
                new Point3d(1, 3, -2),
                new Point3d(-4, -6, 5),
                new Point3d(7, 0, 8)
        );


        Function<Point3d, Point3d> func = Day19.findFunctionTransformBeacon(lstPointsScan0, lstPointsScan1);

        assertNotNull(func);
        for (int i = 0; i < lstPointsScan0.size(); i++) {
            assertEquals(lstPointsScan1.get(i), func.apply(lstPointsScan0.get(i)));
        }
    }

    @Test
    public void rotation4() {
        List<Point3d> lstPointsScan0 = Arrays.asList(
                new Point3d(-1, -1, 1),
                new Point3d(-2, -2, 2),
                new Point3d(-3, -3, 3),
                new Point3d(-2, -3, 1),
                new Point3d(5, 6, -4),
                new Point3d(8, 0, 7)
        );

        List<Point3d> lstPointsScan1 = Arrays.asList(
                new Point3d(1, 1, 1),
                new Point3d(2, 2, 2),
                new Point3d(3, 3, 3),
                new Point3d(3, 1, 2),
                new Point3d(-6, -4, -5),
                new Point3d(0, 7, -8)
        );


        Function<Point3d, Point3d> func = Day19.findFunctionTransformBeacon(lstPointsScan0, lstPointsScan1);

        assertNotNull(func);
        for (int i = 0; i < lstPointsScan0.size(); i++) {
            assertEquals(lstPointsScan1.get(i), func.apply(lstPointsScan0.get(i)));
        }
    }


}
