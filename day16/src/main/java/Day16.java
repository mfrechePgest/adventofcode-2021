import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day16 extends AbstractDay {

    private String inputLine = null;

    private static final Map<Character, String> mapBits = Map.ofEntries(
            Map.entry('0', "0000"),
            Map.entry('1', "0001"),
            Map.entry('2', "0010"),
            Map.entry('3', "0011"),
            Map.entry('4', "0100"),
            Map.entry('5', "0101"),
            Map.entry('6', "0110"),
            Map.entry('7', "0111"),
            Map.entry('8', "1000"),
            Map.entry('9', "1001"),
            Map.entry('A', "1010"),
            Map.entry('B', "1011"),
            Map.entry('C', "1100"),
            Map.entry('D', "1101"),
            Map.entry('E', "1110"),
            Map.entry('F', "1111")
    );
    private int totalVersion = 0;


    public static void main(String[] args) throws IOException {
        Day16 day16 = new Day16("input.txt", true);
        Progress progress = day16.compute();

        System.out.println("totalVersion = " + ConsoleColors.cyan(day16.totalVersion));
        System.out.println("Value = " + ConsoleColors.cyan(progress.value));
    }

    public Progress compute() {
        String binaryString = transformToBinary();
        return analyzePacket(binaryString, 0);
    }

    private Progress analyzePacket(String binaryString, int curseur) {
        int version = Integer.parseInt(binaryString.substring(curseur, curseur + 3), 2);
        totalVersion += version;
        curseur += 3;
        return analyzeSubPacket(binaryString, curseur);
    }

    private Progress analyzeSubPacket(String binaryString, int curseur) {
        int packetType = Integer.parseInt(binaryString.substring(curseur, curseur + 3), 2);
        curseur += 3;
        System.out.println("packetType = " + mapOperationTypes(packetType));
        if (packetType == 4) { // Literal packet
            String packet = "1";
            StringBuilder number = new StringBuilder();
            while (packet.startsWith("1")) {
                packet = binaryString.substring(curseur, curseur + 5);
                number.append(packet.substring(1));
                curseur += 5;
            }
            long literalPackContent = Long.parseLong(number.toString(), 2);
            System.out.println("Literal packet content = " + literalPackContent);
            return new Progress(literalPackContent, curseur);
        } else {
            char lengthTypeId = binaryString.charAt(curseur);
            curseur++;
            switch (lengthTypeId) {
                case '0' -> {
                    int subPacketLength = Integer.parseInt(binaryString.substring(curseur, curseur + 15), 2);
                    curseur += 15;
                    long value = analyzeSubPacketsByLength(binaryString, curseur, subPacketLength, packetType);
                    curseur += subPacketLength;
                    return new Progress(value, curseur);
                }
                case '1' -> {
                    int subPacketCount = Integer.parseInt(binaryString.substring(curseur, curseur + 11), 2);
                    curseur += 11;
                    return analyzeSubPacketsByCount(binaryString, curseur, subPacketCount, packetType);
                }
                default -> throw new IllegalStateException("Unexpected value: " + lengthTypeId);
            }
        }
    }

    private long performOperation(List<Long> values, int packetType) {
        LongStream intStream = values.stream().mapToLong(Long::longValue);
        return switch (packetType) {
            case 0 -> intStream.sum();
            case 1 -> intStream.reduce(1, (i, j) -> i * j);
            case 2 -> intStream.min().orElse(0);
            case 3 -> intStream.max().orElse(0);
            case 5 -> values.get(0) > values.get(1) ? 1 : 0;
            case 6 -> values.get(0) < values.get(1) ? 1 : 0;
            case 7 -> values.get(0).equals(values.get(1)) ? 1 : 0;
            default -> throw new IllegalStateException("Unexpected value: " + packetType);
        };
    }

    private String mapOperationTypes(int packetType) {
        return switch (packetType) {
            case 0 -> "SUM";
            case 1 -> "PRODUCT";
            case 2 -> "MIN";
            case 3 -> "MAX";
            case 4 -> "LITERAL";
            case 5 -> "GREATER";
            case 6 -> "LESSER";
            case 7 -> "EQUALS";
            default -> throw new IllegalStateException("Unexpected value: " + packetType);
        };
    }

    private Progress analyzeSubPacketsByCount(String binaryString, int curseur, int subPacketCount, int packetType) {
        Progress progress = new Progress(0, curseur);
        List<Long> values = new ArrayList<>();
        for (int i = 0; i < subPacketCount; i++) {
            progress = analyzePacket(binaryString, progress.curseur());
            values.add(progress.value);
        }
        return new Progress(performOperation(values, packetType), progress.curseur);
    }

    private long analyzeSubPacketsByLength(String binaryString, int curseur, int subPacketLength, int packetType) {
        String substring = binaryString.substring(curseur, curseur + subPacketLength);
        List<Long> values = new ArrayList<>();
        while (!substring.matches("0*")) {
            Progress progress = analyzePacket(substring, 0);
            substring = substring.substring(progress.curseur);
            values.add(progress.value);
        }
        return performOperation(values, packetType);
    }


    public Day16(String fileName, boolean file) throws IOException {
        this.openFile(fileName);
        while (this.hasMoreLines()) {
            this.readLine();
        }
        this.closeFile();
    }

    public Day16(String content) {
        this.inputLine = content;
    }


    @Override
    public void readLine() throws IOException {
        inputLine = currentLine;
        currentLine = br.readLine();
    }

    public String transformToBinary() {
        return inputLine.chars()
                .mapToObj(c -> (char) c)
                .map(mapBits::get)
                .collect(Collectors.joining());
    }

    public int getTotalVersion() {
        return totalVersion;
    }

    public record Progress(long value, int curseur) {
    }
}
