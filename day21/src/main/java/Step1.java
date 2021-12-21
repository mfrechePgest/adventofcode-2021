public class Step1 extends Day21 {



    public Step1(int player1Position, int player2Position) {
        super(player1Position, player2Position);
    }


    public void playOneTurn(boolean firstPlayer) {
        int diceValue =
                getNextDiceRoll(nbDiceRolls, 1, 100) +
                        (getNextDiceRoll(nbDiceRolls, 2, 100)) +
                        (getNextDiceRoll(nbDiceRolls, 3, 100));
        if (firstPlayer) {
            player1Position = calculateNewPosition(player1Position, diceValue);
            player1Score += player1Position;
        } else {
            player2Position = calculateNewPosition(player2Position, diceValue);
            player2Score += player2Position;
        }
        nbDiceRolls += 3;
    }

    protected int getNextDiceRoll(int nbDiceRolls, int idx, int maxDiceValue) {
        return (((nbDiceRolls + idx) - 1) % maxDiceValue) + 1;
    }

    public void playUntilOneReach1000() {
        boolean whoseTurn = true;
        while(player1Score < 1000 && player2Score < 1000) {
            playOneTurn(whoseTurn);
            whoseTurn = !whoseTurn;
        }
    }

    public int getNbDiceRolls() {
        return nbDiceRolls;
    }

}
