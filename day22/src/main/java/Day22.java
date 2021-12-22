import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day22 extends AbstractDay {


    List<EnlightenRegion> regions = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Day22 day22 = new Day22();

        day22.processFile("input.txt");

        System.out.println("day22 step1 = " + ConsoleColors.cyan(day22.countCubesOn(-50, 50)));
        System.out.println("day22 step2 = " + ConsoleColors.cyan(day22.countCubesOn()));
    }

    public Day22() {
    }

    public void processFile(String fileName) throws IOException {
        this.openFile(fileName);
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }

    private void addRegion(EnlightenRegion e) {
        regions.forEach(region -> region.turnOffLightsInRegion(e));
        regions.add(e);
    }


    @Override
    public void readLine() throws IOException {
        boolean newStatus = currentLine.startsWith("on");
        String[] coords = currentLine.split(" ")[1].split(",");
        String[] xRange = coords[0].substring(2).split("\\.\\.");
        String[] yRange = coords[1].substring(2).split("\\.\\.");
        String[] zRange = coords[2].substring(2).split("\\.\\.");
        addRegion(
                new EnlightenRegion(
                        Integer.parseInt(xRange[0]), Integer.parseInt(xRange[1]),
                        Integer.parseInt(yRange[0]), Integer.parseInt(yRange[1]),
                        Integer.parseInt(zRange[0]), Integer.parseInt(zRange[1]),
                        newStatus)
        );
        currentLine = br.readLine();
    }

    public long countCubesOn(int minRange, int maxRange) {
        return regions.stream()
                .filter(EnlightenRegion::isOn)
                .mapToLong(region -> region.countCubesOn(
                minRange, maxRange,
                minRange, maxRange,
                minRange, maxRange
        )).sum();
    }

    public long countCubesOn() {
        return regions.stream()
                .filter(EnlightenRegion::isOn)
                .mapToLong(EnlightenRegion::countCubesOn)
                .sum();
    }
}
