import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Cell(int x, int y, int value) {


    public Path getBestPathTo(Cell destination, Cell[][] map) {
        PriorityQueue<Path> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(Path::getHeuristique)
        );

        Set<Cell> visitedCells = new HashSet<>();
        Map<Cell, Path> cellInQueue = new HashMap<>();
        neighbours(map)
                .map(nextStep -> new Path(nextStep, nextStep.value(), destination))
                .forEach(path -> addToQueue(path, priorityQueue, cellInQueue));


        while (!priorityQueue.isEmpty()) {
            Path path = priorityQueue.poll();
            cellInQueue.remove(path.step());
            Cell lastCell = path.step();
//            System.out.println("path.sqrDistDestination(destination) = " + path.sqrDistDestination(destination));
            visitedCells.add(lastCell);
            if (lastCell.equals(destination)) {
                return path;
            } else {
                lastCell.neighbours(map)
                        .filter(c -> !visitedCells.contains(c))
                        .map(nextStep -> new Path(nextStep,
                                path.totalCost() + nextStep.value(),
                                destination
                        ))
                        .forEach(p -> addToQueue(p, priorityQueue, cellInQueue));
            }
        }


        System.out.println("On s'est perdu ?");
        System.out.println("====================");
        System.out.println(Arrays.stream(map)
                .map(ligne -> Arrays.stream(ligne)
                        .map(c -> visitedCells.contains(c) ? ConsoleColors.coloredString(String.valueOf(c.value()), ConsoleColors.RED) :
                                String.valueOf(c.value()))
                        .collect(Collectors.joining("")))
                .collect(Collectors.joining("\n")));
        System.out.println("====================");
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addToQueue(Path path, PriorityQueue<Path> priorityQueue, Map<Cell, Path> cellInQueue) {
        if ( cellInQueue.containsKey(path.step()) ) {
            Path autrePath = cellInQueue.get(path.step());
            if ( autrePath.totalCost() > path.totalCost() ) {
                priorityQueue.add(path);
                cellInQueue.put(path.step(), path);
                priorityQueue.remove(autrePath);
            }
        } else {
            priorityQueue.add(path);
            cellInQueue.put(path.step(), path);
        }
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
