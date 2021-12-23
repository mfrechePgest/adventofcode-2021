import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Situation {
    private final List<Chamber> lstChambers;
    private final Parkings parkings;
    private final long cost;
    private int correctlyPlacedPods;
    private Situation parent;
    private double heuristic;

    public Situation(List<Chamber> lstChambers, Parkings parkings, long cost, Situation parent) {
        this.lstChambers = lstChambers;
        this.parkings = parkings;
        this.cost = cost;
        this.correctlyPlacedPods = countCorrectlyPlacedPods();
        this.parent = parent;
    }

    public boolean isFinished() {
        return lstChambers
                .stream()
                .allMatch(Chamber::isFinished);
    }

    public List<Situation> findAllPossibleMoves() {
        List<Situation> moves = Stream.concat(
                        lstChambers.stream()
                                .filter(chamber -> !chamber.isFinished())
                                .filter(chamber -> chamber.countCorrectlyPlacedPods() != chamber.size())
                                .filter(chamber -> !chamber.isEmpty()) // chamber which can move
                                .flatMap(chamber -> chamber.findAllPossibleMoves(this)),
                        IntStream.range(0, parkings.size())
                                .filter(i -> parkings.get(i) != null)
                                .boxed()
                                .flatMap(i -> parkings.findAllPossibleMoves(i, this))
                ).filter(Objects::nonNull)
                .toList();
        if ( this.getCorrectlyPlacedPods() > 12 ) {
            System.out.println(this);
            System.out.println(moves.stream().map(m -> "==> " +m).collect(Collectors.joining("\n")));
        }
        return moves;
    }

    public Situation clone(int moveCost) {
        return new Situation(
                lstChambers.stream().map(Chamber::clone).toList(),
                parkings.clone(),
                cost + moveCost,
                this
        );
    }


    public Situation moveChamberToParking(Chamber from, int parkingId) {
        int moveCost = from.canMoveToParking(parkingId, this.parkings());
        if (moveCost > 0) {
            Situation result = this.clone(moveCost);
            result.parkings.set(parkingId, result.lstChambers.get(from.getIdx()).pollLast());
            result.correctlyPlacedPods = result.countCorrectlyPlacedPods();
            result.computeHeuristic();
            return result;
        }
        return null;
    }

    public Situation moveParkingToChamber(int parkingId, Chamber to) {
        int moveCost = parkings().canMoveToChamber(parkingId, to);
        if (moveCost > 0) {
            Situation result = this.clone(moveCost);
            result.lstChambers.get(to.getIdx()).add(result.parkings().get(parkingId));
            result.parkings.set(parkingId, null);
            result.correctlyPlacedPods = result.countCorrectlyPlacedPods();
            result.computeHeuristic();
            return result;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if (!(o instanceof Situation)) {
            return false;
        }
        return this.lstChambers.equals(((Situation) o).lstChambers)
                && this.parkings.equals(((Situation) o).parkings);
    }

    public int hashCode() {
        return Objects.hash(lstChambers, parkings);
    }

    private int countCorrectlyPlacedPods() {
        return this.lstChambers.stream()
                .mapToInt(Chamber::countCorrectlyPlacedPods)
                .sum();
    }

    public int getCorrectlyPlacedPods() {
        return correctlyPlacedPods;
    }

    public List<Chamber> lstChambers() {
        return lstChambers;
    }

    public Parkings parkings() {
        return parkings;
    }

    public long cost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Situation[" +
                "lstChambers=" + lstChambers + ", " +
                "parkings=" + parkings + ", " +
                "cost=" + cost + ", " +
                "correctlyplaced=" +correctlyPlacedPods + ", " +
                "heuristic=" + heuristic + ']';
    }


    public double getHeuristic() {
        return heuristic;
    }

    private void computeHeuristic() {
        this.heuristic =
                lstChambers.stream()
                        .mapToInt(Chamber::totalStepsToDestination)
                        .sum() +
                parkings.totalStepsToDestination() + (cost / 10d);
    }
}
