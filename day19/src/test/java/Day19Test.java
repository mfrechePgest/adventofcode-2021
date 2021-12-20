import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class Day19Test {


    @Test
    public void findCommonBeacons0and1() throws IOException {
        Day19 day19 = new Day19("sample.txt");


        Set<Point3d> beacons0 = new HashSet<>(day19.getBeacons(0));
        Function<Point3d, Point3d> func = Day19.findFunctionTransformBeacon(day19.getBeacons(1), day19.getBeacons(0));


        List<Point3d> commonPoints = day19.getBeacons(1).stream()
                .map(func)
                .filter(beacons0::contains)
                .toList();


        assertEquals(12, commonPoints.size());

        List<Point3d> expectedCommonPoints = Arrays.asList(
                new Point3d(-618, -824, -621),
                new Point3d(-537, -823, -458),
                new Point3d(-447, -329, 318),
                new Point3d(404, -588, -901),
                new Point3d(544, -627, -890),
                new Point3d(528, -643, 409),
                new Point3d(-661, -816, -575),
                new Point3d(390, -675, -793),
                new Point3d(423, -701, 434),
                new Point3d(-345, -311, 381),
                new Point3d(459, -707, 401),
                new Point3d(-485, -357, 347)
        );
        commonPoints.forEach(common ->
                assertTrue(expectedCommonPoints.contains(common), "Ne fait pas partie des valeurs attendues " + common)
        );
        expectedCommonPoints.forEach(expected ->
                assertTrue(commonPoints.contains(expected), "Devrait contenir " + expected)
        );

        Point3d scanner1Pos = func.apply(new Point3d(0, 0, 0));
        assertEquals(new Point3d(68, -1246, -43), scanner1Pos);
    }

    @Test
    public void findCommonBeacons1and4() throws IOException {
        Day19 day19 = new Day19("sample.txt");


        Function<Point3d, Point3d> func1to0 = Day19.findFunctionTransformBeacon(day19.getBeacons(1), day19.getBeacons(0));

        Set<Point3d> beacons1 = day19.getBeacons(1).stream().map(func1to0).collect(Collectors.toSet());
        Function<Point3d, Point3d> func = Day19.findFunctionTransformBeacon(day19.getBeacons(4), beacons1);

        assertNotNull(func);

        List<Point3d> commonPoints = day19.getBeacons(4).stream()
                .map(func)
                .filter(beacons1::contains)
                .toList();


        assertEquals(12, commonPoints.size());

        List<Point3d> expectedCommonPoints = Arrays.asList(
                new Point3d(459, -707, 401),
                new Point3d(-739, -1745, 668),
                new Point3d(-485, -357, 347),
                new Point3d(432, -2009, 850),
                new Point3d(528, -643, 409),
                new Point3d(423, -701, 434),
                new Point3d(-345, -311, 381),
                new Point3d(408, -1815, 803),
                new Point3d(534, -1912, 768),
                new Point3d(-687, -1600, 576),
                new Point3d(-447, -329, 318),
                new Point3d(-635, -1737, 486)
        );
        commonPoints.forEach(common ->
                assertTrue(expectedCommonPoints.contains(common), "Ne fait pas partie des valeurs attendues " + common)
        );
        expectedCommonPoints.forEach(expected ->
                assertTrue(commonPoints.contains(expected), "Devrait contenir " + expected)
        );

        Point3d scanner1Pos = func.apply(new Point3d(0, 0, 0));
        assertEquals(new Point3d(-20, -1133, 1061), scanner1Pos);
    }


    @Test
    public void findTotalBeaconCount() throws IOException {
        Day19 day19 = new Day19("sample.txt");

        List<Point3d> totalBeacons = day19.findTotalBeacons();
        int totalBeaconCount = totalBeacons.size();

        assertEquals(79, totalBeaconCount);
    }

    @Test
    public void fullSample() throws IOException {
        Day19 day19 = new Day19("sample.txt");

        Function<Point3d, Point3d> func1to0 = Day19.findFunctionTransformBeacon(day19.getBeacons(1), day19.getBeacons(0));
        assertNotNull(func1to0);
        Set<Point3d> beacons1In0Pov = day19.getBeacons(1).stream().map(func1to0).collect(Collectors.toSet());
        assertEquals(new Point3d(68, -1246, -43), func1to0.apply(new Point3d(0,0,0)));

        Function<Point3d, Point3d> func4To0 = Day19.findFunctionTransformBeacon(day19.getBeacons(4), beacons1In0Pov);
        assertNotNull(func4To0);
        Set<Point3d> beacons4In0Pov = day19.getBeacons(4).stream().map(func4To0).collect(Collectors.toSet());
        assertEquals(new Point3d(-20, -1133, 1061), func4To0.apply(new Point3d(0,0,0)));

        Function<Point3d, Point3d> func3To0 = Day19.findFunctionTransformBeacon(day19.getBeacons(3), beacons1In0Pov);
        assertNotNull(func3To0);
        Set<Point3d> beacons3In0Pov = day19.getBeacons(3).stream().map(func3To0).collect(Collectors.toSet());
        assertEquals(new Point3d(-92, -2380, -20), func3To0.apply(new Point3d(0,0,0)));

        Function<Point3d, Point3d> func2To0 = Day19.findFunctionTransformBeacon(day19.getBeacons(2), beacons3In0Pov);
        assertNotNull(func3To0);
        Set<Point3d> beacons2In0Pov = day19.getBeacons(2).stream().map(func2To0).collect(Collectors.toSet());
        assertEquals(new Point3d(1105, -1205, 1229), func2To0.apply(new Point3d(0,0,0)));

        Set<Point3d> totalBeacons = new HashSet<>();
        totalBeacons.addAll(day19.getBeacons(0));
        totalBeacons.addAll(beacons1In0Pov);
        totalBeacons.addAll(beacons4In0Pov);
        totalBeacons.addAll(beacons3In0Pov);
        totalBeacons.addAll(beacons2In0Pov);

        assertEquals(79, totalBeacons.size());

        List<Point3d> expectedFinalPoints = Arrays.asList(
                new Point3d(-892, 524, 684),
                new Point3d(-876, 649, 763),
                new Point3d(-838, 591, 734),
                new Point3d(-789, 900, -551),
                new Point3d(-739, -1745, 668),
                new Point3d(-706, -3180, -659),
                new Point3d(-697, -3072, -689),
                new Point3d(-689, 845, -530),
                new Point3d(-687, -1600, 576),
                new Point3d(-661, -816, -575),
                new Point3d(-654, -3158, -753),
                new Point3d(-635, -1737, 486),
                new Point3d(-631, -672, 1502),
                new Point3d(-624, -1620, 1868),
                new Point3d(-620, -3212, 371),
                new Point3d(-618, -824, -621),
                new Point3d(-612, -1695, 1788),
                new Point3d(-601, -1648, -643),
                new Point3d(-584, 868, -557),
                new Point3d(-537, -823, -458),
                new Point3d(-532, -1715, 1894),
                new Point3d(-518, -1681, -600),
                new Point3d(-499, -1607, -770),
                new Point3d(-485, -357, 347),
                new Point3d(-470, -3283, 303),
                new Point3d(-456, -621, 1527),
                new Point3d(-447, -329, 318),
                new Point3d(-430, -3130, 366),
                new Point3d(-413, -627, 1469),
                new Point3d(-345, -311, 381),
                new Point3d(-36, -1284, 1171),
                new Point3d(-27, -1108, -65),
                new Point3d(7, -33, -71),
                new Point3d(12, -2351, -103),
                new Point3d(26, -1119, 1091),
                new Point3d(346, -2985, 342),
                new Point3d(366, -3059, 397),
                new Point3d(377, -2827, 367),
                new Point3d(390, -675, -793),
                new Point3d(396, -1931, -563),
                new Point3d(404, -588, -901),
                new Point3d(408, -1815, 803),
                new Point3d(423, -701, 434),
                new Point3d(432, -2009, 850),
                new Point3d(443, 580, 662),
                new Point3d(455, 729, 728),
                new Point3d(456, -540, 1869),
                new Point3d(459, -707, 401),
                new Point3d(465, -695, 1988),
                new Point3d(474, 580, 667),
                new Point3d(496, -1584, 1900),
                new Point3d(497, -1838, -617),
                new Point3d(527, -524, 1933),
                new Point3d(528, -643, 409),
                new Point3d(534, -1912, 768),
                new Point3d(544, -627, -890),
                new Point3d(553, 345, -567),
                new Point3d(564, 392, -477),
                new Point3d(568, -2007, -577),
                new Point3d(605, -1665, 1952),
                new Point3d(612, -1593, 1893),
                new Point3d(630, 319, -379),
                new Point3d(686, -3108, -505),
                new Point3d(776, -3184, -501),
                new Point3d(846, -3110, -434),
                new Point3d(1135, -1161, 1235),
                new Point3d(1243, -1093, 1063),
                new Point3d(1660, -552, 429),
                new Point3d(1693, -557, 386),
                new Point3d(1735, -437, 1738),
                new Point3d(1749, -1800, 1813),
                new Point3d(1772, -405, 1572),
                new Point3d(1776, -675, 371),
                new Point3d(1779, -442, 1789),
                new Point3d(1780, -1548, 337),
                new Point3d(1786, -1538, 337),
                new Point3d(1847, -1591, 415),
                new Point3d(1889, -1729, 1762),
                new Point3d(1994, -1805, 1792)
        );

        totalBeacons.forEach(finalPoint ->
                assertTrue(expectedFinalPoints.contains(finalPoint), "Ne fait pas partie des valeurs attendues " + finalPoint)
        );
        expectedFinalPoints.forEach(expected ->
                assertTrue(totalBeacons.contains(expected), "Devrait contenir " + expected)
        );

    }


}
