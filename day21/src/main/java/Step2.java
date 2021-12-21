import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Step2 extends Day21 {

    private final Map<String, long[]> cache = new HashMap<>();

    public Step2(int player1Position, int player2Position) {
        super(player1Position, player2Position);
    }

    public long[] playSplittingUniverses() {
        boolean nextPlayer = true;
        return playOneTurn(true, false, player1Score, player2Score, player1Position, player2Position, nextPlayer, nbDiceRolls +1, cache);
    }

    public static long[] playOneTurn(boolean firstStep, boolean changingPlayer, int player1Score, int player2Score, int player1Position, int player2Position, boolean nextPlayer, int nbDiceRolls, Map<String, long[]> cache) {
        String cacheKey = Stream.of(firstStep, changingPlayer, player1Score, player2Score, player1Position, player2Position, nextPlayer, nbDiceRolls)
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        long[] result = new long[2];
        if (!firstStep && changingPlayer) {
            if (player1Score >= 21) {
                result[0]++;
                return result;
            } else if (player2Score >= 21) {
                result[1]++;
                return result;
            }
        }

        Stream.of(1, 2, 3)
                .map(diceValue -> countWins(diceValue, nextPlayer, player1Position, player2Position, nbDiceRolls, player1Score, player2Score, cache))
                .forEach(res -> {
                    result[0] += res[0];
                    result[1] += res[1];
                });
        cache.put(cacheKey, result);
        return result;
    }

    private static long[] countWins(int diceValue, boolean player, int player1Position, int player2Position, int nbDiceRolls, int player1Score, int player2Score, Map<String, long[]> cache) {
        if (player) {
            int nextPosition = calculateNewPosition(player1Position, diceValue);
            boolean changingPlayer = nbDiceRolls != 0 && nbDiceRolls % 3 == 0;
            boolean nextPlayer = !changingPlayer;
            return playOneTurn(false,
                    changingPlayer,
                    changingPlayer ? player1Score + nextPosition : player1Score,
                    player2Score,
                    nextPosition,
                    player2Position,
                    nextPlayer,
                    nbDiceRolls + 1, cache);
        } else {
            int nextPosition = calculateNewPosition(player2Position, diceValue);
            boolean changingPlayer = nbDiceRolls != 3 && nbDiceRolls % 3 == 0;
            return playOneTurn(false,
                    changingPlayer,
                    player1Score,
                    changingPlayer ? player2Score + nextPosition : player2Score,
                    player1Position,
                    nextPosition,
                    changingPlayer,
                    nbDiceRolls + 1, cache);
        }
    }


}
