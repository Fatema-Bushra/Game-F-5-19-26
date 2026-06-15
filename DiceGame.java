import java.util.Random;

public class DiceGame {

    private BankAccount account; // Direct reference to shared balance
    private int currentBet;
    private int targetValue;
    
    private int die1;
    private int die2;
    
    private String gameMessage;
    private boolean isRoundOver;
    private Random rand;

    public DiceGame(BankAccount account) {
        this.account = account;
        this.rand = new Random();
        this.isRoundOver = true;
        this.gameMessage = "Enter your bet amount to start!";
        
        // Setup initial dealer seed value between [2-12]
        this.targetValue = rand.nextInt(11) + 2;
        
        // Default placeholders for images
        this.die1 = 1;
        this.die2 = 1;
    }

    public boolean lockBet(int bet) {
        if (bet <= 0 || bet > account.getBalance()) {
            gameMessage = "Invalid Bet! Check your bank balance.";
            return false;
        }
        this.currentBet = bet;
        this.isRoundOver = false;
        return true;
    }

    public void playRound(boolean guessHigh) {
        this.die1 = rand.nextInt(6) + 1;
        this.die2 = rand.nextInt(6) + 1;
        int totalRoll = die1 + die2;

        // Rule Case A: Push condition (Equal values)
        if (totalRoll == targetValue) {
            gameMessage = "Tie! Dice matched target value. Bet Returned.";
            isRoundOver = true;
        }
        // Rule Case B: Player Guess Win Condition (Double Bet Payout)
        else if ((guessHigh && totalRoll > targetValue) || (!guessHigh && totalRoll < targetValue)) {
            int netWin = currentBet * 2;
            account.deposit(netWin);
            gameMessage = "Amount Won: +$" + netWin;
            isRoundOver = true;
        }
        // Rule Case C: Failure State (Lose Bet)
        else {
            account.withdraw(currentBet);
            gameMessage = "Amount Lost: -$" + currentBet;
            isRoundOver = true;
        }
    }

    public void prepareNextRound() {
        if (account.getBalance() > 0) {
            this.targetValue = die1 + die2;
        }
    }

    // --- State Getters ---
    public int getBalance() { return account.getBalance(); }
    public int getCurrentBet() { return currentBet; }
    public int getTargetValue() { return targetValue; }
    public int getDie1() { return die1; }
    public int getDie2() { return die2; }
    public String getGameMessage() { return gameMessage; }
    public boolean isRoundOver() { return isRoundOver; }
}