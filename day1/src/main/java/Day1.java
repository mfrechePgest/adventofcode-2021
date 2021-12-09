import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day1 {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Day1.class.getResourceAsStream("first_day.txt")))) {
            String line = br.readLine();
            int result = 0;
            int currentIdx = 0;
            List<List<Integer>> lstWindows = new ArrayList<>();
            while (line != null) {
                int depth = Integer.parseInt(line);
                peupleWindows(lstWindows, currentIdx, depth);
                if ( lstWindows.get(currentIdx).size() == 3 ) {
                    currentIdx++;
                }
                line = br.readLine();
            }
            Integer previousDepth = null;
            for ( List<Integer> window : lstWindows ) {
                assert window.size() == 3;
                int depth = window.stream().mapToInt(i -> i).sum();
                if (previousDepth != null && depth > previousDepth) {
                    result++;
                }
                previousDepth = depth;
            }
            System.out.println("result = " + ConsoleColors.cyan(result));
        }
    }

    private static void peupleWindows(List<List<Integer>> lstWindows, int currentIdx, int value) {
        if (lstWindows.size() <= currentIdx) {
            lstWindows.add(new ArrayList<>());
        }
        List<Integer> currentWindow = lstWindows.get(currentIdx);
        currentWindow.add(value);
        if ( currentWindow.size() != 1 ) {
            peupleWindows(lstWindows, currentIdx+1, value);
        }
    }
}
