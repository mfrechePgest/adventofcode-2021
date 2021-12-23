import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day23 extends AbstractDay {

    Situation initialSituation;
    private Step step;
    private static final String[] newLines = {"DCBA", "DBAC"};


    public static void main(String[] args) throws IOException {
        Day23 day23 = new Day23("input.txt", Step.STEP_1);

        Situation bestPath = day23.getBestPath();
        System.out.println("Step 1 = " + ConsoleColors.cyan(bestPath.cost()));

        day23 = new Day23("input.txt", Step.STEP_2);

        bestPath = day23.getBestPath();
        System.out.println("Step 2 = " + ConsoleColors.cyan(bestPath.cost()));
    }

    public Day23(String fileName, Step step) throws IOException {
        this.openFile(fileName);
        initialSituation = new Situation(new ArrayList<>(IntStream.range(0, 4).mapToObj((int idx) -> new Chamber(idx, step.getChamberCapacity())).collect(Collectors.toList())),
                new Parkings(),
                0,
                null, null);
        this.step = step;
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }

    @Override
    public void readLine() throws IOException {
        currentLine = currentLine.replace("#", "").trim();
        if (currentLine.matches("[A-Z]*")) {
            for (int i = 0; i < currentLine.length(); i++) {
                Chamber chamber = initialSituation.lstChambers().get(i);
                chamber.push(Amphipod.valueOf("" + currentLine.charAt(i)));
                if (step == Step.STEP_2 && chamber.size() == 1) {
                    chamber.push(Amphipod.valueOf("" + newLines[0].charAt(i)));
                    chamber.push(Amphipod.valueOf("" + newLines[1].charAt(i)));
                }
            }
        }
        currentLine = br.readLine();
    }

    public Situation getBestPath() {
        PriorityQueue<Situation> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(Situation::getHeuristic));


        Set<Situation> visitedCells = new HashSet<>();
        initialSituation.findAllPossibleMoves()
                .forEach(path -> addToQueue(path, priorityQueue));

        long iteration = 0;
        while (!priorityQueue.isEmpty()) {
            Situation path = priorityQueue.poll();
            visitedCells.add(path);
            if (path.isFinished()) {
                return path;
            } else {
                path.findAllPossibleMoves()
                        .stream()
                        .filter(c -> !visitedCells.contains(c))
                        .forEach(p -> addToQueue(p, priorityQueue));
            }
            iteration++;
        }


        System.err.println("On s'est perdu ? en " +iteration + " iterations");
        return null;
    }

    private void addToQueue(Situation path, PriorityQueue<Situation> priorityQueue) {
        Optional<Situation> existingElemInQueue = priorityQueue.stream()
                .filter(s -> s.equals(path))
                .findFirst();
        if (existingElemInQueue.isPresent()) {
            if (path.getHeuristic() < existingElemInQueue.get().getHeuristic()) {
                priorityQueue.remove(existingElemInQueue.get());
                priorityQueue.add(path);
            }
        } else {
            priorityQueue.add(path);
        }
    }


}
