import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day8 {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Day8.class.getResourceAsStream("eighth_day_1.txt")))) {
            String line = br.readLine();

            long result = 0;
            while (line != null) {

                String[] splitted = line.split(" \\| ");
                String input = splitted[0];
                String output = splitted[1];

                List<List<Character>> inputs = new ArrayList<>(Arrays.stream(input.split(" "))
                        .map(Day8::getCharsList)
                        .distinct()
                        .toList());

                List<Character> one = findWithCommonSegments(inputs, 2);
                List<Character> four = findWithCommonSegments(inputs, 4);
                List<Character> seven = findWithCommonSegments(inputs, 3);
                List<Character> eight = findWithCommonSegments(inputs, 7);

                assert inputs.size() == 10;

                inputs.remove(one);
                inputs.remove(four);
                inputs.remove(seven);
                inputs.remove(eight);

                List<Character> two = findWithCommonSegments(inputs, 5,
                        new CharOccurences(one, 1),
                        new CharOccurences(four, 2));

                inputs.remove(two);

                List<Character> six = findWithCommonSegments(inputs, 6,
                        new CharOccurences(one, 1),
                        new CharOccurences(four, 3));

                inputs.remove(six);

                List<Character> nine = findWithCommonSegments(inputs, 6,
                        new CharOccurences(four, 4));

                inputs.remove(nine);

                List<Character> five = findWithCommonSegments(inputs, 5,
                        new CharOccurences(nine, 5),
                        new CharOccurences(seven, 2));

                inputs.remove(five);

                List<Character> three = findWithCommonSegments(inputs, 5);

                inputs.remove(three);

                assert inputs.size() == 1;
                List<Character> zero = inputs.get(0);

                List<List<Character>> numbers =
                        Stream.of(zero, one, two, three, four, five, six, seven, eight, nine)
                                .distinct()
                                .toList();

                assert numbers.size() == 10;

                String numberString = Arrays.stream(output.split(" "))
                        .map(s -> toNumber(s, numbers))
                        .collect(Collectors.joining(""));

//                System.out.println("numbers = " + numbers);
//                System.out.println("numberString = " + numberString);

                assert numberString.length() == 4;

                int resultLine = Integer.parseInt(numberString);
                System.out.println("resultLine = " + resultLine);
                result += resultLine;


                line = br.readLine();
            }

            System.out.println("result = " + ConsoleColors.cyan(result));
        }
    }

    private static String toNumber(String s,
                                   List<List<Character>> numbers) {
        List<String> numberResult = IntStream.range(0, numbers.size())
                .filter(i -> s.length() == numbers.get(i).size())
                .filter(i -> s.chars()
                        .mapToObj(c -> (char) c)
                        .allMatch(c -> numbers.get(i).contains(c)))
                .mapToObj(String::valueOf)
                .toList();

        assert numberResult.size() == 1;
        return numberResult.get(0);
    }

    private static List<Character> getCharsList(String s) {
        return s.chars()
                .mapToObj(i -> (char) i)
                .sorted()
                .toList();
    }

    private static List<Character> findWithCommonSegments(List<List<Character>> input, int size, CharOccurences... occurences) {
        Stream<List<Character>> stream = input.stream()
                .filter(s -> s.size() == size);
        for (CharOccurences occurence : occurences) {
            stream = stream.filter(s -> s.stream()
                    .filter(occurence.number::contains)
                    .toList().size() == occurence.occurences
            );
        }
        List<List<Character>> result = stream.toList();
        assert result.size() == 1;
        return result.get(0);
    }

    private record CharOccurences(List<Character> number, int occurences) {
    }


}
