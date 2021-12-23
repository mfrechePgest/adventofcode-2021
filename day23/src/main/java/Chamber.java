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
            return this.getLast().energyCost(countSteps(parkingsATraverser, this.size(), this.fullCapacity));
        }
        return -1;
    }

    public int canMoveToOtherChamber(int chamber, Situation situation) {
        if (situation.lstChambers().get(chamber).size() < fullCapacity) {
            List<Integer> parkingsATraverser = hasToMoveThroughToOtherChamber(chamber);
            if (parkingsATraverser.stream().map(i -> situation.parkings().get(i)).allMatch(Objects::isNull)) {
                return this.getLast().energyCost(countStepsToOtherChamber(chamber, situation, parkingsATraverser));
            }
        }
        return -1;
    }

    private int countStepsToOtherChamber(int chamber, Situation situation, List<Integer> parkingsATraverser) {
        return countSteps(parkingsATraverser, this.size(), this.fullCapacity) + (fullCapacity + 1 - situation.lstChambers().get(chamber).size());
    }

    /**
     * Sans prendre en considération les obstacles
     */
    private int countStepsToOtherChamber(int indexInChamber, int chamber) {
        List<Integer> parkingsATraverser = hasToMoveThroughToOtherChamber(chamber);
        return countSteps(parkingsATraverser, indexInChamber, this.fullCapacity) + 1;
    }

    public static int countSteps(List<Integer> parkingsATraverser, int idxInChamber, int chamberFullCapacity) {
        int steps = chamberFullCapacity + 1 - idxInChamber; // get out of chamber
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
        return hasToMoveThroughToParking(idx, parking);
    }

    public static List<Integer> hasToMoveThroughToParking(int chamber, int parking) {
        if (parking >= chamber + 2) {
            // vers la droite
            return IntStream.range(chamber + 2, parking + 1).boxed().toList();
        } else {
            // vers la gauche
            return IntStream.range(parking, chamber + 2).boxed().sorted(Comparator.reverseOrder()).toList();
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
        return this.size() == getFullCapacity() && this.stream().allMatch(this::isRequiredPod);
    }

    public boolean isRequiredPod(Amphipod pod) {
        return pod.getTargetChamber() == this.idx;
    }

    public Chamber clone() {
        Chamber copy = new Chamber(this.idx, this.fullCapacity);
        copy.addAll(this);
        return copy;
    }

    public Stream<Situation> findAllPossibleMoves(Situation situation) {
        return
                IntStream.range(0, situation.parkings().size())
                        .filter(p -> situation.parkings().get(p) == null)
                        .mapToObj(p -> situation.moveChamberToParking(this, p))
                        .filter(Objects::nonNull);
    }

    public int countCorrectlyPlacedPods() {
        int result = 0;
        for (int i = 0; i < size(); i++) {
            if (isRequiredPod(this.get(i))) {
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

    public int totalStepsToDestination() {
        int totalSteps = 0;
        for (int i = 0; i < size(); i++) {
            if (!isRequiredPod(this.get(i))) {
                totalSteps += countStepsToOtherChamber(i, this.get(i).getTargetChamber());
            }
        }
        return totalSteps;
    }
}
