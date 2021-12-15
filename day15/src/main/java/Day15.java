import java.io.IOException;

public class Day15 extends AbstractDay {

    private Cell[][] cells;
    private int currentLineReadProgress = 0;

    public static void main(String[] args) throws IOException {
        Day15 day15 = new Day15("1.txt");


        long timeStart = System.currentTimeMillis();
        Path bestPath = day15.getBestPath();
        System.out.println("result 1 = " + bestPath.totalCost() + " ; t = " + (System.currentTimeMillis()-timeStart) + "ms");

        timeStart = System.currentTimeMillis();
        day15.expandMap();
        System.out.println("Temps calcul nouvelle map : " + (System.currentTimeMillis()-timeStart) + "ms");
        timeStart = System.currentTimeMillis();
        Path bestPathStep2 = day15.getBestPath();
        System.out.println("result 2 = " + bestPathStep2.totalCost() + " ; t = " + (System.currentTimeMillis()-timeStart) + "ms");
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
        for (int multiplyX = 0; multiplyX < 5; multiplyX++) {
            for (int multiplyY = 0; multiplyY < 5; multiplyY++) {
                for (int x = 0; x < cells.length; x++) {
                    for (int y = 0; y < cells.length; y++) {
                        int value = cells[x][y].value() + (multiplyX) + (multiplyY);
                        while (value > 9) {
                            value -= 9;
                        }
                        int newX = x + (cells.length * multiplyX);
                        int newY = y + (cells.length * multiplyY);
                        newMap[newX][newY] = new Cell(newX, newY, value);
                    }
                }
            }
        }
        cells = newMap;
    }
}
