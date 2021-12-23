import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Chamber extends LinkedList<Amphipod> {

    private final int idx;
    private final int fullCapacity;

    public Chamber(int idx, int fullCapacity) {
        this.idx = idx;
        this.fullCapacity = fullCapacity;
    }

    public int getIdx() {
        return idx;
    }

    public int canMoveToParking(int parking, Parkings parkings) {
        List<Integer> parkingsATraverser = hasToMoveThroughToParking(parking);
        if (parkingsATraverser.stream().map(parkings::get).allMatch(Objects::isNull)) {
            return this.getLast().energyCost(this.countSteps(parkingsATraverser));
        }
        return -1;
    }

    public int canMoveToOtherChamber(int chamber, Situation situation) {
        if (situation.lstChambers().get(chamber).size() < fullCapacity) {
            List<Integer> parkingsATraverser = hasToMoveThroughToOtherChamber(chamber);
            if (parkingsATraverser.stream().map(i -> situation.parkings().get(i)).allMatch(Objects::isNull)) {
                return this.getLast().energyCost(this.countSteps(parkingsATraverser) + ( fullCapacity + 1 - situation.lstChambers().get(chamber).size() ));
            }
        }
        return -1;
    }

    public int countSteps(List<Integer> parkingsATraverser) {
        int steps = fullCapacity + 1 - this.size(); // get out of chamber
        Integer previousParking = null;
        for (Integer parkingATraverser : parkingsATraverser) {
            if (previousParking != null) {
                steps += Parkings.countStepsFromParkingToParking(previousParking, parkingATraverser);
            } else {
                steps++;
            }
            previousParking = parkingATraverser;
        }
        return steps;
    }

    public List<Integer> hasToMoveThroughToParking(int parking) {
        if (parking >= idx + 2) {
            // vers la droite
            return IntStream.range(idx + 2, parking + 1).boxed().toList();
        } else {
            // vers la gauche
            return IntStream.range(parking, idx + 2).boxed().sorted(Comparator.reverseOrder()).toList();
        }
    }

    public List<Integer> hasToMoveThroughToOtherChamber(int chamber) {
        if (chamber > idx) {
            // vers la droite
            return IntStream.range(idx + 2, chamber + 2).boxed().toList();
        } else {
            // vers la gauche
            return IntStream.range(chamber + 2, idx + 2).boxed().sorted(Comparator.reverseOrder()).toList();
        }
    }

    public boolean isFinished() {
        return this.size() == 2 && this.stream().allMatch(this::isRequiredPod);
    }

    public boolean isRequiredPod(Amphipod pod) {
        return pod ==
                switch (idx) {
                    case 0 -> Amphipod.A;
                    case 1 -> Amphipod.B;
                    case 2 -> Amphipod.C;
                    case 3 -> Amphipod.D;
                    default -> throw new IllegalStateException();
                };
    }

    public Chamber clone() {
        Chamber copy = new Chamber(this.idx, this.fullCapacity);
        copy.addAll(this);
        return copy;
    }

    public Stream<Situation> findAllPossibleMoves(Situation situation) {
        return
                Stream.concat(
                                situation.lstChambers()
                                        .stream()
                                        .filter(c -> c != this)
                                        .filter(c -> c.size() < fullCapacity)
                                        .filter(c -> c.isRequiredPod(this.getLast()))
                                        .map(c -> situation.moveChamberToChamber(this, c))
                                ,
                                IntStream.range(0, situation.parkings().size())
                                        .filter(p -> situation.parkings().get(p) == null)
                                        .mapToObj(p -> situation.moveChamberToParking(this, p))
                        )
                        .filter(Objects::nonNull);
    }

    public int countCorrectlyPlacedPods() {
        int result = 0;
        for (int i = 0 ; i < size() ; i++ ) {
            if ( isRequiredPod(this.get(i)) ) {
                result++;
            } else {
                break;
            }
        }
        return result;
    }

    public int getFullCapacity() {
        return fullCapacity;
    }
}
