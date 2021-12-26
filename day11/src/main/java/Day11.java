import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day11 extends AbstractDay {

    private int currentLineNb = 0;
    private int[][] octopusses = null;


    public static void main(String[] args) throws IOException {
        Day11 day11 = new Day11();
        day11.openFile("day_11_1.txt");

        while (day11.hasMoreLines()) {
            day11.readLine();
        }

        System.out.println("octopusses = \n" + arrayToString(day11.getSituation()));

        day11.compute(Day11::printToConsole, false);

        day11.closeFile();
    }

    private static void printToConsole(Situation situation, Boolean duringPropagation) {
        if (!duringPropagation && situation.enlightened.size() == situation.octopusses.length * situation.octopusses.length
                || situation.step % 10 == 0
                || situation.step <= 10) {
            System.out.println("After " + situation.step + " step = \n" + arrayToString(situation));
        }
    }

    public void compute(BiConsumer<Situation, Boolean> eachTurnConsumer, boolean evenDuringPropagation) {

        int result = 0;
        Integer stepWhenAllFlashes = null;
        int i = 0;
        while (stepWhenAllFlashes == null) {
            increaseValues(i, evenDuringPropagation ? eachTurnConsumer : (situ, b) -> {});
            List<Cell> enlightened = new ArrayList<>();
            enlighten(i, enlightened, evenDuringPropagation ? eachTurnConsumer : (situ, b) -> {});
            if (i < 100) {
                result += enlightened.size();
            }
            if (enlightened.size() == octopusses.length * octopusses.length) {
                // Every octopus has flashed this round
                stepWhenAllFlashes = i;
            }
            guiCallback(eachTurnConsumer, i, enlightened, false);
            i++;
        }

        System.out.println("Blink count for 100 steps = " + ConsoleColors.cyan(result));
        System.out.println("Step when every octopus flashes = " + ConsoleColors.cyan(stepWhenAllFlashes + 1));
    }

    private void guiCallback(BiConsumer<Situation, Boolean> eachTurnConsumer, int i, List<Cell> enlightened, boolean duringPropagation) {
        int[][] copyOctopusses = Arrays.stream(octopusses).map(int[]::clone).toArray(int[][]::new);
        eachTurnConsumer.accept(new Situation(i + 1, copyOctopusses, new ArrayList<>(enlightened)), duringPropagation);
    }

    private void enlighten(int step, List<Cell> enlightened, BiConsumer<Situation, Boolean> eachTurnConsumer) {
        List<Cell> shouldFlashAgain = new ArrayList<>();
        for (int x = 0; x < octopusses.length; x++) {
            for (int y = 0; y < octopusses.length; y++) {
                if (octopusses[x][y] > 9) {
                    shouldFlashAgain.addAll(flash(octopusses, new Cell(x, y), enlightened));
                }
            }
        }
        guiCallback(eachTurnConsumer, step, enlightened, true);
        if (!shouldFlashAgain.isEmpty()) {
            enlighten(step, enlightened, eachTurnConsumer);
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

    private void increaseValues(int step, BiConsumer<Situation, Boolean> eachTurnConsumer) {
        for (int x = 0; x < octopusses.length; x++) {
            for (int y = 0; y < octopusses.length; y++) {
                octopusses[x][y]++;
            }
        }
        guiCallback(eachTurnConsumer, step, new ArrayList<>(), true);
    }


    public static String arrayToString(Situation situation) {
        return IntStream.range(0, situation.octopusses.length)
                .mapToObj(x -> IntStream.range(0, situation.octopusses[x].length)
                        .mapToObj(y -> new Cell(x, y))
                        .map(cell -> situation.enlightened.contains(cell) ?
                                ConsoleColors.coloredString(situation.octopusses[cell.x][cell.y], ConsoleColors.YELLOW) :
                                String.valueOf(situation.octopusses[cell.x][cell.y])
                        )
                        .collect(Collectors.joining(" "))
                )
                .collect(Collectors.joining("\n"));
    }



    @Override
    public void readLine() throws IOException {
        if (octopusses == null) {
            octopusses = new int[currentLine.length()][currentLine.length()];
        }
        octopusses[currentLineNb] = currentLine.chars().mapToObj(c -> (char) c).mapToInt(Character::getNumericValue).toArray();
        currentLineNb++;

        currentLine = br.readLine();
    }

    public Situation getSituation() {
        return new Situation(0, octopusses, new ArrayList<>());
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

    public record Situation(int step, int[][] octopusses, List<Cell> enlightened) {
    }

}
