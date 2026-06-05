import processing.core.PApplet;
import processing.core.PImage;
import java.util.HashMap;



public class Game extends PApplet {

  // --- Visual Layout Fields ---
HashMap<String, PImage> cardImages;
PImage backOfCardImage;

  PApplet p;
  public static final int APP_WIDTH = 800;
  public static final int APP_HEIGHT = 600;

  public void settings() {
    //SETUP: Match the screen size to the background image size
    size(APP_WIDTH, APP_HEIGHT, JAVA2D);  //request app size from Processing here
    // Allows p variable to be used by other classes to access PApplet methods
    p = this;
    
  }

  
  // 1. Declare the Blackjack backend object
  Blackjack blackjack;
  String bettingInput = "";     // Temporarily holds the numbers the player types
  boolean enteringBet = true;   // Tracks if the player is currently typing a bet
  
  public void setup() {
    // ... your existing setup code ...
    blackjack = new Blackjack(1000);
    cardImages = new HashMap<String, PImage>();

    // Load the card back image (Make sure you have a card back file or substitute name)
    backOfCardImage = loadImage("images/cardBack.png"); 

    // Generate strings matching your exact file names to load all 52 cards
    String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
    String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    for (String suit : suits) {
        for (String rank : ranks) {
            // Translate short names to full names if your files use them
            String fullRank = rank;

            // Example file path string: "images/2_of_Clubs.png"
            String fileName = "images/" + fullRank + "_of_" + suit + ".png"; 
            
            PImage img = loadImage(fileName);
            if (img != null) {
                // Store using a unique key combination, e.g., "2Clubs" or "AceHearts"
                cardImages.put(rank + suit, img);
            }
        }
    }
}

@Override
public void draw() {
    background(20, 110, 50); // Poker room green felt felt
    
    // 1. Render Table UI Information
    textSize(24);
    fill(255);
    text("Bankroll Balance: $" + blackjack.getBalance(), 50, 40);
    text("Status: " + blackjack.getGameMessage(), 50, 80);
    
    // 2. DRAW THE DECK IN THE MIDDLE
    int deckX = 600;
    int deckY = 100;
    if (backOfCardImage != null) {
        // Draw a stacked effect using 3 offset images
        image(backOfCardImage, deckX + 4, deckY + 4, 90, 130);
        image(backOfCardImage, deckX + 2, deckY + 2, 90, 130);
        image(backOfCardImage, deckX, deckY, 90, 130);
    } else {
        fill(40, 40, 40);
        rect(deckX, deckY, 90, 130, 5);
    }
    fill(255);
    textSize(14);

    // --- PHASE 1: Custom Bet Entry Mode ---
    if (enteringBet && blackjack.getBalance() > 0) {
        fill(255, 255, 150);
        textSize(24);
        text("Type your bet amount: $" + bettingInput + "_", 50, 140);
        
        textSize(14);
        fill(200);
        text("Type digits [0-9], BACKSPACE to edit, ENTER to confirm deal.", 50, 180);
        text("Press 'R' at any time to request a complete engine profile reset.", 50, 200);
    }
    
    // --- PHASE 2: Bankrupt View ---
    else if (blackjack.getBalance() <= 0 && blackjack.isRoundOver()) {
        fill(255, 50, 50);
        textSize(32);
        text("BANKRUPT!", 50, 140);
        textSize(18);
        fill(255);
        text("Press 'R' to wipe profile and transfer a fresh $1000 balance.", 50, 180);
    }

    // --- PHASE 3: Active Card Dealing Hands ---
    if (blackjack.getPlayer().getHand().getCards().size() > 0) {
        
        // --- DEALER HAND (Top Region of Table) ---
        fill(255);
        textSize(18);
        if (blackjack.isRoundOver()) {
            text("Dealer Hand (Total: " + blackjack.getDealerHand().getValue() + ")", 50, 250);
        } else {
            text("Dealer Hand (Showing Card value)", 50, 250);
        }
        // Call helper: hide first card if the game state round is actively running
        drawHandVisuals(blackjack.getDealerHand(), 50, 270, true);
        
        // --- PLAYER HAND (Bottom Region of Table) ---
        fill(255);
        textSize(18);
        text("Your Hand (Total: " + blackjack.getPlayer().getHand().getValue() + ")", 50, 420);
        // Call helper: player cards are always fully visible
        drawHandVisuals(blackjack.getPlayer().getHand(), 50, 430, false);
        
        // Display hotkeys prompt while hand is open
        if (!blackjack.isRoundOver()) {
            fill(255, 255, 150);
            textSize(16);
            text("Options: Press 'H' to Hit  |  Press 'S' to Stand", width - 350, height - 40);
        }
    }
}

