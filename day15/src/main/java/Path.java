import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;


public final class Path {
    private final Cell step;
    private final int totalCost;
    private final double heuristique;

    public Path(Cell step, int totalCost, Cell destination) {
        this.step = step;
        this.totalCost = totalCost;
        this.heuristique = totalCost + Math.sqrt(sqrDistDestination(destination));
    }


    public double sqr(double i) {
        return i * i;
    }

    public double sqrDistDestination(Cell destination) {
        return sqr(destination.x() - step.x()) + sqr(destination.y() - step.y());
    }

    public Cell step() {
        return step;
    }

    public int totalCost() {
        return totalCost;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Path) obj;
        return Objects.equals(this.step, that.step) &&
                this.totalCost == that.totalCost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(step, totalCost);
    }

    @Override
    public String toString() {
        return "Path[" +
                "step=" + step + ", " +
                "totalCost=" + totalCost + ']';
    }


    public double getHeuristique() {
        return heuristique;
    }
}

