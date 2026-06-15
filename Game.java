import processing.core.PApplet;
import processing.core.PImage;
import java.util.HashMap;

public class Game extends PApplet {

  // --- Visual Layout Fields ---
  HashMap<String, PImage> cardImages;
  PImage backOfCardImage;
  
  // --- High-Low Dice Assets ---
  PImage cupImage;
  HashMap<Integer, PImage> diceImages;

  PApplet p;
  public static final int APP_WIDTH = 800;
  public static final int APP_HEIGHT = 600;

  // --- Shared Bank Roll System ---
  BankAccount sharedAccount;

  // --- Parallel Backend Engine Objects ---
  Blackjack blackjack;
  DiceGame diceGame;
  
  // --- Global State Controller ---
  // 1 = Original Blackjack Screen, 2 = High-Low Dice Screen
  int gameMode = 1; 

  // --- Input State Fields ---
  String bettingInput = "";     // Temporarily holds the numbers the player types
  boolean enteringBet = true;   // Tracks if the player is currently typing a bet
  
  // --- Cup Animation Fields ---
  float cupX, cupY;
  float targetCupX, targetCupY;

  @Override
  public void settings() {
    size(APP_WIDTH, APP_HEIGHT, JAVA2D);
    p = this;
  }

  @Override
  public void setup() {
    // 1. Initialize centralized tracking wallet
    sharedAccount = new BankAccount(1000);

    // 2. Instantiate backends passing the shared wallet instance
    blackjack = new Blackjack(sharedAccount);
    diceGame = new DiceGame(sharedAccount);
    
    // 3. Load Blackjack Card Assets
    cardImages = new HashMap<String, PImage>();
    backOfCardImage = loadImage("images/cardBack.png"); 

    String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
    String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    for (String suit : suits) {
        for (String rank : ranks) {
            String fileName = "images/" + rank + "_of_" + suit + ".png"; 
            PImage img = loadImage(fileName);
            if (img != null) {
                cardImages.put(rank + suit, img);
            }
        }
    }
    
    // 4. Load High-Low Dice Assets
    diceImages = new HashMap<Integer, PImage>();
    cupImage = loadImage("cup.png");
    diceImages.put(1, loadImage("oneSide.png"));
    diceImages.put(2, loadImage("twoSide.png"));
    diceImages.put(3, loadImage("threeSide.png"));
    diceImages.put(4, loadImage("fourSide.png"));
    diceImages.put(5, loadImage("fiveSide.png"));
    diceImages.put(6, loadImage("sixSide.png"));

    // Default cup positions
    cupX = width / 2f;
    cupY = height / 2f;
    targetCupX = cupX;
    targetCupY = cupY;
  }

  @Override
  public void draw() {
    background(20, 110, 50); // Poker room green felt
    
    // Draw Global Persistent Game Selector Header
    drawGlobalHeader();
    
    // Global Bankruptcy Check
    if (sharedAccount.getBalance() <= 0 && ((gameMode == 1 && blackjack.isRoundOver()) || (gameMode == 2 && diceGame.isRoundOver()))) {
        drawBankruptcyScreen();
        return;
    }

    // --- Screen Router ---
    if (gameMode == 1) {
        drawOriginalBlackjackScreen();
    } else if (gameMode == 2) {
        drawDiceScreen();
    }
  }

  private void drawGlobalHeader() {
    fill(0, 75);
    rect(0, 0, width, 45);
    fill(255);
    textSize(14);
    text("WALLET BALANCE: $" + sharedAccount.getBalance(), 20, 28);
    
    textAlign(RIGHT);
    fill(gameMode == 1 ? color(255, 215, 0) : 180);
    text("[Press B] Blackjack Table", width - 220, 28);
    fill(gameMode == 2 ? color(255, 215, 0) : 180);
    text("[Press D] High-Low Dice", width - 30, 28);
    textAlign(LEFT);
  }

