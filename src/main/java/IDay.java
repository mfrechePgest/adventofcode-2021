import java.io.IOException;

public interface IDay {


    void openFile(String fileName) throws IOException;

    void closeFile() throws IOException;

    boolean hasMoreLines();

    void readLine() throws IOException;
}
