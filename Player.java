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

    // FIX: Remove the hidden '* 2' multiplier so it deposits the exact payout passed from Blackjack
    public void winBet(int amountToDeposit) {
        account.deposit(amountToDeposit); 
    }

    public void loseBet(int bet) {
        account.withdraw(bet);
    }

    public void resetHand() {
        hand = new Hand();
    }
}