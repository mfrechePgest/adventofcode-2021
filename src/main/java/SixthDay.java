import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SixthDay {


    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                SecondDay.class.getResourceAsStream("sixth_day_1.txt")))) {
            String line = br.readLine();

            Map<Lanternfish, Long> fishList = Arrays.stream(line.split(","))
                    .map(Integer::parseInt)
                    .map(Lanternfish::new)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            for (int i = 0; i < 256; i++) {
                fishList.keySet()
                        .forEach(Lanternfish::age);
                Long newBabies = fishList.entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().canHaveBabies())
                        .peek(entry -> entry.getKey().haveBabies())
                        .mapToLong(Map.Entry::getValue)
                        .sum();
                
                fishList.put(new Lanternfish(8), newBabies);
                        
                if (i < 18) {
                    System.out.println("After " + (i+1) + " days: " + fishList.entrySet().stream()
                            .map(f -> f.getKey().timer + "("+f.getValue()+")")
                            .map(String::valueOf)
                            .collect(Collectors.joining(","))
                    );
                }
            }

            System.out.println("Somme poissons : " + fishList.values().stream().mapToLong(l -> l).sum());

        }
    }

    private static class Lanternfish {

        private Integer timer;

        public Lanternfish(Integer timer) {
            this.timer = timer;
        }

        public void age() {
            timer--;
        }

        public boolean canHaveBabies() {
            return timer < 0;
        }

        public Optional<Lanternfish> haveBabies() {
            if (timer < 0) {
                timer = 6;
                return Optional.of(new Lanternfish(8));
            }
            return Optional.empty();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Lanternfish that)) return false;
            return Objects.equals(timer, that.timer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(timer);
        }
    }
}
