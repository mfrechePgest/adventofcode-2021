import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day19 extends AbstractDay {

    private int currentScanner = 0;
    private Map<Integer, List<Point3d>> scanMap = new HashMap<>();


    public static void main(String[] args) throws IOException {
        Day19 day19 = new Day19("input.txt");
        Cartographie carto = day19.findTotalBeacons();
        int totalBeacon = carto.beacons.size();
        System.out.println("totalBeacon = " + totalBeacon);
        System.out.println("largest manathan dist = " + Day19.findLargestDistance(carto.scanners));
    }

    public Day19(String fileName) throws IOException {
        this.openFile(fileName);
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }

    public static long findLargestDistance(List<Point3d> scanners) {
        return scanners.stream()
                .flatMap(s -> scanners.stream().map(otherPoint -> s.manathanDistance(otherPoint)))
                .mapToLong(l -> l)
                .max().orElse(0);
    }


    @Override
    public void readLine() throws IOException {
        if (!currentLine.isBlank()) {
            if (currentLine.startsWith("---")) {
                currentScanner = Integer.parseInt(currentLine.split(" ")[2]);
                scanMap.put(currentScanner, new ArrayList<>());
            } else {
                scanMap.get(currentScanner).add(Point3d.fromString(currentLine));
            }
        }
        currentLine = br.readLine();
    }

    public String toString() {
        return scanMap.entrySet().stream()
                .flatMap(entry -> Stream.concat(Stream.of("--- scanner " + entry.getKey() + " ---"),
                        entry.getValue().stream().map(Point3d::toString)
                )).collect(Collectors.joining("\n"));
    }

    public static Function<Point3d, Point3d> findFunctionTransformBeacon(Collection<Point3d> beacons1, Collection<Point3d> beacons2) {
        Map<Double, List<PointPair>> pairs1 = findAllPairs(beacons1);
        Map<Double, List<PointPair>> pairs2 = findAllPairs(beacons2);


        List<Function<Point3d, Point3d>> maybeFunc = null;
        for (Double dist : pairs1.keySet()) {
            if (pairs2.containsKey(dist)) {
                if (pairs2.get(dist).size() == 1 && pairs1.get(dist).size() == 1) {
                    // Trouver la conversion
                    PointPair pair1 = pairs1.get(dist).get(0);
                    PointPair pair2 = pairs2.get(dist).get(0);
                    if (maybeFunc == null) {
                        maybeFunc = new ArrayList<>(pair1.findDiffPoint(pair2));
                    } else {
                        Iterator<Function<Point3d, Point3d>> itFunc = maybeFunc.iterator();
                        while (itFunc.hasNext()) {
                            Function<Point3d, Point3d> func = itFunc.next();
                            Point3d p1 = pair1.p1();
                            Point3d p2 = pair1.p2();
                            Point3d p1bis = pair2.p1();
                            Point3d p2bis = pair2.p2();
                            if ((func.apply(p1).equals(p1bis) && func.apply(p2).equals(p2bis))
                                    || (func.apply(p1).equals(p2bis) && func.apply(p2).equals(p1bis))) {
                                assert true;
                            } else {
                                itFunc.remove();
                            }
                        }
                        if (maybeFunc.size() == 1) {
                            return maybeFunc.get(0);
                        } else if (maybeFunc.isEmpty()) {
//                            throw new IllegalStateException("On n'arrive pas à trouver une function qui fonctionne !");
                            maybeFunc = null;
                        } else {
//                            System.out.println("On a encore un doute");
//                            throw new IllegalStateException("On a encore un doute c'est bizarre");
                        }
                    }
                }
            }
        }
        if (maybeFunc != null && maybeFunc.size() > 1) {
//            System.out.println("On ne devrait plus avoir de doute : il reste " + maybeFunc.stream().map(f -> f.apply(new Point3d(0, 0, 0))).toList());
            Set<Point3d> pointsPair2 = pairs2.values().stream()
                    .flatMap(Collection::stream)
                    .flatMap(pair -> Stream.of(pair.p1(), pair.p2()))
                    .collect(Collectors.toSet());
            int maxCount = 0;
            Function<Point3d, Point3d> bestFunc = null;
            for (Function<Point3d, Point3d> func : maybeFunc) {
                long count = pairs1.values().stream()
                        .flatMap(Collection::stream)
                        .flatMap(pair -> Stream.of(pair.p1(), pair.p2()))
                        .map(func)
                        .filter(pointsPair2::contains)
                        .count();
                if (count > maxCount) {
                    bestFunc = func;
                }
            }
//            System.out.println("bestFunc = " + (bestFunc != null ? bestFunc.apply(new Point3d(0, 0, 0)) : null));
            return bestFunc;
        }
        return maybeFunc == null || maybeFunc.isEmpty() ? null : maybeFunc.get(0);
    }


    public Cartographie findTotalBeacons() {
        Map<Integer, Integer> migratedToPov = new HashMap<>();
        Map<Integer, Set<Point3d>> beacons = new HashMap<>();
        scanMap.keySet().forEach(i -> migratedToPov.put(i, i));
        scanMap.forEach((key, value) -> beacons.put(key, new HashSet<>(value)));
        ArrayList<Point3d> scanners = new ArrayList<>();
        Point3d origin = new Point3d(0, 0, 0);
        scanners.add(origin);

        while(beacons.size() > 1) {
            List<Integer> listMigratedToZero = migratedToPov.entrySet().stream()
                    .filter(e -> e.getValue() == 0)
                    .map(Map.Entry::getKey)
                    .toList();
            for (int scanId : listMigratedToZero) {
                for (int scanId2 : scanMap.keySet()) {
                    if (scanId != scanId2 && !migratedToPov.get(scanId2).equals(migratedToPov.get(scanId))) {
                        Function<Point3d, Point3d> func = findFunctionTransformBeacon(getBeacons(scanId2), getBeacons(scanId));
                        if (func != null) {
                            List<Point3d> beaconFromFirstPerspective = scanMap.get(scanId2).stream().map(func).toList();
                            long commonBeaconCount = beaconFromFirstPerspective.stream().filter(o -> getBeacons(scanId).contains(o)).count();
                            if (commonBeaconCount >= 12) {
                                System.out.println("scanId + scanId2 = " + scanId + " + " +scanId2);
                                Integer newGrappe = migratedToPov.get(scanId);
                                Integer previousGrappe = migratedToPov.get(scanId2);
                                if (newGrappe == 0) {
                                    scanners.add(func.apply(origin));
                                }

                                scanMap.put(scanId2, beaconFromFirstPerspective);

                                rassembler2Grappes(migratedToPov, beacons, func, newGrappe, previousGrappe);
                            }
                        }
                    }
                }
            }
            System.out.println("Un tour complet a été fait beacons = " + beacons.size() + " grappe 0 = "+ beacons.get(0).size());
        }

        return new Cartographie(beacons.values().stream().flatMap(Collection::stream).toList(), scanners);
    }

    private void rassembler2Grappes(Map<Integer, Integer> migratedToPov,
                                    Map<Integer, Set<Point3d>> beacons,
                                    Function<Point3d, Point3d> func,
                                    Integer newGrappe,
                                    Integer previousGrappe) {
        beacons.get(previousGrappe).stream()
                .map(func)
                .forEach(b -> beacons.get(newGrappe).add(b));
        beacons.remove(previousGrappe);
        migratedToPov.entrySet()
                .stream()
                .filter(val -> val.getValue().equals(previousGrappe))
                .map(Map.Entry::getKey)
                .toList()
                .forEach(val -> migratedToPov.put(val, newGrappe));
    }

    public static Map<Double, List<PointPair>> findAllPairs(Collection<Point3d> point3ds) {
        Set<Point3d> alreadyVisited = new HashSet<>();
        return point3ds.stream()
                .peek(alreadyVisited::add)
                .flatMap(p1 -> point3ds.stream()
                        .filter(p2 -> !alreadyVisited.contains(p2))
                        .filter(p2 -> !p2.equals(p1))
                        .map(p2 -> new PointPair(p1, p2))
                )
                .collect(Collectors.groupingBy((PointPair p) -> p.p1().dist2(p.p2())));
    }

    public List<Point3d> getBeacons(int scannerId) {
        return scanMap.get(scannerId);
    }

    public record Cartographie(List<Point3d> beacons, List<Point3d> scanners) {
    }
}