  /**
   * Screen 1: Original Blackjack Layout
   */
  private void drawOriginalBlackjackScreen() {
    textSize(24);
    fill(255);
    text("Bankroll Balance: $" + blackjack.getBalance(), 50, 80);
    text("Status: " + blackjack.getGameMessage(), 50, 120);
    
    // Draw Deck Stack
    int deckX = 650;
    int deckY = 80;
    if (backOfCardImage != null) {
        image(backOfCardImage, deckX + 4, deckY + 4, 90, 130);
        image(backOfCardImage, deckX + 2, deckY + 2, 90, 130);
        image(backOfCardImage, deckX, deckY, 90, 130);
    } else {
        fill(40, 40, 40);
        rect(deckX, deckY, 90, 130, 5);
    }

    // Phase 1: Betting Mode
    if (enteringBet) {
        fill(255, 255, 150);
        textSize(24);
        text("Type your bet: $" + bettingInput + "_", 50, 180);
        textSize(14);
        fill(200);
        text("Type digits [0-9], BACKSPACE to edit, ENTER to confirm deal.", 50, 220);
    }
    
    // Phase 2: Active Hands Table Environment
    if (!blackjack.getPlayer().getHand().getCards().isEmpty()) {
        // Dealer Region
        fill(255);
        textSize(18);
        if (blackjack.isRoundOver()) {
            text("Dealer Hand (Total: " + blackjack.getDealerHand().getValue() + ")", 50, 250);
        } else {
            text("Dealer Hand (Showing Card value)", 50, 250);
        }
        drawHandVisuals(blackjack.getDealerHand(), 50, 270, true);
        
        // Player Region
        fill(255);
        textSize(18);
        text("Your Hand (Total: " + blackjack.getPlayer().getHand().getValue() + ")", 50, 420);
        drawHandVisuals(blackjack.getPlayer().getHand(), 50, 440, false);
        
        // Bet & Outcome Status displays
        textSize(24);
        textAlign(CENTER);
        if (!blackjack.isRoundOver()) {
            fill(255, 215, 0); 
            text("Current Bet: $" + blackjack.getCurrentBet(), width - 150, height - 100);
            fill(255, 255, 150);
            textSize(16);
            text("Press 'H' to Hit\nPress 'S' to Stand", width - 150, height - 60);
        } else {
            String outcomeMsg = blackjack.getGameMessage();
            if (outcomeMsg.contains("You win") || outcomeMsg.contains("win!")) {
                fill(50, 255, 50);
                text("Amount Won: +$" + (blackjack.getCurrentBet() * 2), width - 150, height - 80);
            } else if (outcomeMsg.contains("Tie") || outcomeMsg.contains("Push")) {
                fill(200);
                text("Push: No Change", width - 150, height - 80);
            } else {
                fill(255, 50, 50);
                text("Amount Lost: -$" + blackjack.getCurrentBet(), width - 150, height - 80);
            }
            fill(255);
            textSize(14);
            text("Press ENTER to play again", width - 150, height - 40);
        }
        textAlign(LEFT);
    }
  }

  /**
   * Screen 2: High or Low Dice Layout
   */
  private void drawDiceScreen() {
    cupX = lerp(cupX, targetCupX, 0.1f);
    cupY = lerp(cupY, targetCupY, 0.1f);

    textSize(24);
    fill(255);
    text("Current Value: " + diceGame.getTargetValue(), 50, 80);

    if (enteringBet) {
        fill(255, 255, 150);
        text("Type your bet: $" + bettingInput + "_", 50, 150);
        targetCupX = width / 2f;
        targetCupY = height / 2f;
    } 
    else if (!enteringBet && !diceGame.isRoundOver()) {
        fill(255, 255, 150);
        text("Will dice roll HIGHER or LOWER than " + diceGame.getTargetValue() + "?", 50, 140);
        textSize(16);
        fill(255);
        text("Press 'H' for HIGHER  |  Press 'L' for LOWER", 50, 180);
        fill(255, 215, 0);
        text("Current Bet: $" + diceGame.getCurrentBet(), 50, 210);
        
        targetCupX = width - 120;
        targetCupY = 160;
    } 
    else if (diceGame.isRoundOver()) {
        String msg = diceGame.getGameMessage();
        if (msg.contains("Won")) fill(50, 255, 50);
        else if (msg.contains("Returned") || msg.contains("Push")) fill(200);
        else fill(255, 50, 50);
        
        text(msg, 50, 140);
        textSize(16);
        fill(255);
        text("Press ENTER to continue.", 50, 180);
    }

    if (!enteringBet) {
        drawDiceVisuals(width / 2 - 110, height / 2 - 40);
    }

    if (cupImage != null) {
        imageMode(CENTER);
        image(cupImage, cupX, cupY, 180, 180);
        imageMode(CORNER);
    }
  }

  private void drawDiceVisuals(float startX, float startY) {
    int d1 = diceGame.getDie1();
    int d2 = diceGame.getDie2();
    PImage img1 = diceImages.get(d1);
    PImage img2 = diceImages.get(d2);

    if (img1 != null) image(img1, startX, startY, 100, 100);
    if (img2 != null) image(img2, startX + 120, startY, 100, 100);

    fill(255);
    textSize(22);
    textAlign(CENTER);
    text("Dice Total: " + (d1 + d2), startX + 110, startY + 135);
    textAlign(LEFT);
  }

