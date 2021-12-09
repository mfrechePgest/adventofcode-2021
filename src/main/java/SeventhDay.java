import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class SeventhDay {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                SecondDay.class.getResourceAsStream("seventh_day_1.txt")))) {
            String line = br.readLine();

            List<Integer> crabs = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();

            int totalFuel = IntStream.range(0, crabs.stream().mapToInt(Integer::intValue).max().orElse(0))
                    .map(i -> getTotalFuel(crabs, i))
                    .min()
                    .orElse(0);

            System.out.println("minTotalFuel = " + totalFuel);
        }
    }

    private static int getTotalFuel(List<Integer> crabs, int destination) {
        int totalFuel = 0;
        for (Integer crab : crabs) {
            int distance = Math.abs(crab - destination);
            for ( int i = 0 ; i < distance ; i ++ ) {
                totalFuel += i;
            }
            totalFuel += distance;
        }
        return totalFuel;
    }
    

}
