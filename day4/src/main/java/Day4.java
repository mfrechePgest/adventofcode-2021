import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 {

	public static void main(String[] args) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				Day4.class.getResourceAsStream("fourth_day_1.txt")))) {
			String line = br.readLine();

			List<Integer> inputs = Arrays.stream(line.split(",")).map(Integer::parseInt)
					.toList();

			line = br.readLine();
			Board board = null;
			List<Board> boards = new ArrayList<>();
			int idx = 0;
			while (line != null) {
				if (line.isBlank()) {
					board = new Board(idx);
					boards.add(board);
					idx++;
				}
				else {
					assert board != null;
					board.tableau.add(
							Arrays.stream(line.split(" ")).filter(num -> !num.isBlank())
									.map(Integer::parseInt).toList());
				}
				line = br.readLine();
			}

			List<Integer> exploring = new ArrayList<>();
			Board lastWinner = null;
			int lastWinnerInput = 0;
			List<Integer> lastWinnerExploring = new ArrayList<>();
			List<Board> winners = new ArrayList<>();
			for (Integer input : inputs) {
				exploring.add(input);
				System.out.println("Tirage du " + input);
				List<Board> winningBoards = boards.stream()
						.filter(b -> !winners.contains(b))
						.filter(b -> b.isWinning(exploring))
						.toList();
				if (!winningBoards.isEmpty()) {
					Board winningBoard = winningBoards.get(0);
					if (lastWinner == null) {
						System.out.println("Un board a gagné = \n" + winningBoard.toString(exploring));
						printResult(exploring, input, winningBoard);
					} else {
						System.out.println("Nouveau boards gagnants ! BOARDS = " + 
								winningBoards.stream()
								.map(b -> b.idx).toList());
					}
					lastWinner = winningBoards.get(winningBoards.size() - 1);
					lastWinnerInput = input;
					lastWinnerExploring = new ArrayList<>(exploring);
					winners.addAll(winningBoards);
					System.out.println("Il y a mainteanant " + winners.size() + " board gagnants");
				}
				else {
					System.out.println("Aucun board gagnant pour l'instant");
				}
			}
			assert lastWinner != null;
			System.out.println("Le dernier board à gagner est = \n" + lastWinner.toString(lastWinnerExploring));
			List<Integer> finalLastWinnerExploring = lastWinnerExploring;
			printResult(finalLastWinnerExploring, lastWinnerInput, lastWinner);

		}
	}

	private static void printResult(List<Integer> exploring, Integer input,
			Board winningBoard) {
		int somme = winningBoard.tableau.stream().flatMap(Collection::stream)
				.filter(num -> !exploring.contains(num)).mapToInt(Integer::intValue).sum();
		System.out.println("somme = " + somme);
		System.out.println("input = " + input);
		System.out.println("(somme * input) = " + ConsoleColors.cyan(somme * input));
	}

	private static class Board {
		private final int idx;
		List<List<Integer>> tableau = new ArrayList<>();
		
		public Board(int idx) {
			this.idx = idx;
		}

		public String toString() {
			return "\n ============ BOARD " + idx + " ===================== \n" +
					tableau.stream().map(ligne -> ligne.stream().map(String::valueOf)
					.map(s -> String.format("%1$" + 3 + "s", s))
					.collect(Collectors.joining(" "))).collect(Collectors.joining("\n"));
		}
		
		public String toString(List<Integer> exploring) {
			return "\n ============ BOARD " + idx + " ===================== \n" +
					tableau.stream().map(ligne -> ligne.stream()
					.map(String::valueOf)
					.map(s -> exploring.contains(Integer.parseInt(s)) ? "*" + s + "*": s)
					.map(s -> String.format("%1$" + 5 + "s", s))
					.collect(Collectors.joining(" "))).collect(Collectors.joining("\n"));
		}
		
		public boolean isWinning(List<Integer> exploring) {
			return tableau.stream().anyMatch(exploring::containsAll)
					|| 
					IntStream.range(0, tableau.size())
							.anyMatch(colNumber -> isColumnWinning(colNumber, exploring));
		}
		
		public boolean isColumnWinning(int colNumber, List<Integer> exploring) {
			return tableau.stream().map(t -> t.get(colNumber)).allMatch(exploring::contains);
		}
		
	}

}
