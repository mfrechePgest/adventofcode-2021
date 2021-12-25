import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day24 extends AbstractDay {

    private Function<FourDimensions, FourDimensions> operations;
//    private List<Function<FourDimensions, FourDimensions>> revertedOperations = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        Day24 day24 = new Day24("input.txt");

        String result = day24.findLargestModelNumber("99986249646877");

        System.out.println("step 1 = " + ConsoleColors.cyan(result));
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
        String largest = startsWith;
        Long largestNumber = Long.valueOf(largest);
        FourDimensions fd = this.apply(largest);
        return iterateOverIndex(startsWith, 0, new AtomicLong(0), true);
//        while (fd.z() != 0) {
//            if (iterations % 10000000 == 0) {
//                System.out.println("======================");
//                System.out.println("iterations = " + iterations);
//                System.out.println("largest = " + largest);
//                System.out.println("fd = " + fd);
//                System.out.println("======================");
//            }
//            largestNumber--;
//            largest = String.valueOf(largestNumber);
//            while (largest.contains("0")) {
//                largestNumber--;
//                largest = String.valueOf(largestNumber);
//            }
//            if (largest.length() < 14) {
//                throw new IllegalStateException("On n'a pas trouvé !");
//            }
//            fd = this.apply(largest);
//            iterations++;
//        }
//        System.out.println("fd = " + fd);
//        return largest;
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

        if ( fd.z() == 0 ) {
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
//        revertedOperations.add(0,
//                switch (splitted[0]) {
//                    case "inp" -> this.inp(currentLine);
//                    case "add" -> this.biOperation(splitted, FourDimensions::sub);
//                    case "mul" -> this.biOperation(splitted, FourDimensions::div);
//                    case "div" -> this.biOperation(splitted, FourDimensions::mul);
//                    case "mod" -> Function.identity();
//                    case "eql" -> this.biOperation(splitted, FourDimensions::eql);
//                    default -> throw new IllegalStateException("Unsupported operation " + currentLine);
//                }
//        );
        if (operations == null) {
            operations = newOp;
        } else {
            operations = operations.andThen(newOp);
        }
        currentLine = br.readLine();
    }

    private Function<FourDimensions, FourDimensions> inp(String currentLine) {
        return switch (currentLine.charAt(4)) {
            case 'w' -> FourDimensions::inpW;
            case 'x' -> FourDimensions::inpX;
            case 'y' -> FourDimensions::inpY;
            case 'z' -> FourDimensions::inpZ;
            default -> throw new IllegalArgumentException();
        };
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
