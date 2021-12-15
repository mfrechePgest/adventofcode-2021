import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day15 extends AbstractDay {

    private Cell[][] cells;
    private int currentLineReadProgress = 0;

    public static void main(String[] args) throws IOException {
        Day15 day15 = new Day15("sample.txt");

        System.out.println(Arrays.stream(day15.cells)
                .map(ligne -> Arrays.stream(ligne).map(c -> String.valueOf(c.value())).collect(Collectors.joining("")))
                .collect(Collectors.joining("\n")));
        Path bestPath = day15.getBestPath();
        System.out.println("result = " + bestPath.totalCost());

        day15.expandMap();
        System.out.println(Arrays.stream(day15.cells)
                .map(ligne -> Arrays.stream(ligne).map(c -> c != null ? String.valueOf(c.value()) : "N").collect(Collectors.joining("")))
                .collect(Collectors.joining("\n")));
        Path bestPathStep2 = day15.getBestPath();
        System.out.println("result = " + bestPathStep2.totalCost());
    }


    public Day15(String fileName) throws IOException {
        this.openFile(fileName);
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }

    @Override
    public void readLine() throws IOException {
        if (currentLineReadProgress == 0) {
            cells = new Cell[currentLine.length()][currentLine.length()];
        }
        for (int i = 0; i < currentLine.length(); i++) {
            cells[currentLineReadProgress][i] = new Cell(currentLineReadProgress, i, Character.getNumericValue(currentLine.charAt(i)));
        }
        currentLine = br.readLine();
        currentLineReadProgress++;
    }

    public Path getBestPath() {
        Cell origin = cells[0][0];
        Cell destination = cells[cells.length - 1][cells.length - 1];
        return origin.getBestPathTo(destination, cells);
    }

    public void expandMap() {
        Cell[][] newMap = new Cell[cells.length * 5][cells.length * 5];
        for (int multiplyX = 1; multiplyX <= 5; multiplyX++) {
            for (int multiplyY = 1; multiplyY <= 5; multiplyY++) {
                for (int x = 0; x < cells.length; x++) {
                    for (int y = 0; y < cells.length; y++) {
                        int value = cells[x][y].value() + (multiplyX - 1) + (multiplyY - 1);
                        while (value > 9) {
                            value -= 9;
                        }
                        newMap[x * multiplyX][y * multiplyY] =
                                new Cell(x * multiplyX, y * multiplyY,
                                        value);
                    }
                }
            }
        }
        cells = newMap;
    }
}
