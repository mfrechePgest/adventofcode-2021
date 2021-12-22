import java.io.IOException;
import java.util.Arrays;

public class Day22 extends AbstractDay {

    Boolean[][][] grid;
    private final long minRange, maxRange;
    private final int size;

    public static void main(String[] args) throws IOException {
        Day22 day22 = new Day22(-50, 50);

        day22.processFile("input.txt");

        System.out.println("day22 = " + day22.countCubesOn());
    }

    public Day22(long minRange, long maxRange) {
        this.minRange = minRange;
        this.maxRange = maxRange;
        size = (int) (maxRange - minRange) + 1;
        grid = new Boolean[size][size][size];
    }

    public void processFile(String fileName) throws IOException {
        this.openFile(fileName);
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }

    @Override
    public void readLine() throws IOException {
        boolean newStatus = currentLine.startsWith("on");
        String[] coords = currentLine.split(" ")[1].split(",");
        String[] xRange = coords[0].substring(2).split("\\.\\.");
        String[] yRange = coords[1].substring(2).split("\\.\\.");
        String[] zRange = coords[2].substring(2).split("\\.\\.");
        for (long x = Math.max(minRange,Integer.parseInt(xRange[0])); x <= Math.min(maxRange, Integer.parseInt(xRange[1])) ; x++ ) {
            for (long y = Math.max(minRange,Integer.parseInt(yRange[0])); y <= Math.min(maxRange, Integer.parseInt(yRange[1])) ; y++ ) {
                for (long z = Math.max(minRange,Integer.parseInt(zRange[0])); z <= Math.min(maxRange, Integer.parseInt(zRange[1])) ; z++ ) {
                    grid[(int) (x - minRange)][(int) (y - minRange)][(int) (z - minRange)] = newStatus;
                }
            }
        }
        currentLine = br.readLine();
    }

    public long countCubesOn() {
        return Arrays.stream(grid)
                .flatMap(Arrays::stream)
                .flatMap(Arrays::stream)
                .filter(b -> b != null && b)
                .count();
    }
}
