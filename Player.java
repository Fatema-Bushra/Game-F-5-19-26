public class Player {

    private Hand hand;
    private int balance;

    public Player(int startingBalance) {

        hand = new Hand();
        balance = startingBalance;
    }

    public Hand getHand() {
        return hand;
    }

    public int getBalance() {
        return balance;
    }

    public void winBet(int bet) {
        balance += bet;
    }

    public void loseBet(int bet) {
        balance -= bet;
    }

    public void resetHand() {
        hand = new Hand();
    }
}