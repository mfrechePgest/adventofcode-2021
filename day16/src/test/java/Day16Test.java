import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day16Test {

    @Test
    public void sample1_test_1() {
        assertStep1("8A004A801A8002F478", 16);
    }

    @Test
    public void sample2_test_1() {
        assertStep1("620080001611562C8802118E34", 12);
    }

    @Test
    public void sample3_test_1() {
        assertStep1("C0015000016115A2E0802F182340", 23);
    }

    @Test
    public void sample4_test_1() {
        assertStep1("A0016C880162017C3686B18A3D4780", 31);
    }


    @Test
    public void sample1_test_2() {
        assertStep2("C200B40A82", 3);
    }


    @Test
    public void sample2_test_2() {
        assertStep2("04005AC33890", 54);
    }


    @Test
    public void sample3_test_2() {
        assertStep2("880086C3E88112", 7);
    }

    @Test
    public void sample4_test_2() {
        assertStep2("CE00C43D881120", 9);
    }


    @Test
    public void sample5_test_2() {
        assertStep2("D8005AC2A8F0", 1);
    }

    @Test
    public void sample6_test_2() {
        assertStep2("F600BC2D8F", 0);
    }

    @Test
    public void sample7_test_2() {
        assertStep2("9C005AC2F8F0", 0);
    }

    @Test
    public void sample8_test_2() {
        assertStep2("9C0141080250320F1802104A08", 1);
    }


    private void assertStep1(String content, int expectedTotalVersion) {
        // GIVEN
        Day16 day16 = new Day16(content);

        // WHEN
        day16.compute();

        // THEN
        Assertions.assertEquals(expectedTotalVersion, day16.getTotalVersion());
    }

    private void assertStep2(String content, long expectedResult) {
        // GIVEN
        Day16 day16 = new Day16(content);

        // WHEN
        Day16.Progress result = day16.compute();

        // THEN
        Assertions.assertEquals(expectedResult, result.value());
    }

}
