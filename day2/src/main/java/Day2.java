import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Day2 {

	public static void main(String[] args) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				Day2.class.getResourceAsStream("second_day_1.txt")))) {
			String line = br.readLine();
			int depth = 0;
			int horizontal = 0;
			int aim = 0;
			while (line != null) {
				String[] instructions = line.split(" ");
				final int range = Integer.parseInt(instructions[1]);
				switch (instructions[0]) {
				case "forward" -> {
					horizontal += range;
					depth += aim * range;
				}
				case "up" -> aim -= range;
				case "down" -> aim += range;
				}

				line = br.readLine();
			}
			System.out.println("horizontal = " + horizontal);
			System.out.println("depth = " + depth);
			System.out.println("depth*horizontal = " + (depth * horizontal));
		}
	}

}
