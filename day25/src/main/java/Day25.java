import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day25 extends AbstractDay {

    private List<List<Cucumber>> cucumbersMap = new ArrayList<>();
    private int lineIndex = 0;

    public static void main(String[] args) throws IOException {
        Day25 day25 = new Day25("input.txt");

        System.out.println("day25.moveTillBlocked() = " + ConsoleColors.cyan(day25.moveTillBlocked()));
    }

    public Day25(String fileName) throws IOException {
        this.openFile(fileName);
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }

    public String toString() {
        return cucumbersMap
                .stream()
                .map(l -> l.stream()
                        .map(cucumber -> cucumber == null ? '.' : cucumber.toChar())
                        .map(c -> "" + c)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));

    }

    @Override
    public void readLine() throws IOException {
        for (int i = 0; i < currentLine.length(); i++) {
            if (cucumbersMap.size() <= lineIndex ) {
                ArrayList<Cucumber> line = new ArrayList<>(currentLine.length());
                IntStream.range(0, currentLine.length()).forEach(u -> line.add(null));
                cucumbersMap.add(line);
            }
            cucumbersMap.get(lineIndex).set(i, Cucumber.fromChar(currentLine.charAt(i)));
        }
        lineIndex++;
        currentLine = br.readLine();
    }

    public int turn() {
        int moveEast = moveAll(Cucumber.EAST, new HashSet<>());
        int moveSouth = moveAll(Cucumber.SOUTH, new HashSet<>());
        return moveEast + moveSouth;
    }

    public int moveTillBlocked() {
        int turn = 1;
        while(turn() > 0) {
            turn++;
        }
        return turn;
    }

    private int moveAll(Cucumber cucumberType, Set<Coord> alreadyMoved) {
        int count = 0;
        for (int i = 0; i < cucumbersMap.size(); i++) {
            for (int j = 0; j < cucumbersMap.get(i).size(); j++) {
                Cucumber cucumber = cucumbersMap.get(i).get(j);
                if (cucumber == cucumberType) {
                    Coord origin = new Coord(j, i);
                    Coord destination = cucumber.findDestination(origin, cucumbersMap);

                    if (cucumbersMap.get(destination.y()).get(destination.x()) == null && !alreadyMoved.contains(origin) && !alreadyMoved.contains(destination)) {
                        cucumbersMap.get(destination.y()).set(destination.x(), cucumbersMap.get(i).get(j));
                        cucumbersMap.get(i).set(j, null);
                        alreadyMoved.add(destination);
                        alreadyMoved.add(origin);
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
