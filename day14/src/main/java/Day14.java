import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day14 extends AbstractDay {



    private final Map<String, Character> pairInsertionRulesString = new HashMap<>();
    private final Map<String, Long> pairCount = new HashMap<>();
    private Map<Character, Long> letterCount = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Day14 day14 = new Day14("day14_1.txt");

        for (int i = 0; i < 40; i++) {
            System.out.println("Computing step " + (i + 1) + " ...");

            day14.stepOverStrings();

            if (i == 9) {
                printResult(day14, 1);
            }
        }

        printResult(day14, 2);


    }

    private static void printResult(Day14 day14, int step) {
        System.out.println("Result step " + step + " = " + ConsoleColors.cyan(day14.result()));
    }

    public long result() {
        Map<Character, Long> countEachLetter = countEachLetter();
        long max = countEachLetter.values().stream().mapToLong(Long::longValue)
                .max().orElse(0);
        long min = countEachLetter.values().stream().mapToLong(Long::longValue)
                .min().orElse(0);
        return max - min;
    }

    public Day14(String fileName) throws IOException {
        this.openFile(fileName);
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }

    private Map<Character, Long> countEachLetter() {
        return letterCount;
    }

    public void stepOverStrings() {
        Map<String, Long> copyPairCount = new HashMap<>(pairCount);
        for (Map.Entry<String, Long> entry : copyPairCount.entrySet()) {
            Character toInsert = pairInsertionRulesString.get(entry.getKey());
            if (toInsert != null) {
                String leftPair = "" + entry.getKey().charAt(0) + toInsert;
                String rightPair = "" + toInsert + entry.getKey().charAt(1);
                addPairToCount(leftPair, entry.getValue());
                addPairToCount(rightPair, entry.getValue());
                addPairToCount(entry.getKey(), -entry.getValue());
                long val = 0;
                if (letterCount.containsKey(toInsert)) {
                    val = letterCount.get(toInsert);
                }
                letterCount.put(toInsert, val + entry.getValue());
            }
        }
    }

    @Override
    public void readLine() throws IOException {
        if (pairCount.isEmpty()) {
            // First line is the polymer template
            List<Character> polymerTemplate = new LinkedList<>(currentLine.chars().mapToObj(c -> (char) c).toList());

            Iterator<Character> itPolymer = polymerTemplate.iterator();
            Character previous = itPolymer.next();
            int i = 0;
            while (i < polymerTemplate.size() - 1) {
                Character next = itPolymer.next();
                String pair = "" + previous + next;
                previous = next;
                addPairToCount(pair, 1L);
                i++;
            }

            letterCount = currentLine.chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        } else if (!currentLine.isBlank()) {
            String[] splittedLine = currentLine.split(" -> ");
            pairInsertionRulesString.put(splittedLine[0], splittedLine[1].charAt(0));
        }
        currentLine = br.readLine();
    }

    private void addPairToCount(String pair, Long count) {
        long val = 0;
        if (pairCount.containsKey(pair)) {
            val = pairCount.get(pair);
        }
        pairCount.put(pair, val + count);
    }
}