  private void drawBankruptcyScreen() {
    fill(255, 50, 50);
    textSize(32);
    text("BANKRUPT!", 50, 140);
    textSize(18);
    fill(255);
    text("Press 'R' to completely wipe profiles and claim a fresh $1000.", 50, 180);
  }

  @Override
  public void keyPressed() {
    // Global Profile Hard Reboot
    if (key == 'r' || key == 'R') {
        sharedAccount = new BankAccount(1000);
        blackjack = new Blackjack(sharedAccount);
        diceGame = new DiceGame(sharedAccount);
        bettingInput = "";
        enteringBet = true;
        return;
    }

    // Dynamic Navigation Keys (Only switch table views when a round isn't active)
    if ((key == 'b' || key == 'B') && (blackjack.isRoundOver() && diceGame.isRoundOver())) {
        gameMode = 1;
        bettingInput = "";
        enteringBet = true;
        return;
    }
    if ((key == 'd' || key == 'D') && (blackjack.isRoundOver() && diceGame.isRoundOver())) {
        gameMode = 2;
        bettingInput = "";
        enteringBet = true;
        return;
    }

    // Route inputs down to sub-methods based on active room
    if (gameMode == 1) {
        handleBlackjackInput();
    } else if (gameMode == 2) {
        handleDiceInput();
    }
  }

  private void handleBlackjackInput() {
    if (enteringBet) {
        if (key >= '0' && key <= '9') bettingInput += key;
        else if (key == BACKSPACE && bettingInput.length() > 0) bettingInput = bettingInput.substring(0, bettingInput.length() - 1);
        else if (key == ENTER || key == RETURN) {
            if (bettingInput.length() > 0) {
                int bet = Integer.parseInt(bettingInput);
                if (blackjack.startRound(bet)) {
                    enteringBet = false;
                } else {
                    bettingInput = "";
                }
            }
        }
    } else {
        if (!blackjack.isRoundOver()) {
            if (key == 'h' || key == 'H') blackjack.hit();
            else if (key == 's' || key == 'S') blackjack.stand();
        } else {
            if (key == ENTER || key == RETURN) {
                bettingInput = "";
                enteringBet = true;
            }
        }
    }
  }

  private void handleDiceInput() {
    if (enteringBet) {
        if (key >= '0' && key <= '9') bettingInput += key;
        else if (key == BACKSPACE && bettingInput.length() > 0) bettingInput = bettingInput.substring(0, bettingInput.length() - 1);
        else if (key == ENTER || key == RETURN) {
            if (bettingInput.length() > 0) {
                int bet = Integer.parseInt(bettingInput);
                if (diceGame.lockBet(bet)) {
                    enteringBet = false;
                } else {
                    bettingInput = "";
                }
            }
        }
    } 
    else if (!diceGame.isRoundOver()) {
        if (key == 'h' || key == 'H') diceGame.playRound(true);
        else if (key == 'l' || key == 'L') diceGame.playRound(false);
    } 
    else {
        if (key == ENTER || key == RETURN) {
            diceGame.prepareNextRound();
            bettingInput = "";
            enteringBet = true;
        }
    }
  }

  public void drawHandVisuals(Hand hand, float startX, float startY, boolean hideFirstCard) {
    float xOffset = 0;
    int cardWidth = 90;  
    int cardHeight = 130;

    for (int i = 0; i < hand.getCards().size(); i++) {
        Card card = hand.getCards().get(i);
        String lookupKey = card.getRank() + card.getSuit();
        PImage cardImg = cardImages.get(lookupKey);
        float currentX = startX + xOffset;

        if (i == 0 && hideFirstCard && !blackjack.isRoundOver()) {
            if (backOfCardImage != null) image(backOfCardImage, currentX, startY, cardWidth, cardHeight);
            else { fill(0, 0, 150); rect(currentX, startY, cardWidth, cardHeight, 5); }
        } else {
            if (cardImg != null) image(cardImg, currentX, startY, cardWidth, cardHeight);
            else {
                fill(255); stroke(0); rect(currentX, startY, cardWidth, cardHeight, 5);
                fill(0); textSize(16); text(card.getRank() + "\n" + card.getSuit().substring(0,3), currentX + 10, startY + 30);
            }
        }
        xOffset += 110;
    }
  }
}