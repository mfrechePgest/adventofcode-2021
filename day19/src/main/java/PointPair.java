import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record PointPair(Point3d p1, Point3d p2) {

    public enum Axe {
        X {
            @Override
            Function<Point3d, Integer> tocoordFunction() {
                return Point3d::x;
            }
        },
        Y {
            @Override
            Function<Point3d, Integer> tocoordFunction() {
                return Point3d::y;
            }
        },
        Z {
            @Override
            Function<Point3d, Integer> tocoordFunction() {
                return Point3d::z;
            }
        };

        abstract Function<Point3d, Integer> tocoordFunction();
    }

    public List<Function<Point3d, Point3d>> findDiffPoint(PointPair pointPair) {

        return Stream.concat(
                        getStreamFunction(this.p1(), pointPair.p2(), this.p2(), pointPair.p1()),
                        getStreamFunction(this.p1(), pointPair.p1(), this.p2(), pointPair.p2())
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Stream<Function<Point3d, Point3d>> getStreamFunction(Point3d p1, Point3d p1bis, Point3d p2, Point3d p2bis) {
        return Stream.of(
                find3dPointDiff(p1, p1bis, p2, p2bis),
                find3dPointDiff(p1, p1bis, p2, p2bis, Axe.Y, Axe.X, Axe.Z),
                find3dPointDiff(p1, p1bis, p2, p2bis, Axe.X, Axe.Z, Axe.Y),
                find3dPointDiff(p1, p1bis, p2, p2bis, Axe.Z, Axe.Y, Axe.X),
                find3dPointDiff(p1, p1bis, p2, p2bis, Axe.Y, Axe.Z, Axe.X),
                find3dPointDiff(p1, p1bis, p2, p2bis, Axe.Z, Axe.X, Axe.Y)
        );
    }

    private Function<Point3d, Point3d> find3dPointDiff(Point3d p1, Point3d p2, Point3d p1bis, Point3d p2bis) {
        return find3dPointDiff(p1, p2, p1bis, p2bis, Axe.X, Axe.Y, Axe.Z);
    }

    private Function<Point3d, Point3d> find3dPointDiff(Point3d p1, Point3d p2, Point3d p1bis, Point3d p2bis,
                                                       Axe compareXWith,
                                                       Axe compareYWith,
                                                       Axe compareZWith) {
        ConvertFunction diffX = findDiff(p1, p2, p1bis, p2bis, Point3d::x, compareXWith.tocoordFunction());
        ConvertFunction diffY = findDiff(p1, p2, p1bis, p2bis, Point3d::y, compareYWith.tocoordFunction());
        ConvertFunction diffZ = findDiff(p1, p2, p1bis, p2bis, Point3d::z, compareZWith.tocoordFunction());
        if (diffX == null || diffY == null || diffZ == null) {
            return null;
        }

        ConvertFunction diffX2;
        Function<Point3d, Integer> whichAxeGoesToX;
        if (compareYWith == Axe.X) {
            diffX2 = diffY;
            whichAxeGoesToX = Point3d::y;
        } else if (compareZWith == Axe.X) {
            diffX2 = diffZ;
            whichAxeGoesToX = Point3d::z;
        } else {
            diffX2 = diffX;
            whichAxeGoesToX = Point3d::x;
        }

        ConvertFunction diffY2;
        Function<Point3d, Integer> whichAxeGoesToY;
        if (compareXWith == Axe.Y) {
            diffY2 = diffX;
            whichAxeGoesToY = Point3d::x;
        } else if (compareZWith == Axe.Y) {
            diffY2 = diffZ;
            whichAxeGoesToY = Point3d::z;
        } else {
            diffY2 = diffY;
            whichAxeGoesToY = Point3d::y;
        }

        ConvertFunction diffZ2;
        Function<Point3d, Integer> whichAxeGoesToZ;
        if (compareXWith == Axe.Z) {
            diffZ2 = diffX;
            whichAxeGoesToZ = Point3d::x;
        } else if (compareYWith == Axe.Z) {
            diffZ2 = diffY;
            whichAxeGoesToZ = Point3d::y;
        } else {
            diffZ2 = diffZ;
            whichAxeGoesToZ = Point3d::z;
        }


        Function<Point3d, Point3d> result = p -> new Point3d(
                diffX2.apply(whichAxeGoesToX.apply(p)),
                diffY2.apply(whichAxeGoesToY.apply(p)),
                diffZ2.apply(whichAxeGoesToZ.apply(p))
        );

        if (result.apply(p1).equals(p2) && result.apply(p1bis).equals(p2bis)) {
            return result;
        } else {
            throw new IllegalStateException("On a rien à faire ici");
//            return null;
        }
    }


    public ConvertFunction findDiff(Point3d p1,
                                    Point3d p2, Point3d p1bis, Point3d p2bis,
                                    Function<Point3d, Integer> coordFunction,
                                    Function<Point3d, Integer> secondCoordFunction) {
        return findDiff(coordFunction.apply(p1),
                secondCoordFunction.apply(p2),
                coordFunction.apply(p1bis),
                secondCoordFunction.apply(p2bis)
        );
    }

    public ConvertFunction findDiff(int p1x, int newp1x, int p2x, int newp2x) {
        if (p1x + newp1x == p2x + newp2x) {
            int ref1 = p1x + newp1x;
            assert ref1 - p1x == newp1x && ref1 - p2x == newp2x;
            return new ConvertFunction(ref1, (ref, x) -> ref - x);
        } else if (p1x - newp1x == p2x - newp2x) {
            int ref1 = p1x - newp1x;
            assert p1x - ref1 == newp1x && p2x - ref1 == newp2x;
            return new ConvertFunction(ref1, (ref, x) -> x - ref);
        }
        return null;
    }

    public class ConvertFunction implements Function<Integer, Integer> {
        private final int ref;
        private final BiFunction<Integer, Integer, Integer> operation;

        public ConvertFunction(int ref, BiFunction<Integer, Integer, Integer> operation) {
            this.ref = ref;
            this.operation = operation;
        }


        @Override
        public Integer apply(Integer integer) {
            return this.operation.apply(ref, integer);
        }
    }


}
