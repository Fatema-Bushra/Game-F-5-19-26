public class Blackjack {

    private Player player; 
    private Hand dealerHand;
    private Deck deck;
    private int currentBet;
    private String gameMessage;
    private boolean isRoundOver;

    public Blackjack(BankAccount account) {
        this.player = new Player(account); 
        this.gameMessage = "Place your bet to begin!";
        this.isRoundOver = true;
    }

    public boolean startRound(int bet) {
        if (bet > player.getBalance() || bet <= 0) {
            gameMessage = "Invalid bet amount.";
            return false;
        }

        this.currentBet = bet;
        
        // Deduct the bet amount upfront
        player.loseBet(this.currentBet); 

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
        } 
        else if (dealerHand.isBust()) {
            gameMessage = "Dealer busted! You win!";
            player.winBet(currentBet * 2); // Return bet + 1x profit
        } 
        else if (playerNatural && !dealerNatural) {
            gameMessage = "Natural 21! You win!";
            player.winBet(currentBet * 2); // Return bet + 1x profit
        } 
        else if (dealerNatural && !playerNatural) {
            gameMessage = "Dealer natural 21 wins.";
        } 
        else if (playerNatural && dealerNatural) {
            gameMessage = "Tie! Both got Natural 21.";
            player.winBet(currentBet); // Push: Return initial bet
        } 
        else if (playerMade21 && dealerMade21) {
            gameMessage = "Tie! Both made 21.";
            player.winBet(currentBet); // Push: Return initial bet
        } 
        else if (playerTotal > dealerTotal) {
            gameMessage = "You win!";
            player.winBet(currentBet * 2); // Return bet + 1x profit
        } 
        else if (playerTotal < dealerTotal) {
            gameMessage = "Dealer wins.";
        } 
        else {
            gameMessage = "Push (Tie).";
            player.winBet(currentBet); // Push: Return initial bet
        }
    }

    public Player getPlayer() { return player; }
    public Hand getDealerHand() { return dealerHand; }
    public String getGameMessage() { return gameMessage; }
    public boolean isRoundOver() { return isRoundOver; }
    public int getBalance() { return player.getBalance(); }
    public int getCurrentBet() { return currentBet; }
}