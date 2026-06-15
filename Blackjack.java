public class Blackjack {

    private Player player; // Restored the original Player tracking logic
    private Hand dealerHand;
    private Deck deck;
    private int currentBet;
    private String gameMessage;
    private boolean isRoundOver;

    public Blackjack(BankAccount account) {
        this.player = new Player(account); // Links Player to the shared bank reference
        this.gameMessage = "Place your bet to begin!";
        this.isRoundOver = true;
    }

    public boolean startRound(int bet) {
        if (bet > player.getBalance() || bet <= 0) {
            gameMessage = "Invalid bet amount.";
            return false;
        }

        this.currentBet = bet;
        this.deck = new Deck();
        this.player.resetHand();
        this.dealerHand = new Hand();
        this.isRoundOver = false;

        player.getHand().addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        player.getHand().addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        gameMessage = "Hit or Stand?";
        return true;
    }

    public void hit() {
        if (isRoundOver) return;

        player.getHand().addCard(deck.drawCard());

        if (player.getHand().isBust()) {
            gameMessage = "You busted! Dealer wins.";
            player.loseBet(currentBet);
            isRoundOver = true;
        }
    }

    public void stand() {
        if (isRoundOver) return;

        while (dealerHand.getValue() < 17) {
            dealerHand.addCard(deck.drawCard());
        }

        evaluateWinners();
    }

    private void evaluateWinners() {
        isRoundOver = true;

        int playerTotal = player.getHand().getValue();
        int dealerTotal = dealerHand.getValue();
        boolean playerNatural = player.getHand().isNatural21();
        boolean dealerNatural = dealerHand.isNatural21();
        boolean playerMade21 = (playerTotal == 21 && !playerNatural);
        boolean dealerMade21 = (dealerTotal == 21 && !dealerNatural);

        if (player.getHand().isBust()) {
            gameMessage = "You busted! Dealer wins.";
            player.loseBet(currentBet);
        } 
        else if (dealerHand.isBust()) {
            gameMessage = "Dealer busted! You win!";
            player.winBet(currentBet);
        } 
        else if (playerNatural && !dealerNatural) {
            gameMessage = "Natural 21! You win!";
            player.winBet(currentBet);
        } 
        else if (dealerNatural && !playerNatural) {
            gameMessage = "Dealer natural 21 wins.";
            player.loseBet(currentBet);
        } 
        else if (playerNatural && dealerNatural) {
            gameMessage = "Tie! Both got Natural 21.";
        } 
        else if (playerMade21 && dealerMade21) {
            gameMessage = "Tie! Both made 21.";
        } 
        else if (playerTotal > dealerTotal) {
            gameMessage = "You win!";
            player.winBet(currentBet);
        } 
        else if (playerTotal < dealerTotal) {
            gameMessage = "Dealer wins.";
            player.loseBet(currentBet);
        } 
        else {
            gameMessage = "Push (Tie).";
        }
    }

    public Player getPlayer() { return player; }
    public Hand getDealerHand() { return dealerHand; }
    public String getGameMessage() { return gameMessage; }
    public boolean isRoundOver() { return isRoundOver; }
    public int getBalance() { return player.getBalance(); }
    public int getCurrentBet() { return currentBet; }
}