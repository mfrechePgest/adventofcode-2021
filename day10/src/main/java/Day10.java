import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day10 {

    private static final List<Character> OPENING_BRACES = Arrays.asList('{', '[', '(', '<');
    private static final List<Character> CLOSING_BRACES = Arrays.asList('}', ']', ')', '>');

    private static final Map<Character, Character> mapOpeners = Map.of(
            '}', '{',
            ']', '[',
            ')', '(',
            '>', '<'
    );

    private static final Map<Character, Integer> mapPoints = Map.of(
            ')', 3,
            ']', 57,
            '}', 1197,
            '>', 25137
    );

    private static final Map<Character, Character> mapClosers = Map.of(
            '{', '}',
            '[', ']',
            '(', ')',
            '<', '>'
    );

    private static final Map<Character, Integer> mapPointsPartTwo = Map.of(
            ')', 1,
            ']', 2,
            '}', 3,
            '>', 4
    );


    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Day10.class.getResourceAsStream("day_10_1.txt")))) {

            String line = br.readLine();

            int resultPart1 = 0;
            List<Long> resultPart2 = new ArrayList<>();
            while (line != null) {

                LinkedList<Character> openingBraces = new LinkedList<>();

                int points = 0;

//                System.out.println("line = " + line);
                boolean corrupted = false;
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (OPENING_BRACES.contains(c)) {
                        openingBraces.add(c);
                    } else if (CLOSING_BRACES.contains(c)) {
//                        System.out.println("Closing braces found " + c + " openings are " + openingBraces);
                        if (openingBraces.getLast().equals(mapOpeners.get(c))) {
                            openingBraces.removeLast();
                        } else {
                            points += mapPoints.get(c);
                            for ( int j = 0 ; j < line.length() ; j++ ) {
                                if (j == i) {
                                    System.out.print(ConsoleColors.coloredString(line.charAt(j),ConsoleColors.RED));
                                } else {
                                    System.out.print(line.charAt(j));
                                }
                            }
                            System.out.println("");
                            corrupted = true;
                            break;
                        }
                    } else {
                        throw new IllegalArgumentException("Rien à faire ici");
                    }
                }

                if ( corrupted ) {
                    System.out.println("points Part 1 = " + ConsoleColors.coloredString(points, ConsoleColors.YELLOW));
                    resultPart1 += points;
                } else {
                    long points2 = 0;
                    System.out.print(line);
                    while(!openingBraces.isEmpty()) {
                        Character lastChar = openingBraces.removeLast();
                        points2 *= 5;
                        Character missingBrace = mapClosers.get(lastChar);
                        points2 += mapPointsPartTwo.get(missingBrace);
                        System.out.print(ConsoleColors.coloredString(missingBrace,ConsoleColors.RED));
                    }
                    System.out.println("");
                    System.out.println("points Part 2 = " + ConsoleColors.coloredString(points2, ConsoleColors.GREEN));
                    resultPart2.add(points2);


                }
                line = br.readLine();
            }

            System.out.println("resultPart1 = " + ConsoleColors.cyan(resultPart1));
            Collections.sort(resultPart2);
            long middleResultPart2 = resultPart2.get(resultPart2.size() / 2);
            System.out.println("resultPart2 = " + ConsoleColors.cyan(middleResultPart2));

        }
    }

}
