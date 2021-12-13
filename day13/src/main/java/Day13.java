import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day13 extends AbstractDay {

    List<Cell> dots = new ArrayList<>();
    boolean readingFoldInstructions = false;
    LinkedList<FoldingInstruction> foldingInstructions = new LinkedList<>();
    int maxX = 0;
    int maxY = 0;

    public Day13(String filename) throws IOException {
        this.openFile(filename);
        while (this.hasMoreLines()) {
            this.readLine();
        }
    }

    public static void main(String[] args) throws IOException {
        Day13 day13 = new Day13("day13_1.txt");

        day13.foldOnce();
        System.out.println("Dot count after one fold = " + ConsoleColors.cyan(day13.getDotCount()));

        day13.foldAll();
        System.out.println("After complete folding = \n" + day13.toStringColorized(true));
    }

    public void foldOnce() {
        FoldingInstruction instruction = foldingInstructions.removeFirst();
        // D'abord les points qui bougent pas
        List<Cell> foldedDots = new ArrayList<>(dots.stream()
                .filter(dot -> instruction.isVertical() ? dot.x() < instruction.coord() : dot.y() < instruction.coord())
                .toList());
        // Ensuite les points qui sont pliés
        foldedDots.addAll(
                dots.stream()
                        .filter(dot -> instruction.isVertical() ? dot.x() > instruction.coord() : dot.y() > instruction.coord())
                        .map(dot -> new Cell(
                                instruction.isVertical() ? dot.x() - (2 * (dot.x() - instruction.coord()))
                                        : dot.x(),
                                instruction.isVertical() ? dot.y()
                                        : dot.y() - (2 * (dot.y() - instruction.coord()))
                        ))
                        .filter(dot -> !foldedDots.contains(dot))
                        .toList()
        );
        dots = foldedDots;
        if (instruction.isVertical()) {
            maxX = instruction.coord();
        } else {
            maxY = instruction.coord();
        }
    }

    public void foldAll() {
        while (!foldingInstructions.isEmpty()) {
            foldOnce();
        }
    }

    public String toStringColorized(boolean color) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (dots.contains(new Cell(x, y))) {
                    sb.append(color ? ConsoleColors.coloredString("#",ConsoleColors.RED) : "#");
                } else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    @Override
    public void readLine() throws IOException {
        if (currentLine.isBlank()) {
            readingFoldInstructions = true;
        } else {
            if (readingFoldInstructions) {
                String[] instruction = currentLine.split(" ")[2].split("=");
                foldingInstructions.add(new FoldingInstruction(instruction[0].equals("x"), Integer.parseInt(instruction[1])));
            } else {
                String[] coord = currentLine.split(",");
                Cell cell = new Cell(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
                if (cell.x() > maxX) {
                    maxX = cell.x();
                }
                if (cell.y() > maxY) {
                    maxY = cell.y();
                }
                dots.add(cell);
            }
        }
        currentLine = br.readLine();
    }

    public int getDotCount() {
        return dots.size();
    }
}
