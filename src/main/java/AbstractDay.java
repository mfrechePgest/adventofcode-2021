import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class AbstractDay {

    BufferedReader br;
    protected String currentLine;

    public void openFile(String fileName) throws IOException {
        br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName)));
        currentLine = br.readLine();
    }

    void closeFile() throws IOException {
        br.close();
    }

    boolean hasMoreLines() {
        return currentLine != null;
    }

    public abstract void readLine() throws IOException;
}
