import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day20Test {

    @Test
    public void sample() throws IOException {
        Day20 day20 = new Day20("sample.txt");

        String image = Arrays.stream(day20.image)
                .map(l -> Arrays.stream(l).map(String::valueOf).collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
        System.out.println(image);

        String binaryNumber = day20.find9CharsCenter();
        assertEquals("000100010", binaryNumber);

        char pixel = day20.getPixelFromBinaryString(binaryNumber);
        assertEquals('1', pixel);
    }

    @Test
    public void sample2() throws IOException {
        Day20 day20 = new Day20("sample2.txt");

        System.out.println(day20.debugString());

        System.out.println("===================================");

        day20.enhanceFullImage();
        System.out.println(day20.debugString());

        System.out.println("===================================");

        Character[][] image2 = day20.enhanceFullImage();        
        System.out.println(day20.debugString());

        long countLight = Arrays.stream(image2)
                .flatMap(Arrays::stream)
                .filter(c -> c.equals('1'))
                .count();

        assertEquals(35, countLight);
    }

    @Test
    public void sample2_50times() throws IOException {
        Day20 day20 = new Day20("sample2.txt");

        System.out.println(day20.debugString());

        System.out.println("===================================");
        Character[][] image2 = null;
        for (int i = 0 ; i < 50 ; i++ ) {
              image2 = day20.enhanceFullImage();
        }
        System.out.println(day20.debugString());

        long countLight = Arrays.stream(image2)
                .flatMap(Arrays::stream)
                .filter(c -> c.equals('1'))
                .count();

        assertEquals(3351, countLight);
    }


}
