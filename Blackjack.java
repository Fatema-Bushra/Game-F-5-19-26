
public class Blackjack {

    // --- Fields to keep track of the current game state ---
    private Player player;
    private Hand dealerHand;
    private Deck deck;
    private int currentBet;
    private String gameMessage;
    private boolean isRoundOver;

    /**
     * Constructor initializes a player with a starting bankroll
     */
    public Blackjack(int startingBalance) {
        this.player = new Player(startingBalance);
        this.gameMessage = "Place your bet to begin!";
        this.isRoundOver = true; // Ready for a new round
    }

    // --- Core Methods for the Game Class to use ---

    /**
     * Starts a brand new round of Blackjack with a specified bet.
     * @param bet The amount the player wants to wager
     * @return boolean True if the bet was valid and round started, false otherwise
     */
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

        // Deal initial 2 cards to each
        player.getHand().addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        player.getHand().addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        gameMessage = "Hit or Stand?";

        // The immediate check for Natural 21s has been removed here, 
        // allowing the player to manually press 'H' or 'S' to continue.

        return true;
    }

    /**
     * Player requests another card.
     */
    public void hit() {
        if (isRoundOver) return;

        player.getHand().addCard(deck.drawCard());

        if (player.getHand().isBust()) {
            gameMessage = "You busted! Dealer wins.";
            player.loseBet(currentBet);
            isRoundOver = true;
        }
    }

    /**
     * Player stands. The dealer plays out their hand, and winners are determined.
     */
    public void stand() {
        if (isRoundOver) return;

        // Dealer hits until hitting 17 or higher
        while (dealerHand.getValue() < 17) {
            dealerHand.addCard(deck.drawCard());
        }

        evaluateWinners();
    }

    /**
     * Contains the outcome comparison logic taken directly from your original main method.
     */
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

    // --- Getters to allow Game.java to read the current state for drawing ---

    public Player getPlayer() { return player; }
    public Hand getDealerHand() { return dealerHand; }
    public String getGameMessage() { return gameMessage; }
    public boolean isRoundOver() { return isRoundOver; }
    public int getBalance() { return player.getBalance(); }
}