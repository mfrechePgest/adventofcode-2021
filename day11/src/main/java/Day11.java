import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day11 {


    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Day11.class.getResourceAsStream("day_11_1.txt")))) {

            int[][] octopusses = null;

            String line = br.readLine();

            int lineNb = 0;
            while (line != null) {
                if (octopusses == null) {
                    octopusses = new int[line.length()][line.length()];
                }
                octopusses[lineNb] = line.chars().mapToObj(c -> (char) c).mapToInt(Character::getNumericValue).toArray();
                lineNb++;
                line = br.readLine();
            }
            assert octopusses != null;
            System.out.println("octopusses = \n" + arrayToString(octopusses));

            int result = 0;
            Integer stepWhenAllFlashes = null;
            int i = 0;
            while (stepWhenAllFlashes == null) {
                increaseValues(octopusses);
                List<Cell> enlightened = new ArrayList<>();
                enlighten(octopusses, enlightened);
                if (i < 100) {
                    result += enlightened.size();
                }
                if (enlightened.size() == octopusses.length * octopusses.length) {
                    // Every octopus has flashed this round
                    System.out.println("After " + (i + 1) + " step = \n" + arrayToString(octopusses, enlightened));
                    stepWhenAllFlashes = i;
                }
                if (i < 10 || (i + 1) % 10 == 0)
                    System.out.println("After " + (i + 1) + " step = \n" + arrayToString(octopusses, enlightened));
                i++;
            }

            System.out.println("Blink count for 100 steps = " + ConsoleColors.cyan(result));
            System.out.println("Step when every octopus flashes = " + ConsoleColors.cyan(stepWhenAllFlashes + 1));

        }
    }

    private static void enlighten(int[][] octopusses, List<Cell> enlightened) {
        List<Cell> shouldFlashAgain = new ArrayList<>();
        for (int x = 0; x < octopusses.length; x++) {
            for (int y = 0; y < octopusses.length; y++) {
                if (octopusses[x][y] > 9) {
                    shouldFlashAgain.addAll(flash(octopusses, new Cell(x, y), enlightened));
                }
            }
        }
        if (!shouldFlashAgain.isEmpty()) {
            enlighten(octopusses, enlightened);
        }
    }

    private static List<Cell> flash(int[][] octopusses, Cell cell, List<Cell> enlightened) {
        octopusses[cell.x][cell.y] = 0;
        enlightened.add(cell);
        return cell.neighbours(octopusses)
                .filter(c -> !enlightened.contains(c))
                .peek(c -> octopusses[c.x][c.y]++)
                .filter(c -> octopusses[c.x][c.y] > 9)
                .toList();
    }

    private static void increaseValues(int[][] octopusses) {
        for (int x = 0; x < octopusses.length; x++) {
            for (int y = 0; y < octopusses.length; y++) {
                octopusses[x][y]++;
            }
        }
    }

    public static String arrayToString(int[][] a) {
        return Arrays.stream(a)
                .map(s -> Arrays.stream(s)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" "))
                )
                .collect(Collectors.joining("\n"));
    }

    public static String arrayToString(int[][] a, List<Cell> enlightened) {
        return IntStream.range(0, a.length)
                .mapToObj(x -> IntStream.range(0, a[x].length)
                        .mapToObj(y -> new Cell(x, y))
                        .map(cell -> enlightened.contains(cell) ?
                                ConsoleColors.coloredString(a[cell.x][cell.y], ConsoleColors.YELLOW) :
                                String.valueOf(a[cell.x][cell.y])
                        )
                        .collect(Collectors.joining(" "))
                )
                .collect(Collectors.joining("\n"));
    }

    public record Cell(int x, int y) {
        public Stream<Cell> neighbours(int[][] octopusses) {
            return IntStream.range(x - 1, x + 2)
                    .filter(i -> i >= 0)
                    .filter(i -> i < octopusses.length)
                    .boxed()
                    .flatMap(i -> IntStream.range(y - 1, y + 2)
                                    .filter(j -> j >= 0)
                                    .filter(j -> j < octopusses.length)
//                            .peek(j -> System.err.println("Voisin de "+this+ " = ["+(i)+","+(j)+"]"))
                                    .mapToObj(j -> new Cell(i, j))
                    );
        }
    }

}
