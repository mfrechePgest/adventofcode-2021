import java.io.IOException;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day24 extends AbstractDay {

    private Function<FourDimensions, FourDimensions> operations;
    private int currentDigit = -1;
    private Map<Integer, BiFunction<Integer, FourDimensions, FourDimensions>> operationsPerDigit = new HashMap<>();
    private Map<Integer, Function<FourDimensions, FourDimensions>> resetFirstInputColumnPerDigit = new HashMap<>();


    public static void main(String[] args) throws IOException {
        Day24 day24 = new Day24("input.txt");

        String result = day24.findBestResult(Comparator.reverseOrder());
        System.out.println("step 1 = " + ConsoleColors.cyan(result));

        result = day24.findBestResult(Comparator.naturalOrder());
        System.out.println("step 2 = " + ConsoleColors.cyan(result));
    }

    public Day24(String fileName) throws IOException {
        this.openFile(fileName);
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }

    public String findLargestModelNumber() {
        String largest = IntStream.range(0, 14).mapToObj(i -> "9").collect(Collectors.joining());
        return findLargestModelNumber(largest);
    }

    public String findLargestModelNumber(String startsWith) {
        return iterateOverIndex(startsWith, 0, new AtomicLong(0), true);
    }

    private String findBestResult(Comparator<Integer> comparator) {
        Map<FourDimensions, String> mapResultats = new HashMap<>();
        mapResultats.put(new FourDimensions(0, 0, 0, 0, null, 0), "");
        for (int i = 0; i < 14; i++) {
            System.out.println("Examen du " + (i + 1) + "e digit...");
            System.out.println(mapResultats.size() + " possibilités");
            int digitIndex = i;
            Map<FourDimensions, String> newMapResult = new HashMap<>();
            IntStream.range(1, 10)
                    .boxed()
                    .sorted(comparator)
                    .flatMap(digit -> mapResultats
                            .entrySet()
                            .stream()
                            .map(entry -> new AbstractMap.SimpleEntry<>(
                                            operationsPerDigit.get(digitIndex)
                                                    // parce que seul Z compte dans l'input, d'un "inp" à l'autre
                                                    .andThen(f -> f.opW("0", FourDimensions::mul))
                                                    .andThen(f -> f.opX("0", FourDimensions::mul))
                                                    .andThen(f -> f.opY("0", FourDimensions::mul))
                                                    .apply(digit, entry.getKey()),
                                            entry.getValue() + digit
                                    )
                            )
                    )
                    .forEach(e -> newMapResult.computeIfAbsent(e.getKey(), k -> e.getValue()));
            mapResultats.clear();
            mapResultats.putAll(newMapResult);
        }
        return mapResultats.entrySet()
                .stream()
                .filter(e -> e.getKey().z() == 0)
                .map(Map.Entry::getValue)
                .findFirst().orElse(null);
    }

    private String iterateOverIndex(String startWith, int index, AtomicLong iterations, boolean firstRound) {
        for (int i = firstRound ? Character.getNumericValue(startWith.charAt(index)) : 9;
             i > 0; i--) {
            String largest;

            char[] charArray = startWith.toCharArray();
            charArray[index] = Character.forDigit(i, 10);
            startWith = String.valueOf(charArray);

            if (index < 13) {
                largest = iterateOverIndex(startWith, index + 1, iterations, firstRound);
                if (largest != null) return largest;
            } else {
                largest = innerIterate(startWith, iterations);
                if (largest != null) return largest;
            }

            firstRound = false;
        }
        return null;
    }

    private String innerIterate(String startWith, AtomicLong iterations) {

        FourDimensions fd = this.apply(startWith);

        if (iterations.longValue() % 10000000 == 0) {
            System.out.println("======================");
            System.out.println("iterations = " + iterations);
            System.out.println("largest = " + startWith);
            System.out.println("fd = " + fd);
            System.out.println("======================");
        }

        iterations.incrementAndGet();

        if (fd.z() == 0) {
            System.out.println("fd = " + fd);
            return startWith;
        }
        return null;
    }

    public FourDimensions apply(String modelNumber) {
        FourDimensions fd = new FourDimensions(0, 0, 0, 0, modelNumber, 0);
        return operations.apply(fd);
    }


    @Override
    public void readLine() throws IOException {
        String[] splitted = currentLine.split(" ");
        Function<FourDimensions, FourDimensions> newOp = switch (splitted[0]) {
            case "inp" -> this.inp(currentLine);
            case "add" -> this.biOperation(splitted, FourDimensions::add);
            case "mul" -> this.biOperation(splitted, FourDimensions::mul);
            case "div" -> this.biOperation(splitted, FourDimensions::div);
            case "mod" -> this.biOperation(splitted, FourDimensions::mod);
            case "eql" -> this.biOperation(splitted, FourDimensions::eql);
            default -> throw new IllegalStateException("Unsupported operation " + currentLine);
        };
        if (operations == null) {
            operations = newOp;
        } else {
            operations = operations.andThen(newOp);
        }
        if (!splitted[0].equals("inp")) {
            operationsPerDigit.put(currentDigit, operationsPerDigit.get(currentDigit).andThen(newOp));
        }
        currentLine = br.readLine();
    }

    private Function<FourDimensions, FourDimensions> inp(String currentLine) {
        currentDigit++;
        switch (currentLine.charAt(4)) {
            case 'w' -> {
                resetFirstInputColumnPerDigit.put(currentDigit, f -> f.opW("0", FourDimensions::mul));
                operationsPerDigit.put(currentDigit, (i, f) -> f.inpW(i));
                return FourDimensions::inpW;
            }
            case 'x' -> {
                resetFirstInputColumnPerDigit.put(currentDigit, f -> f.opX("0", FourDimensions::mul));
                operationsPerDigit.put(currentDigit, (i, f) -> f.inpX(i));
                return FourDimensions::inpX;
            }
            case 'y' -> {
                resetFirstInputColumnPerDigit.put(currentDigit, f -> f.opY("0", FourDimensions::mul));
                operationsPerDigit.put(currentDigit, (i, f) -> f.inpY(i));
                return FourDimensions::inpY;
            }
            case 'z' -> {
                resetFirstInputColumnPerDigit.put(currentDigit, f -> f.opZ("0", FourDimensions::mul));
                operationsPerDigit.put(currentDigit, (i, f) -> f.inpZ(i));
                return FourDimensions::inpZ;
            }
            default -> throw new IllegalArgumentException();
        }
    }


    private Function<FourDimensions, FourDimensions> biOperation(String[] splitted, BiFunction<Integer, Integer, Integer> operation) {
        return switch (splitted[1]) {
            case "w" -> fd -> fd.opW(splitted[2], operation);
            case "x" -> fd -> fd.opX(splitted[2], operation);
            case "y" -> fd -> fd.opY(splitted[2], operation);
            case "z" -> fd -> fd.opZ(splitted[2], operation);
            default -> throw new IllegalArgumentException();
        };
    }
}
