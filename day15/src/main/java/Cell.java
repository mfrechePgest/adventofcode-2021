import java.util.*;
import java.util.stream.Stream;

public record Cell(int x, int y, int value) {
    public Path getBestPathTo(Cell destination, Cell[][] map) {
        PriorityQueue<Path> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Path::totalCost));

        Set<Cell> visitedCells = new HashSet<>();
        neighbours(map)
                .map(nextStep -> new Path(nextStep, nextStep.value()))
                .forEach(priorityQueue::add);


        while (!priorityQueue.isEmpty()) {
            Path path = priorityQueue.poll();
            Cell lastCell = path.step();
            visitedCells.add(lastCell);
            if (lastCell.equals(destination)) {
                return path;
            } else {
                lastCell.neighbours(map)
                        .filter(c -> !visitedCells.contains(c))
                        .map(nextStep -> new Path(nextStep,
                                path.totalCost() + nextStep.value()
                        ))
                        .forEach(priorityQueue::add);
            }
        }
        return null;


//        return neighbours(map)
//                .map(nextStep -> new Path(this, destination,
//                        new LinkedList<>(Arrays.asList(this, nextStep)),
//                        nextStep.value()))
//                .flatMap((Path p) -> p.getNextStep(map))
//                .toList();
    }

    public Stream<Cell> neighbours(Cell[][] map) {
        return Stream.of(
                        x > 0 ? map[x - 1][y] : null,
                        x < map.length - 1 ? map[x + 1][y] : null,
                        y > 0 ? map[x][y - 1] : null,
                        y < map[0].length - 1 ? map[x][y + 1] : null
                )
                .filter(Objects::nonNull);
    }
}
