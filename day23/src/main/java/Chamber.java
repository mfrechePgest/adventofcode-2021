import java.util.*;
import java.util.stream.IntStream;

public class Chamber extends LinkedList<Amphipod> {

    private final int idx;

    public Chamber(int idx) {
        this.idx = idx;
    }

    public int canMove(int parking, Parkings parkings) {
        List<Integer> parkingsATraverser = hasToMoveThroughToParking(parking);
        if (parkingsATraverser.stream().map(parkings::get).allMatch(Objects::isNull)) {
            return this.getLast().energyCost(this.countSteps(parkingsATraverser));
        }
        return -1;
    }

    private int countSteps(List<Integer> parkingsATraverser) {
        int steps = Math.abs(1 - this.size()); // get out of chamber
        Integer previousParking = null;
        for (Integer parkingATraverser : parkingsATraverser) {
            if ( previousParking != null ) {
                steps += Parkings.countStepsFromParkingToParking(previousParking, parkingATraverser);
            } else {
                steps++;
            }
            previousParking = parkingATraverser;
        }
        return steps;
    }

    public List<Integer> hasToMoveThroughToParking(int parking) {
        if ( parking >= idx + 2 ) {
            // vers la droite
            return IntStream.range(idx + 2, parking + 1).boxed().toList();
        } else {
            // vers la gauche
            return IntStream.range(parking, idx +2).boxed().sorted(Comparator.reverseOrder()).toList();
        }
    }

    public List<Integer> hasToMoveThroughToOtherChamber(int chamber) {
        if ( chamber > idx ) {
            // vers la droite
            return IntStream.range(idx + 2, chamber + 2).boxed().toList();
        } else {
            // vers la gauche
            return IntStream.range(chamber + 2, idx + 2).boxed().sorted(Comparator.reverseOrder()).toList();
        }
    }

}
