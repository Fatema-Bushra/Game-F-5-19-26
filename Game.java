import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

public class Game extends PApplet {

  // --- Visual Layout Fields ---
  HashMap<String, PImage> cardImages;
  PImage backOfCardImage;
  PImage mainMenuBackgroundImage;
  
  // --- High-Low Dice Assets ---
  PImage cupImage;
  HashMap<Integer, PImage> diceImages;

  // --- Information Icons, Navigation & Pop-up States ---
  PImage blackjackIconImage;
  PImage diceIconImage;
  PImage exitIconImage; 
  boolean showRules = false; 
  
  // --- Scroll Tracking Fields ---
  float scrollY = 0;          
  float maxScrollY = 320;     
  
  // Universal icon position/dimensions
  float iconX = 740;
  float iconY = 8;
  float iconSize = 30;

  // Exit button positions
  float exitX = 20;
  float exitY = 530;
  float exitW = 50;
  float exitH = 50;

  PApplet p;
  public static final int APP_WIDTH = 800;
  public static final int APP_HEIGHT = 600;

  // --- Shared Bank Roll System ---
  BankAccount sharedAccount;

  // --- Parallel Backend Engine Objects ---
  Blackjack blackjack;
  DiceGame diceGame;
  
  // --- Global State Controller ---
  // 0 = Main Menu, 1 = Blackjack Screen, 2 = High-Low Dice Screen
  int gameMode = 0; 

  // --- Input State Fields ---
  String bettingInput = "";     
  boolean enteringBet = true;   
  
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
    sharedAccount = new BankAccount(1000);

    blackjack = new Blackjack(sharedAccount);
    diceGame = new DiceGame(sharedAccount);
    
    cardImages = new HashMap<String, PImage>();
    backOfCardImage = loadImage("images/cardBack.png"); 
    mainMenuBackgroundImage = loadImage("images/nesBackwout.png");

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
    
    diceImages = new HashMap<Integer, PImage>();
    cupImage = loadImage("images/cup.png");
    diceImages.put(1, loadImage("images/oneSide.png"));
    diceImages.put(2, loadImage("images/twoSide.png"));
    diceImages.put(3, loadImage("images/threeSide.png"));
    diceImages.put(4, loadImage("images/fourSide.png"));
    diceImages.put(5, loadImage("images/fiveSide.png"));
    diceImages.put(6, loadImage("images/sixSide.png"));

    // FIXED IMAGE PATHS: Appended "images/" directory prefix to properly target data files
    blackjackIconImage = loadImage("images/Blackjack_Icon.png");
    diceIconImage = loadImage("images/High-or-Low_Icon.png");
    exitIconImage = loadImage("images/enter_Icon.png");

