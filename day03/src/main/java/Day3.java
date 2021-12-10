import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day3 {

	public static void main(String[] args) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				Day3.class.getResourceAsStream("third_day_1.txt")))) {
			String line = br.readLine();						
			List<String> allBinaries = new ArrayList<>();
			while (line != null) {				
				allBinaries.add(line);
				line = br.readLine();
			}
			List<AtomicInteger> oneCount = getOneCount(allBinaries);
			int lineCount = allBinaries.size();
			StringBuilder gammaRateBinary = new StringBuilder();
			StringBuilder epsilonBinary = new StringBuilder();
			assert oneCount != null;
			for (AtomicInteger atomicInteger : oneCount) {
				if (atomicInteger.get() > lineCount / 2) {
					gammaRateBinary.append("1");
					epsilonBinary.append("0");
				}
				else {
					gammaRateBinary.append("0");
					epsilonBinary.append("1");
				}
			}
			
			String oxyGeneratorBinary = allBinaries.stream().max(new SubmarineMetricsComparator(oneCount, lineCount, '1', allBinaries, false)).orElse(null);
			String co2GeneratorBinary = allBinaries.stream().min(new SubmarineMetricsComparator(oneCount, lineCount, '0', allBinaries, false)).orElse(null);			
		
			
			System.out.println("gammaRateBinary = " + gammaRateBinary);
			System.out.println("epsilonBinary = " + epsilonBinary);
			System.out.println("oxyGeneratorBinary = " + oxyGeneratorBinary);
			System.out.println("co2GeneratorBinary = " + co2GeneratorBinary);
			
			int decimalGammaRate = Integer.parseInt(String.valueOf(gammaRateBinary), 2);
			int decimalEpsilon = Integer.parseInt(String.valueOf(epsilonBinary), 2);
			int decimalOxygen = Integer.parseInt(oxyGeneratorBinary, 2);
			int decimalCo2 = Integer.parseInt(co2GeneratorBinary, 2);
			
			System.out.println("decimalGammaRate = " + decimalGammaRate);
			System.out.println("decimalEpsilon = " + decimalEpsilon);
			System.out.println("decimalOxygen = " + decimalOxygen);
			System.out.println("decimalCo2 = " + decimalCo2);
			int result = decimalEpsilon * decimalGammaRate;			
			System.out.println("result = " + result);
			int result2 = decimalOxygen * decimalCo2;
			System.out.println("result2 = " + result2);
		}
	}

	private static class SubmarineMetricsComparator
			implements java.util.Comparator<String> {

		private final List<AtomicInteger> oneCount;
		private final int lineCount;
		private final char equalityMeans;
		private final List<String> allBinaries;
		private boolean debug;

		public SubmarineMetricsComparator(List<AtomicInteger> oneCount, int lineCount, char equalityMeans, List<String> allBinaries, boolean debug) {
			this.oneCount = oneCount;
			this.lineCount = lineCount;
			this.equalityMeans = equalityMeans;
			this.allBinaries = allBinaries;
			this.debug = debug;
		}

		@Override 
		public int compare(String s1, String s2) {
			if ( debug ) System.err.println("Comparing "+s1+ " and "+ s2);
			for ( int i = 0 ; i < s1.length() ; i++ ) {
				int lineCountSubset = ( i == 0 ) ? lineCount : subsetAllBinaries(allBinaries, s1, i).size(); 
				List<AtomicInteger> oneCountSubset = ( i == 0 ) ? oneCount : getOneCount(subsetAllBinaries(allBinaries, s1, i));
								
				char c1 = s1.charAt(i);
				char c2 = s2.charAt(i);
				if ( debug ) System.err.println("Comparing chars : " + c1 + " and " +c2 );
				if ( c1 == c2 ) {
					if ( debug ) System.err.println("Equality, next char...");
					continue;
				}
				double moitie = lineCountSubset / 2d;
				char mostCommonChar;
				if ( debug ) System.err.println("Chars datas : " + lineCountSubset + " ; " + moitie + " ; " + oneCountSubset.get(i).get());
				if ( oneCountSubset.get(i).get() == moitie ) {
					if ( debug ) System.err.println("Equal common char, using : " + equalityMeans);
					mostCommonChar = '1';
				} else if ( oneCountSubset.get(i).get() > moitie ) {
					mostCommonChar = '1';
				} else {
					mostCommonChar = '0';
				}
				if ( debug ) System.err.println("Most common char here : " + mostCommonChar);
				if ( c1 == mostCommonChar ) {
					if ( debug ) System.err.println("c1 : " + c1 + " from " + s1 + " won");
					return 1;					
				} else {
					if ( debug ) System.err.println("c2 : " + c2 + " from " + s2 + " won");
					return -1;
				}
			}
			return 0;
		}

		private List<String> subsetAllBinaries(List<String> allBinaries, String s1, int i) {
			return allBinaries.stream()
					.filter(s -> s.startsWith(s1.substring(0,i)))
					.collect(Collectors.toList());
		}

		
	}
	
	private static List<AtomicInteger> getOneCount(List<String> subsetAllBinaries) {
		List<AtomicInteger> oneCount = null;
		for ( String line : subsetAllBinaries ) {
			if (oneCount == null) {
				oneCount = new ArrayList<>(line.length());
				for (int i = 0; i < line.length(); i++) {
					oneCount.add(new AtomicInteger(0));
				}
			}
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				if (c == '1') {
					oneCount.get(i).incrementAndGet();
				}
			}
		}
		return oneCount;
	}
}
