import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day20 extends AbstractDay {

    String algorithm = null;
    Character[][] image = null;
    int currentIndex = 0;
    char lastInfinityChar = '0';

    public static void main(String[] args) throws IOException {
        Day20 day20 = new Day20("input.txt");

        day20.enhanceFullImage();
        Character[][] image2 = day20.enhanceFullImage();

        System.out.println(day20.debugString());

        long countLight = Arrays.stream(image2)
                .flatMap(Arrays::stream)
                .filter(c -> c.equals('1'))
                .count();

        System.out.println("countLight = " + countLight);

        for (int i = 0; i < 48; i++) {
            image2 = day20.enhanceFullImage();
        }

        countLight = Arrays.stream(image2)
                .flatMap(Arrays::stream)
                .filter(c -> c.equals('1'))
                .count();

        System.out.println("countLight = " + countLight);

        System.out.println(day20.debugString());
    }

    public Day20(String fileName) throws IOException {
        this.openFile(fileName);
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }

    public String debugString() {
        return Arrays.stream(image)
                .map(l -> Arrays.stream(l)
                        .map(c -> c == '1' ? ConsoleColors.coloredString('█', ConsoleColors.RED) : '.')
                        .map(String::valueOf)
                        .collect(Collectors.joining())
                )
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void readLine() throws IOException {
        if (algorithm == null) {
            algorithm = currentLine.replace('#', '1').replace('.', '0');
        } else {
            if (!currentLine.isBlank()) {
                if (image == null) {
                    image = new Character[currentLine.length()][currentLine.length()];
                }
                image[currentIndex] = currentLine.replace('#', '1').replace('.', '0').chars().mapToObj(i -> (char) i).toArray(Character[]::new);
                currentIndex++;
            }
        }
        currentLine = br.readLine();
    }

    public Character[][] enhanceFullImage() {
        Character[][] result = new Character[image.length + 4][image[0].length + 4];
        char nextInfinityChar = '0';
        for (int x = -2; x <= image.length + 1; x++) {
            for (int y = -2; y <= image[0].length + 1; y++) {
                char pixel = getPixelFromBinaryString(find9CharsCentered(x, y));
                if (x == -2 && y == -2) {
                    nextInfinityChar = pixel;
                }
                result[x + 2][y + 2] = pixel;
            }
        }
        image = result;
        lastInfinityChar = nextInfinityChar;
        return result;
    }


    public String find9CharsCenter() {
        assert image.length % 2 == 1;
        int center = (int) Math.floor(image.length / 2d);
        assert image[0].length % 2 == 1;
        int vcenter = (int) Math.floor(image[0].length / 2d);

        return find9CharsCentered(center, vcenter);
    }

    private String find9CharsCentered(int center, int vcenter) {
        StringBuilder binaryNumber = new StringBuilder();
        for (int i = center - 1; i <= center + 1; i++) {
            for (int y = vcenter - 1; y <= vcenter + 1; y++) {
                if (i < 0 || y < 0 || i >= image.length || y >= image.length) {
                    binaryNumber.append(lastInfinityChar);
                } else {
                    binaryNumber.append(image[i][y]);
                }
            }
        }
        return binaryNumber.toString();
    }

    public char getPixelFromBinaryString(String binaryString) {
        int index = Integer.parseInt(binaryString, 2);
        return algorithm.charAt(index);
    }
}