    cupX = width / 2f;
    cupY = height / 2f;
    targetCupX = cupX;
    targetCupY = cupY;
  }

  @Override
  public void draw() {
    background(20, 110, 50); 
    
    // --- Screen Router ---
    if (gameMode == 0) {
        drawMainMenu();
        return; 
    }

    drawGlobalHeader();
    
    if (sharedAccount.getBalance() <= 0 && ((gameMode == 1 && blackjack.isRoundOver()) || (gameMode == 2 && diceGame.isRoundOver()))) {
        drawBankruptcyScreen();
        drawExitButton();
        return;
    }

    if (gameMode == 1) {
        drawOriginalBlackjackScreen();
    } else if (gameMode == 2) {
        drawDiceScreen();
    }

    drawActiveInfoIcon();
    drawExitButton();
    
    if (showRules) {
        drawRulesModal();
    }
  }

  /**
   * Renders the initial landing page featuring selection options
   */
  private void drawMainMenu() {
    if (mainMenuBackgroundImage != null) {
      image(mainMenuBackgroundImage, 0, 0, width, height);
  } else {
      background(20, 110, 50); // Fallback green background if image fails to load
  }
    textAlign(CENTER, CENTER);
    fill(255);
    textSize(36);
    text("BACK-ALLEY CASINO", width / 2, 80);
    textSize(16);
    fill(200);
    text("Click on an icon to play", width / 2, 130);

    // Dynamic placement positions for selection buttons
    float boxW = 160;
    float boxH = 200;
    float blackjackBtnX = width / 2 - 230;
    float diceBtnX = width / 2 + 120;
    float btnY = height - 250;

    // Blackjack Selection Option
    if (blackjackIconImage != null) {
        image(blackjackIconImage, blackjackBtnX, btnY, boxW, boxH);
    } else {
        fill(30, 40, 45);
        rect(blackjackBtnX, btnY, boxW, boxH, 10);
    }
    fill(255);
    textSize(18);
    text("Blackjack", blackjackBtnX + boxW / 2 - 20, btnY + boxH + 25);

    // Dice Selection Option
    if (diceIconImage != null) {
        image(diceIconImage, diceBtnX, btnY, boxW, boxH);
    } else {
        fill(30, 40, 45);
        rect(diceBtnX, btnY, boxW, boxH, 10);
    }
    fill(255);
    text("High-or-Low", diceBtnX + boxW / 2, btnY + boxH + 25);
    
    textAlign(LEFT, BASELINE);
  }

  private void drawGlobalHeader() {
    fill(0, 75);
    rect(0, 0, width, 45);
    fill(255);
    textSize(14);
    text("CHECKING BALANCE: $" + sharedAccount.getBalance(), 20, 28);
    
    textAlign(RIGHT);
    fill(255, 215, 0);
    if (gameMode == 1) {
        text("Blackjack Instructions", width - 80, 28);
    } else if (gameMode == 2) {
        text("High-or-Low Instructions", width - 100, 28);
    }
    textAlign(LEFT);
  }

  /**
   * Renders the exit door asset enabling user redirection back to selection arena
   */
  private void drawExitButton() {
    if (exitIconImage != null) {
        image(exitIconImage, exitX, exitY, exitW, exitH);
    } else {
        fill(150, 50, 50);
        rect(exitX, exitY, exitW, exitH, 5);
        fill(255);
        textSize(12);
        textAlign(CENTER, CENTER);
        text("EXIT", exitX + exitW/2, exitY + exitH/2);
        textAlign(LEFT, BASELINE);
    }
  }

  private void drawActiveInfoIcon() {
    PImage activeIcon = (gameMode == 1) ? blackjackIconImage : diceIconImage;

    if (activeIcon != null) {
        image(activeIcon, iconX, iconY, iconSize, iconSize);
    } else {
        fill(gameMode == 1 ? color(0, 150, 255) : color(255, 100, 0));
        ellipse(iconX + iconSize/2, iconY + iconSize/2, iconSize, iconSize);
        fill(255);
        textSize(16);
        textAlign(CENTER, CENTER);
        text("?", iconX + iconSize/2, iconY + iconSize/2);
        textAlign(LEFT, BASELINE);
    }
  }

  private void drawRulesModal() {
    fill(0, 180);
    rect(0, 0, width, height);

    float modalW = 520;
    float modalH = 340;
    float modalX = (width - modalW) / 2;
    float modalY = (height - modalH) / 2;

    fill(30, 40, 45);
    stroke(255, 215, 0);
    strokeWeight(2);
    rect(modalX, modalY, modalW, modalH, 12);
    noStroke();

    fill(255, 215, 0);
    textSize(22);
    textAlign(CENTER);

    String textBody = "";

    if (gameMode == 1) {
        text("INSTRUCTIONS", width / 2, modalY + 45);
        textBody = 
            "- Use your MOUSE WHEEL to scroll content.\n" +
            "- Objective: Get to 21.\n\n" +
            "- Card Values:\n" +
            "   * Face Cards (J, Q, K) count as 10 points.\n" +
            "   * Aces scale dynamically as 1 or 11.\n" +
            "   * Numerical cards equal their face value.\n\n" +
            "- Comparisons:\n" +
            "   * Natural 21's are worth more than made 21's.\n" + 
            "   * Natural 21 vs Natural 21 = Push(Tie).\n" + 
            "   * The side that goes over 21 automatically loses.\n\n" +
            "- Player Actions:\n" +
            "   * Press [H] to Hit (Take a card).\n" +
            "   * Press [S] to Stand (Hold and end turn).\n\n" +
            "- Click anywhere OUTSIDE this box to return to the game.";
    } else {
        text("INSTRUCTIONS", width / 2, modalY + 45);
        textBody = 
            "- Use your MOUSE WHEEL to scroll content.\n" +
            "- Objective: Predict whether the sum of two dice will be higher or lower than the displayed Value.\n\n" +
            "- The Target Value is carried over from the total of the prior round's roll.\n\n" +
            "- Player Controls:\n" +
            "   * Press [H] to bet the total roll will be HIGHER than target.\n" +
            "   * Press [L] to bet the total roll will be LOWER than target.\n\n" +
            "- Payout Multipliers:\n" +
            "   * Correct guesses pay out double your bet amount.\n" +
            "   * Incorrect guesses lose the bet amount.\n" +
            "   * Exact ties prompt a Push outcome.\n\n" +
            "- Click anywhere OUTSIDE this box to return to the game.";
    }

    float clipX = modalX + 35;
    float clipY = modalY + 70;
    float clipW = modalW - 70;
    float clipH = modalH - 95;

    clip(clipX, clipY, clipW, clipH);

    fill(240);
    textSize(14);
    textAlign(LEFT);
    
    text(textBody, clipX, clipY - scrollY, clipW, 850);
    
    noClip();
    textAlign(LEFT);
  }

  @Override
  public void mouseWheel(MouseEvent event) {
    if (showRules) {
        float count = event.getCount(); 
        scrollY += count * 15;          
        
        if (scrollY < 0) {
            scrollY = 0;
        }
        if (scrollY > maxScrollY) {
            scrollY = maxScrollY;
        }
    }
  }

  @Override
  public void mousePressed() {
    // 1. Click Management inside Main Menu Room State
    if (gameMode == 0) {
        float boxW = 160;
        float boxH = 200;
        float blackjackBtnX = width / 2 - 230;
        float diceBtnX = width / 2 + 120;
        float btnY = height - 250;

        // Check if Blackjack icon is pressed
        if (mouseX >= blackjackBtnX && mouseX <= blackjackBtnX + boxW && mouseY >= btnY && mouseY <= btnY + boxH) {
            gameMode = 1;
            bettingInput = "";
            enteringBet = true;
        }
        // Check if Dice game icon is pressed
        else if (mouseX >= diceBtnX && mouseX <= diceBtnX + boxW && mouseY >= btnY && mouseY <= btnY + boxH) {
            gameMode = 2;
            bettingInput = "";
            enteringBet = true;
        }
    } 
    // 2. Click Management inside Live Game Rooms
    else {
        if (showRules) {
            float modalW = 520;
            float modalH = 340;
            float modalX = (width - modalW) / 2;
            float modalY = (height - modalH) / 2;

            if (mouseX < modalX || mouseX > modalX + modalW || mouseY < modalY || mouseY > modalY + modalH) {
                showRules = false;
            }
        } 
        else {
            // Check if info icon is clicked
            if (mouseX >= iconX && mouseX <= iconX + iconSize && mouseY >= iconY && mouseY <= iconY + iconSize) {
                scrollY = 0; 
                showRules = true;
            }
            // Check if exit icon is clicked (Restricted to when a round isn't actively running)
            else if (mouseX >= exitX && mouseX <= exitX + exitW && mouseY >= exitY && mouseY <= exitY + exitH) {
                if ((gameMode == 1 && blackjack.isRoundOver()) || (gameMode == 2 && diceGame.isRoundOver())) {
                    gameMode = 0; // Routes back to the selection hub screen
                }
            }
        }
    }
  }

  private void drawOriginalBlackjackScreen() {
    textSize(24);
    fill(255);
    text("Bankroll Balance: $" + blackjack.getBalance(), 50, 80);
    text("Status: " + blackjack.getGameMessage(), 50, 120);
    
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

    if (enteringBet) {
        fill(255, 255, 150);
        textSize(24);
        text("Type your blackjack bet: $" + bettingInput + "_", 50, 180);
        textSize(14);
        fill(200);
        text("Type digits [0-9], BACKSPACE to edit, ENTER to confirm deal.", 50, 220);
    }
    
    if (!blackjack.getPlayer().getHand().getCards().isEmpty()) {
        fill(255);
        textSize(18);
        if (blackjack.isRoundOver()) {
            text("Dealer Hand (Total: " + blackjack.getDealerHand().getValue() + ")", 50, 250);
        } else {
            text("Dealer Hand (Showing Card value)", 50, 250);
        }
        drawHandVisuals(blackjack.getDealerHand(), 50, 270, true);
        
        fill(255);
        textSize(18);
        text("Your Hand (Total: " + blackjack.getPlayer().getHand().getValue() + ")", 50, 420);
        drawHandVisuals(blackjack.getPlayer().getHand(), 50, 440, false);
        
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
                text("Amount Won: +$" + blackjack.getCurrentBet(), width - 150, height - 80);
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

  private void drawDiceScreen() {
    cupX = lerp(cupX, targetCupX, 0.1f);
    cupY = lerp(cupY, targetCupY, 0.1f);

    textSize(24);
    fill(255);
    text("Dice Room Target Value: " + diceGame.getTargetValue(), 50, 80);

    if (enteringBet) {
        fill(255, 255, 150);
        text("Type your High-Low bet: $" + bettingInput + "_", 50, 150);
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
        text("Current Bet Locked: $" + diceGame.getCurrentBet(), 50, 210);
        
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
        text("Press ENTER to change target benchmark and continue.", 50, 180);
    }

    if (!enteringBet) {
        drawDiceVisuals(width / 2 - 110, height / 2 - 40);
    }

    if (cupImage != null) {
        imageMode(CENTER);
        image(cupImage, cupX, cupY, 180, 180);
        imageMode(CORNER);
    } else {
        fill(30, 30, 30, 220);
        rectMode(CENTER);
        rect(cupX, cupY, 140, 140, 15);
        rectMode(CORNER);
    }
  }

  private void drawDiceVisuals(float startX, float startY) {
    int d1 = diceGame.getDie1();
    int d2 = diceGame.getDie2();
    PImage img1 = diceImages.get(d1);
    PImage img2 = diceImages.get(d2);

    if (img1 != null) {
        image(img1, startX, startY, 100, 100);
    } else {
        fill(255); rect(startX, startY, 100, 100, 10);
        fill(0); textSize(20); text(d1, startX + 40, startY + 55);
    }

    if (img2 != null) {
        image(img2, startX + 120, startY, 100, 100);
    } else {
        fill(255); rect(startX + 120, startY, 100, 100, 10);
        fill(0); textSize(20); text(d2, startX + 160, startY + 55);
    }

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
    text("Press 'R' to completely wipe profiles and claim a fresh $1000 credit line.", 50, 180);
  }

  @Override
  public void keyPressed() {
    if (showRules) {
        return;
    }

    if (key == 'r' || key == 'R') {
        sharedAccount = new BankAccount(1000);
        blackjack = new Blackjack(sharedAccount);
        diceGame = new DiceGame(sharedAccount);
        bettingInput = "";
        enteringBet = true;
        return;
    }

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