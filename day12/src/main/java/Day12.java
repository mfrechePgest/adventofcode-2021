import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 extends AbstractDay {

    public final static String STARTING_CELL = "start";
    public final static String ENDING_CELL = "end";

    private Map<String, Cell> cells = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Day12 day12 = new Day12("day12_1.txt");



        List<Path> paths = day12.findPathFromStartToEnd(Step.STEP_1);
        System.out.println("STEP 1 : Paths = \n" + paths.stream().map(Path::toString).collect(Collectors.joining("\n")));
        System.out.println("STEP 1 : Path count : " + ConsoleColors.cyan(paths.size()));

        System.out.println("""
                    
                ==================================
                            
                """);

        paths = day12.findPathFromStartToEnd(Step.STEP_2);
        System.out.println("STEP 2 : Paths = \n" + paths.stream().map(Path::toString).collect(Collectors.joining("\n")));
        System.out.println("STEP 2 : Path count : " + ConsoleColors.cyan(paths.size()));

    }

    public Day12(String fileName) throws IOException {
        this.openFile(fileName);
        while (this.hasMoreLines()) {
            this.readLine();
        }
    }

    public List<Path> findPathFromStartToEnd(Step stepMode) {
        return cells.get(STARTING_CELL).getAllPathTo(cells.get(ENDING_CELL), stepMode);
    }


    @Override
    public void readLine() throws IOException {
        String[] connections = currentLine.split("-");
        Cell origin = cells.computeIfAbsent(connections[0], name -> new Cell(name, name.toLowerCase().equals(name), new ArrayList<>()));
        Cell destination = cells.computeIfAbsent(connections[1], name -> new Cell(name, name.toLowerCase().equals(name), new ArrayList<>()));
        origin.connectedCells.add(destination);
        destination.connectedCells.add(origin);
        currentLine = br.readLine();
    }

    public record Cell(String name, boolean small, List<Cell> connectedCells) {
        public List<Path> getAllPathTo(Cell destination, Step stepMode) {
            return connectedCells.stream()
                    .map(nextStep -> new Path(this, destination,
                            new LinkedList<>(Arrays.asList(this, nextStep)),
                            stepMode == Step.STEP_1))
                    .flatMap(Path::getNextStep)
                    .toList();
        }
    }

    public record Path(Cell origin, Cell destination, LinkedList<Cell> visited, boolean secondPassUsed) {
        public String toString() {
            String result = visited.stream().map(cell -> cell.name).collect(Collectors.joining(","));
            return ConsoleColors.coloredString(result, secondPassUsed ? ConsoleColors.GREEN : ConsoleColors.RED);
        }

        public Stream<Path> getNextStep() {
            Cell last = visited.getLast();
            if (last.name.equals(ENDING_CELL)) {
                return Stream.of(this);
            } else {
                return last.connectedCells().stream()
                        .filter(nextStep -> !nextStep.name.equals(STARTING_CELL) &&
                                (!secondPassUsed
                                        || !nextStep.small
                                        || !visited.contains(nextStep)))
                        .map(nextStep -> {
                            Path newPath = new Path(origin,
                                    destination,
                                    new LinkedList<>(this.visited),
                                    secondPassUsed || (visited.contains(nextStep) && nextStep.small)
                            );
                            newPath.visited.add(nextStep);
                            return newPath;
                        })
                        .flatMap(Path::getNextStep);
            }
        }
    }

    public enum Step {
        STEP_1, STEP_2
    }

}