  // 4. Trigger methods when GUI inputs happen (like keys or custom Button clicks)
  @Override
public void keyPressed() {
    
    // --- CASE A: Player is Bankrupt ---
    if (blackjack.getBalance() <= 0 && blackjack.isRoundOver()) {
        // Only allow resetting the full game
        if (key == 'r' || key == 'R') {
            blackjack = new Blackjack(1000); // Start totally fresh
            bettingInput = "";
            enteringBet = true;
        }
        return; // Block all other keys
    }

    // --- CASE B: Choosing/Typing a Bet ---
    if (enteringBet) {
        if (key >= '0' && key <= '9') {
            bettingInput += key; // Append typed digit to the string
        } 
        else if (key == BACKSPACE && bettingInput.length() > 0) {
            bettingInput = bettingInput.substring(0, bettingInput.length() - 1); // Delete last digit
        } 
        else if (key == ENTER || key == RETURN) {
            if (bettingInput.length() > 0) {
                int customBet = Integer.parseInt(bettingInput);
                
                // Attempt to start the round with this bet
                boolean success = blackjack.startRound(customBet);
                if (success) {
                    enteringBet = false; // Successfully moved to playing phase
                } else {
                    bettingInput = ""; // Reset incorrect bet input (e.g., betting more than owned)
                }
            }
        }
        // Allow restarting a completely fresh game even before bankrupt
        if (key == 'r' || key == 'R') {
            blackjack = new Blackjack(1000);
            bettingInput = "";
        }
    } 
    
    // --- CASE C: Round is Active (Hit or Stand) ---
    else {
        if (key == 'h' || key == 'H') {
            blackjack.hit();
            
            // If hitting caused a bust, round is instantly over
            if (blackjack.isRoundOver()) {
                handleRoundEnding();
            }
        } 
        else if (key == 's' || key == 'S') {
            blackjack.stand();
            handleRoundEnding();
        }
    }
}

/**
 * Helper method to determine where the player goes next after a hand finishes
 */
private void handleRoundEnding() {
    if (blackjack.getBalance() <= 0) {
        // Player is broke! Force them to restart
        enteringBet = false; 
    } else {
        // Player still has money! Transfer balance over and ask for a new custom bet
        bettingInput = "";
        enteringBet = true; 
    }
}

/**
 * Draws a hand of cards horizontally across the screen
 * @param hand The Hand object containing cards
 * @param startX The starting X position on screen
 * @param startY The starting Y position on screen
 * @param hideFirstCard True if this is the dealer's face-down card setup
 */
public void drawHandVisuals(Hand hand, float startX, float startY, boolean hideFirstCard) {
  float xOffset = 0;
  int cardWidth = 90;   // Adjust card scale size to fit your custom screen layout
  int cardHeight = 130;

  for (int i = 0; i < hand.getCards().size(); i++) {
      Card card = hand.getCards().get(i);
      String lookupKey = card.getRank() + card.getSuit();
      PImage cardImg = cardImages.get(lookupKey);

      // Position for this specific card
      float currentX = startX + xOffset;

      // If it's the dealer's first card and the round is still active, draw it face down
      if (i == 0 && hideFirstCard && !blackjack.isRoundOver()) {
          if (backOfCardImage != null) {
              image(backOfCardImage, currentX, startY, cardWidth, cardHeight);
          } else {
              // Fallback rectangle if card back image is missing
              fill(0, 0, 150);
              rect(currentX, startY, cardWidth, cardHeight, 5);
          }
      } else {
          // Draw the real face-up card image asset
          if (cardImg != null) {
              image(cardImg, currentX, startY, cardWidth, cardHeight);
          } else {
              // Fallback text card box if file loading had a typo path
              fill(255);
              stroke(0);
              rect(currentX, startY, cardWidth, cardHeight, 5);
              fill(0);
              textSize(16);
              text(card.getRank() + "\n" + card.getSuit().substring(0,3), currentX + 10, startY + 30);
          }
      }

      // Space out subsequent cards slightly overlapping to look natural
      xOffset += 110; 
  }
}

}