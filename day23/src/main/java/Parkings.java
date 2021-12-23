import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Parkings extends ArrayList<Amphipod> {

    public Parkings() {
        super(7);
        IntStream.range(0, 7).forEach(i -> this.add(null));
    }

    public Parkings(Parkings p) {
        super(p);
    }

    // Pour les parkings voisins
    public static int countStepsFromParkingToParking(int origin, int destination) {
        if (origin == 3 || destination == 3) {
            return 2;
        }
        if (origin == 1 && destination == 2 || origin == 2 && destination == 1) {
            return 2;
        }
        if (origin == 4 && destination == 5 || origin == 5 && destination == 4) {
            return 2;
        }
        return 1;
    }

    public int canMoveToChamber(int parkingId, Chamber to) {
        if (to.isRequiredPod(this.get(parkingId)) && to.size() < 2 && to.countCorrectlyPlacedPods() == to.size()) {
            List<Integer> parkingsATraverser = to.hasToMoveThroughToParking(parkingId);
            if (parkingsATraverser.stream().filter(i -> i != parkingId).map(this::get).allMatch(Objects::isNull)) {
                return this.get(parkingId).energyCost(to.countSteps(parkingsATraverser) - 1);
            }
            return -1;
        }
        return -1;
    }

    public Parkings clone() {
        return new Parkings(this);
    }

    public Stream<Situation> findAllPossibleMoves(int fromParkingIdx, Situation situation) {
        Amphipod movingPod = this.get(fromParkingIdx);
        return situation.lstChambers()
                .stream()
                .filter(c -> c.isRequiredPod(movingPod))
                .filter(c -> c.size() < c.getFullCapacity())
                .filter(c -> c.countCorrectlyPlacedPods() == c.size())
                .map(c -> situation.moveParkingToChamber(fromParkingIdx, c));
    }
}
