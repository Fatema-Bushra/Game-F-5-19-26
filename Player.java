public class Player {

    private Hand hand;
    private BankAccount account; 

    public Player(BankAccount account) {
        this.hand = new Hand();
        this.account = account;
    }

    public Hand getHand() {
        return hand;
    }

    public int getBalance() {
        return account.getBalance();
    }

    public void winBet(int bet) {
        account.deposit((bet * 2)); // Net addition matching original payout mechanics
    }

    public void loseBet(int bet) {
        account.withdraw(bet);
    }

    public void resetHand() {
        hand = new Hand();
    }
}