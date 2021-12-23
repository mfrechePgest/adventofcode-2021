import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day23 extends AbstractDay {

    List<Chamber> lstPods = new ArrayList<>();
    Parkings parkings = new Parkings();

    public Day23(String fileName) throws IOException {
        this.openFile(fileName);
        IntStream.range(0, 4).forEach(i -> lstPods.add(new Chamber(i)));
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
                lstPods.get(i).push(Amphipod.valueOf("" + currentLine.charAt(i)));
            }
        }
        currentLine = br.readLine();
    }



    public boolean isFinished() {
        return lstPods
                .stream()
                .allMatch(
                        c -> c.stream().distinct().count() == 1
                );
    }
}
