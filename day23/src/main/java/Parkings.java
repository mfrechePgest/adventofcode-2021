import java.util.ArrayList;
import java.util.stream.IntStream;

public class Parkings extends ArrayList<Amphipod> {

    public Parkings() {
        super(7);
        IntStream.range(0,7).forEach(i -> this.add(null));
    }

    public static int countStepsFromParkingToParking(int origin, int destination) {
        if (origin == 3 || destination == 3) {
            return 2;
        }
        if ( origin == 1 && destination == 2 || origin == 2 && destination == 1 ) {
            return 2;
        }
        if ( origin == 4 && destination == 5 || origin == 5 && destination == 4 ) {
            return 2;
        }
        return 1;

    }

}
