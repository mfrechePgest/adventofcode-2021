import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Day5 extends AbstractDay {
	
	private final Map<Point, AtomicInteger> coveredPoints;	

	public Day5() {
		coveredPoints = new HashMap<>();
	}

	public static void main(String[] args) throws IOException {
		Day5 event = new Day5();
		event.openFile("fifth_day_sample.txt");
		while (event.hasMoreLines()) {
			event.readLine();
		}
		Map<Point, AtomicInteger> coveredPoints = event.getCoveredPoints();

		event.closeFile();

		List<Point> overlappingPoints = coveredPoints.entrySet().stream()
				.filter(e -> e.getValue().intValue() > 1).map(Map.Entry::getKey)
				.sorted(Comparator.comparingInt((Point p) -> p.x)
						.thenComparing((Point p) -> p.y)).toList();
		System.out.println("overlappingPoints = " + overlappingPoints);
		System.out.println("overlappingPoints.size() = " + overlappingPoints.size());
		
	}



	public Map<Point, AtomicInteger> getCoveredPoints() {
		return coveredPoints;
	}



	@Override
	public void readLine() throws IOException {
		String[] splitLine = currentLine.split(" -> ");
		Point origin = new Point(splitLine[0]);
		Point destination = new Point(splitLine[1]);
		origin.pointsBetween(destination)
				.forEach(p -> p.addToCoveredPoints(coveredPoints));

		currentLine = br.readLine();
	}

	protected static class Point {
		protected final int x;
		protected final int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Point(String origin) {
			String[] splitted = origin.split(",");
			this.x = Integer.parseInt(splitted[0]);
			this.y = Integer.parseInt(splitted[1]);
		}

		public List<Point> pointsBetween(Point destination) {
			List<Point> result = new ArrayList<>();
			//			System.err.println("Point between "+this+ " and "+destination);
			if (this.x == destination.x) {
				IntStream.range(Math.min(this.y, destination.y),
								Math.max(this.y, destination.y) + 1)
						.mapToObj(y -> new Point(this.x, y))
						//						.peek(System.err::println)
						.forEach(result::add);
			}
			else if (this.y == destination.y) {
				IntStream.range(Math.min(this.x, destination.x),
								Math.max(destination.x, this.x) + 1)
						.mapToObj(x -> new Point(x, this.y))
						//						.peek(System.err::println)
						.forEach(result::add);
			}
			else {
				int originX = Math.min(this.x, destination.x);
				int originY = Math.max(this.y, destination.y);
				boolean monte;
				if ((originY == this.y && originX == this.x)
						|| originX == destination.x && originY == destination.y) {
					// Si l'un des 2 points a minX et maxY c'est que c'est une diagonale descendante
					monte = false;
				}
				else {
					// Sinon diagonale montante, et on part de minY du coup
					monte = true;
					originY = Math.min(this.y, destination.y);
				}
				int finalOriginY = originY;
				//				System.err.println("Diagonale montante ? " +monte);
				//				System.err.println("Origin " +originX + " , " +originY);
				IntStream.range(0,
								Math.max(destination.x, this.x) - Math.min(this.x, destination.x)
										+ 1).mapToObj(range -> new Point(originX + range,
								monte ? finalOriginY + range : finalOriginY - range))
						//						.peek(System.err::println)
						.forEach(result::add);
			}
			return result;
		}

		private void addToCoveredPoints(Map<Point, AtomicInteger> coveredPoints) {
			if (coveredPoints.containsKey(this)) {
				coveredPoints.get(this).getAndIncrement();
			}
			else {
				coveredPoints.put(this, new AtomicInteger(1));
			}
		}

		@Override public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Point point))
				return false;
			return x == point.x && y == point.y;
		}

		@Override public int hashCode() {
			return Objects.hash(x, y);
		}

		public String toString() {
			return "[" + x + "," + y + "]";
		}
	}

}
