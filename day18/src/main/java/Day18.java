import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Day18 extends AbstractDay {

    List<Element> elements;

    public static void main(String[] args) throws IOException {
        Day18 day18 = new Day18("input.txt");

        Element finalSum = day18.finalSum();

        System.out.println("elem.getMagnitude() = " + finalSum.getMagnitude());
        Element largestMagnitude = day18.largestMagnitude();
        System.out.println("largestMagnitude = " + largestMagnitude);
        System.out.println("largestMagnitude = " + largestMagnitude.getMagnitude());
    }

    public Element finalSum() {
        List<Element> sumElements = new LinkedList<>(elements);
        Element elem = sumElements.remove(0);
        while (!sumElements.isEmpty()) {
            elem = elem.add(sumElements.remove(0));
            elem.reduceUntilFinished();
        }
        return elem;
    }


    public Day18(String fileName) throws IOException {
        this.openFile(fileName);
        elements = new LinkedList<>();
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }


    @Override
    public void readLine() throws IOException {
        elements.add(Element.parseContent(currentLine, 0, null));
        currentLine = br.readLine();
    }

    public Element largestMagnitude() {
        return elements.stream()
                .flatMap(
                        elm1 -> elements.stream().flatMap(elm2 ->
                                Stream.of(elm1.add(elm2), elm2.add(elm1))
                        )
                )
                .peek(Element::reduceUntilFinished)
                .max(Comparator.comparingInt(Element::getMagnitude))
                .orElse(null);
    }
}
