public abstract class Day21 {

    protected int player1Position;
    protected int player2Position;


    protected int player1Score = 0;
    protected int player2Score = 0;

    protected int nbDiceRolls = 0;


    public Day21(int player1Position, int player2Position) {
        this.player1Position = player1Position;
        this.player2Position = player2Position;
    }

    public static void main(String[] args) {
        Step1 day21 = new Step1(4, 5);
        day21.playUntilOneReach1000();
        if (day21.player1Score >= 1000) {
            System.out.println("step 1 = " + (day21.getNbDiceRolls() * day21.player2Score));
        } else {
            System.out.println("step 1 = " + (day21.getNbDiceRolls() * day21.player1Score));
        }

        Step2 day21Step2 = new Step2(4, 5);
        long[] result = day21Step2.playSplittingUniverses();
        System.out.println("step 2 = " + Math.max(result[0], result[1]));
    }


    protected static int calculateNewPosition(int initialPosition, int diceValue) {
        int finalPosition = initialPosition;
        finalPosition += diceValue;
        finalPosition = ((finalPosition - 1) % 10) + 1;
        return finalPosition;
    }


    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }
}
