import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;

public class Day9 {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Day9.class.getResourceAsStream("day_9_1.txt")))) {
            String line = br.readLine();

            List<List<Integer>> heightmap = new ArrayList<>();
            while (line != null) {
                heightmap.add(
                        Arrays.stream(line.split("")).map(Integer::parseInt).toList()
                );
                line = br.readLine();
            }

            int resultFirstStep = 0;
            List<List<Cell>> basins = new ArrayList<>();
            for (int i = 0; i < heightmap.size(); i++) {
                for (int j = 0; j < heightmap.get(0).size(); j++) {
                    Integer cell = heightmap.get(i).get(j);
                    Coord coord = new Coord(i, j);
                    if (getNeighbours(heightmap, coord)
                            .allMatch(neigh -> neigh.value > cell)) {
                        System.out.println("cell = " + cell);
                        resultFirstStep += 1 + cell;
                        List<Cell> basinFromCell = findBasinFromCell(heightmap, new Cell(cell, coord), new ArrayList<>());
//                        System.out.println("basinFromCell = " + basinFromCell);
                        basins.add(basinFromCell);
                    }
                }
            }
            System.out.println("resultFirstStep = " + resultFirstStep);
//            System.out.println("basins = " + basins);
            int resultSecondStep = basins.stream()
                    .map(List::size)
                    .sorted(Comparator.reverseOrder())
                    .limit(3)
                    .mapToInt(i -> i)
                    .reduce(1, (a, b) -> a * b);
            System.out.println("resultSecondStep = " + resultSecondStep);


        }
    }

    private static List<Cell> findBasinFromCell(List<List<Integer>> heightmap, Cell cell, List<Cell> alreadyExplored) {
        alreadyExplored.add(cell);
        return Stream.concat(Stream.of(cell),
                        getNeighbours(heightmap, cell.coord)
                                .filter(neigh -> neigh.value < 9)
                                .filter(neigh -> alreadyExplored.stream()
                                        .noneMatch(c -> c.equals(neigh)))
                                .flatMap(neigh -> findBasinFromCell(heightmap, neigh, alreadyExplored).stream())
                )
                .toList();
    }

    public static Stream<Cell> getNeighbours(List<List<Integer>> heightmap, Coord coord) {
        return Stream.of(
                        coord.x > 0 ? new Coord(coord.x - 1, coord.y) : null,
                        coord.x < heightmap.size() - 1 ? new Coord(coord.x + 1, coord.y) : null,
                        coord.y > 0 ? new Coord(coord.x, coord.y - 1) : null,
                        coord.y < heightmap.get(0).size() - 1 ? new Coord(coord.x, coord.y + 1) : null
                )
                .filter(Objects::nonNull)
                .map(c -> new Cell(heightmap.get(c.x).get(c.y), c));
    }

    public record Coord(int x, int y) {
    }

    public record Cell(int value, Coord coord) {
    }


}
