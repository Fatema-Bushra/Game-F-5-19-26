# AI Prompts History
Paste the prompts that you use to generate any code here:

Example:
"Can you revise this Game class? The text on the buttons from the second screen don't fit. Can you change the background to green? For the tic tac toe game screen, the x's and o's are hidden behind the boxes. Can you place them on top?

## (1) INITIAL PROMPT FOR GAME CLASS
create a black jack game where the player wins if they get to 21 before the dealer, if the dealer busts before the user, if the player has a natural 21 while the dealer has a made 21, and if the user and the dealer both either have natural or made up 21s its a tie also make it so that the user can bet a set amount that doubles when the user wins, when the user ties return that bet amount, and when the user loses take out that bet amount

import java.util.Scanner;
import java.util.Random;

public class Blackjack {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        Random rand = new Random();

        int balance = 1000;

        while (balance > 0) {

            System.out.println("\nBalance: $" + balance);

            System.out.print("Enter your bet (0 to quit): ");
            int bet = scan.nextInt();

            if (bet == 0) {
                System.out.println("Thanks for playing!");
                break;
            }

            if (bet > balance || bet < 0) {
                System.out.println("Invalid bet.");
                continue;
            }

            // Deal cards
            int playerCard1 = drawCard(rand);
            int playerCard2 = drawCard(rand);

            int dealerCard1 = drawCard(rand);
            int dealerCard2 = drawCard(rand);

            int playerTotal = playerCard1 + playerCard2;
            int dealerTotal = dealerCard1 + dealerCard2;

            boolean playerNatural = playerTotal == 21;
            boolean dealerNatural = dealerTotal == 21;

            System.out.println("\nYour cards: " + playerCard1 + " and "
                    + playerCard2 + " (Total = " + playerTotal + ")");

            System.out.println("Dealer shows: " + dealerCard1);

            // Player turn
            while (playerTotal < 21) {

                System.out.print("Hit or Stand? (h/s): ");
                String choice = scan.next();

                if (choice.equalsIgnoreCase("h")) {

                    int card = drawCard(rand);
                    playerTotal += card;

                    System.out.println("You drew a " + card);
                    System.out.println("Your total is now " + playerTotal);

                } else {
                    break;
                }
            }

            // Dealer turn
            while (dealerTotal < 17) {
                dealerTotal += drawCard(rand);
            }

            System.out.println("\nDealer total: " + dealerTotal);
            System.out.println("Player total: " + playerTotal);

            boolean playerMade21 = playerTotal == 21 && !playerNatural;
            boolean dealerMade21 = dealerTotal == 21 && !dealerNatural;

            // Determine winner

            // Player busts
            if (playerTotal > 21) {
                System.out.println("You busted. You lose.");
                balance -= bet;
            }

            // Dealer busts
            else if (dealerTotal > 21) {
                System.out.println("Dealer busted. You win!");
                balance += bet;
            }

            // Natural 21 beats made 21
            else if (playerNatural && dealerMade21) {
                System.out.println("Natural 21 beats dealer's made 21!");
                balance += bet;
            }

            else if (dealerNatural && playerMade21) {
                System.out.println("Dealer's natural 21 beats your made 21.");
                balance -= bet;
            }

            // Both natural 21
            else if (playerNatural && dealerNatural) {
                System.out.println("Both have natural 21. Push.");
            }

            // Both made 21
            else if (playerMade21 && dealerMade21) {
                System.out.println("Both reached 21. Push.");
            }

            // One reaches 21
            else if (playerTotal == 21 && dealerTotal != 21) {
                System.out.println("You got 21 first. You win!");
                balance += bet;
            }

            else if (dealerTotal == 21 && playerTotal != 21) {
                System.out.println("Dealer got 21. You lose.");
                balance -= bet;
            }

            // Regular comparison
            else if (playerTotal > dealerTotal) {
                System.out.println("You win!");
                balance += bet;
            }

            else if (dealerTotal > playerTotal) {
                System.out.println("Dealer wins.");
                balance -= bet;
            }

            else {
                System.out.println("Push.");
            }

            System.out.println("New balance: $" + balance);
        }

        if (balance <= 0) {
            System.out.println("You are out of money.");
        }

        scan.close();
    }

    public static int drawCard(Random rand) {

        int card = rand.nextInt(13) + 1;

        if (card > 10) {
            return 10; // J, Q, K
        }

        if (card == 1) {
            return 11; // Ace
        }

        return card;
    }
}


## (2) REVISION #1 PROMPT
make a blackjack game and all the classes that would be needed

Below is a complete console-based Blackjack game with betting and the rules you requested.

Classes Needed
Card.java
Deck.java
Hand.java
Player.java
Blackjack.java (main game)
Card.java
public class Card {

    private String rank;
    private String suit;
    private int value;

    public Card(String rank, String suit, int value) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public boolean isAce() {
        return rank.equals("A");
    }

    public String toString() {
        return rank + " of " + suit;
    }
}
Deck.java
import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> cards;

    public Deck() {

        cards = new ArrayList<Card>();

        String[] suits = {
            "Hearts",
            "Diamonds",
            "Clubs",
            "Spades"
        };

        String[] ranks = {
            "A","2","3","4","5","6","7",
            "8","9","10","J","Q","K"
        };

        for(String suit : suits) {

            for(String rank : ranks) {

                int value;

                if(rank.equals("A")) {
                    value = 11;
                }
                else if(rank.equals("J")
                        || rank.equals("Q")
                        || rank.equals("K")) {
                    value = 10;
                }
                else {
                    value = Integer.parseInt(rank);
                }

                cards.add(new Card(rank, suit, value));
            }
        }

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {

        if(cards.isEmpty()) {
            return null;
        }

        return cards.remove(0);
    }

    public int size() {
        return cards.size();
    }
}
Hand.java
import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<Card>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public int getValue() {

        int total = 0;
        int aces = 0;

        for(Card card : cards) {

            total += card.getValue();

            if(card.isAce()) {
                aces++;
            }
        }

        while(total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

    public boolean isBust() {
        return getValue() > 21;
    }

    public boolean isNatural21() {

        return cards.size() == 2 &&
               getValue() == 21;
    }

    public String toString() {

        String result = "";

        for(Card card : cards) {
            result += card + "\n";
        }

        return result;
    }
}
Player.java
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
Blackjack.java
import java.util.Scanner;

public class Blackjack {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        Player player = new Player(1000);

        while(player.getBalance() > 0) {

            Deck deck = new Deck();

            player.resetHand();

            Hand dealerHand = new Hand();

            System.out.println("\nBalance: $" +
                    player.getBalance());

            System.out.print("Bet amount (0 to quit): ");

            int bet = scan.nextInt();

            if(bet == 0) {
                break;
            }

            if(bet > player.getBalance()
                    || bet <= 0) {

                System.out.println("Invalid bet.");
                continue;
            }

            player.getHand().addCard(deck.drawCard());
            dealerHand.addCard(deck.drawCard());

            player.getHand().addCard(deck.drawCard());
            dealerHand.addCard(deck.drawCard());

            boolean playerNatural =
                    player.getHand().isNatural21();

            boolean dealerNatural =
                    dealerHand.isNatural21();

            System.out.println("\nYour Hand:");
            System.out.println(player.getHand());

            System.out.println(
                "Dealer shows: "
                + dealerHand.getCards().get(0)
            );

            while(player.getHand().getValue() < 21) {

                System.out.print(
                    "Hit or Stand? (h/s): "
                );

                String choice = scan.next();

                if(choice.equalsIgnoreCase("h")) {

                    player.getHand()
                          .addCard(deck.drawCard());

                    System.out.println(
                        "\nYour Hand:"
                    );

                    System.out.println(
                        player.getHand()
                    );

                    System.out.println(
                        "Total: "
                        + player.getHand().getValue()
                    );
                }
                else {
                    break;
                }
            }

            if(!player.getHand().isBust()) {

                while(dealerHand.getValue() < 17) {
                    dealerHand.addCard(
                        deck.drawCard()
                    );
                }
            }

            int playerTotal =
                    player.getHand().getValue();

            int dealerTotal =
                    dealerHand.getValue();

            System.out.println("\nDealer Hand:");
            System.out.println(dealerHand);

            System.out.println(
                "Dealer Total: "
                + dealerTotal
            );

            System.out.println(
                "Player Total: "
                + playerTotal
            );

            boolean playerMade21 =
                playerTotal == 21 && !playerNatural;

            boolean dealerMade21 =
                dealerTotal == 21 && !dealerNatural;

            if(playerTotal > 21) {

                System.out.println(
                    "You busted. Lose."
                );

                player.loseBet(bet);
            }

            else if(dealerTotal > 21) {

                System.out.println(
                    "Dealer busted. Win!"
                );

                player.winBet(bet);
            }

            else if(playerNatural &&
                    dealerMade21) {

                System.out.println(
                    "Natural 21 beats made 21!"
                );

                player.winBet(bet);
            }

            else if(dealerNatural &&
                    playerMade21) {

                System.out.println(
                    "Dealer natural 21 wins."
                );

                player.loseBet(bet);
            }

            else if(playerNatural &&
                    dealerNatural) {

                System.out.println("Tie!");
            }

            else if(playerMade21 &&
                    dealerMade21) {

                System.out.println("Tie!");
            }

            else if(playerTotal >
                    dealerTotal) {

                System.out.println("You win!");

                player.winBet(bet);
            }

            else if(playerTotal <
                    dealerTotal) {

                System.out.println(
                    "Dealer wins."
                );

                player.loseBet(bet);
            }

            else {

                System.out.println(
                    "Push (Tie)"
                );
            }

            System.out.println(
                "\nBalance: $"
                + player.getBalance()
            );
        }

        System.out.println(
            "\nGame Over. Final Balance: $"
            + player.getBalance()
        );

        scan.close();
    }
}


## (3) REVISION #2 PROMPT
using the logic in the blackjack class, create methods within said blackjack class that can be used in the game class

To transition your text-based, terminal-driven Blackjack game into the GUI-driven Game class (which extends Processing's PApplet), we need to refactor Blackjack.java. Currently, all the logic lives inside a single main method using a while loop and a console Scanner.

In a graphical game framework like Processing, the game loop runs continuously (via draw()), so we cannot use blocking console inputs or infinite terminal loops. Instead, we should turn Blackjack into a state-management object that holds the game data and provides methods that Game can trigger on button clicks or event loops.

Here is how you can rewrite Blackjack.java to make it modular and fully compatible with your Game class.

Refactored Blackjack.java
Replace the contents of your Blackjack.java with this instance-driven version:

Java
import java.util.Scanner;

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

        // Check for immediate Naturals
        if (player.getHand().isNatural21() || dealerHand.isNatural21()) {
            evaluateWinners();
        }

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
How to integrate this inside your Game class
Now, instead of reading text from a console, your Game.java engine can instantiate this refactored class and use Processing's setup or buttons to trigger it.

Here is a quick structural example of how you would link them together in Game.java:

Java
public class Game extends PApplet {
  
  // 1. Declare the Blackjack backend object
  Blackjack blackjack;
  
  public void setup() {
    // 2. Initialize the backend engine with $1000 starting cash
    blackjack = new Blackjack(1000);
  }

  public void draw() {
    background(0, 128, 0); // Green felt poker table background
    
    // 3. Render game state to the screen text elements
    textSize(20);
    fill(255);
    text("Balance: $" + blackjack.getBalance(), 50, 50);
    text("Status: " + blackjack.getGameMessage(), 50, 100);
    
    // Display player cards
    text("Your Hand Value: " + blackjack.getPlayer().getHand().getValue(), 50, 400);
    // (Loop through blackjack.getPlayer().getHand().getCards() to draw them)
  }

  // 4. Trigger methods when GUI inputs happen (like keys or custom Button clicks)
  public void keyPressed() {
    if (key == 'h' || key == 'H') {
      blackjack.hit();
    } else if (key == 's' || key == 'S') {
      blackjack.stand();
    } else if (key == 'b' || key == 'B') {
      blackjack.startRound(100); // Deal a new hand automatically wagering $100
    }
  }
}


## (4)
i want it to be where I can adjust my bet amount, and where I can play more games with the amount that I have in the bank (transferable) unless I either request a new game entirely, or when I go bankrupt

To allow players to type customized bet amounts and continue playing consecutive rounds seamlessly while preserving their balance, we need to adapt our loop design into an interactive graphic flow.

In a graphic framework, we can build this by tracking a few additional states:

Whether the player is currently typing a custom bet (e.g., typing "250" on the keyboard).

Accepting numeric keys (0-9) to let them construct their bet.

Pressing Enter to submit the bet and kick off the deal.

Handling bankruptcy by disabling interactions until they choose to request a brand new game completely.

Here is the update to make your Game class fully interactive.

Step 1: Update Game.java Fields and Setup
Add two new helper variables at the top of your Game.java to handle the interactive betting buffer:

Java
// --- Fields to add near the top of Game.java ---
Blackjack blackjack;
String bettingInput = "";     // Temporarily holds the numbers the player types
boolean enteringBet = true;   // Tracks if the player is currently typing a bet
Inside your setup() method, initialize the engine:

Java
public void setup() {
    // ... your existing setup code ...
    blackjack = new Blackjack(1000); // Game starts with $1000 bankroll
}
Step 2: Implement Typing Logic in keyPressed()
Replace your keyPressed() method in Game.java with this robust typing and phase management structure. It handles numeric typing, deleting, submitting via Enter, and resetting via R:

Java
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
Step 3: Draw the Interactive HUD in draw()
Update your draw() method to clearly show instructions depending on whether they are in the typing stage, playing stage, or out of money:

Java
@Override
public void draw() {
    background(20, 110, 50); // Poker green felt table
    
    textSize(24);
    fill(255); // White text
    
    // Always display current bankroll at the top
    text("Bankroll Balance: $" + blackjack.getBalance(), 50, 50);
    
    // --- VIEW 1: Game Over / Bankrupt ---
    if (blackjack.getBalance() <= 0 && blackjack.isRoundOver()) {
        fill(255, 50, 50); // Red alert text
        text("BANKRUPT! Game Over.", 50, 120);
        fill(255);
        text("Press 'R' to request an entirely new game ($1000).", 50, 160);
    }
    
    // --- VIEW 2: Typing a Custom Bet ---
    else if (enteringBet) {
        text("Status: " + blackjack.getGameMessage(), 50, 100);
        
        fill(255, 255, 150); // Highlighted input box color
        text("Type your bet amount: $" + bettingInput + "_", 50, 160);
        
        fill(200);
        textSize(16);
        text("Press numbers 0-9 to change wager, BACKSPACE to fix, and ENTER to deal.", 50, 200);
        text("Or press 'R' to completely reset bankroll to $1000.", 50, 230);
    } 
    
    // --- VIEW 3: Active Playing Hand ---
    else {
        text("Status: " + blackjack.getGameMessage(), 50, 100);
        
        // Render hands text
        textSize(22);
        text("Your Hand (Total: " + blackjack.getPlayer().getHand().getValue() + "):", 50, 280);
        text(blackjack.getPlayer().getHand().toString(), 50, 310);
        
        text("Dealer Hand (Total: " + blackjack.getDealerHand().getValue() + "):", 450, 280);
        text(blackjack.getDealerHand().toString(), 450, 310);
        
        textSize(16);
        fill(200);
        text("Press 'H' to Hit | Press 'S' to Stand", 50, 520);
    }
}
How the game rules behave now:
Persistent Bankroll: When you win or lose a hand, your balance dynamically adjusts. If you win and have $1150, the interface returns to the bet-entry mode immediately showing Bankroll Balance: $1150.

Custom Wagers: You can type any specific amount like 2, 5, 0 to wager $250. The backend will block it if you try to bet more than you currently have.

Bankruptcy Lock: If your balance falls to $0, it halts gameplay, tells you that you are bankrupt, and restricts actions until you type R to completely regenerate a new profile.

Voluntary Reset: At any point while typing a bet, you can tap R to discard your progress and instantly reset back to a clean $1000 starting game.

## 5
make it so that you can actually see the cards being played (see the dealer and the user's hand with card images" as well as having a deck in the middle of the screen

To render actual card images onto your Processing window, we need a clean way to load the .png files and match them to the Card objects in a hand.

Looking at your project directory, your image files follow a specific naming pattern (for example: 2_of_Clubs.png, 2_of_Diamonds.png). We can dynamically construct these filenames using each card's rank and suit!

Here is the step-by-step implementation to build a beautiful visual card table.

Step 1: Pre-loading Card Images in Game.java
Loading images from the hard drive inside a fast loop like draw() will cause severe game lag. We need to load all 52 card assets into a lookup table (a HashMap) once inside setup().

Add these fields to the top of Game.java:

Java
import java.util.HashMap;

// --- Visual Layout Fields ---
HashMap<String, PImage> cardImages;
PImage backOfCardImage;
Now, initialize and load the assets inside your setup() method:

Java
public void setup() {
    // ... your existing setup code ...
    blackjack = new Blackjack(1000);
    cardImages = new HashMap<String, PImage>();

    // Load the card back image (Make sure you have a card back file or substitute name)
    backOfCardImage = loadImage("images/Card_Back.png"); 

    // Generate strings matching your exact file names to load all 52 cards
    String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
    String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    for (String suit : suits) {
        for (String rank : ranks) {
            // Translate short names to full names if your files use them
            String fullRank = rank;
            if (rank.equals("A")) fullRank = "Ace";
            else if (rank.equals("J")) fullRank = "Jack";
            else if (rank.equals("Q")) fullRank = "Queen";
            else if (rank.equals("K")) fullRank = "King";

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
Step 2: Creating a Render Helper Method
Add this helper method inside Game.java. It loops through a given player or dealer hand and draws the card textures side-by-side. If a card belongs to a dealer and the round isn't finished, it hides the face-down card using the card back texture.

Java
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
Step 3: Updating the Main draw() Layout Engine
Let's assemble a complete card layout view inside your draw() loop. It places the static deck stack in the middle of the screen and renders the distinct hand sets above and below it.

Java
@Override
public void draw() {
    background(20, 110, 50); // Poker room green felt felt
    
    // 1. Render Table UI Information
    textSize(24);
    fill(255);
    text("Bankroll Balance: $" + blackjack.getBalance(), 50, 40);
    text("Status: " + blackjack.getGameMessage(), 50, 80);
    
    // 2. DRAW THE DECK IN THE MIDDLE
    int deckX = width / 2 - 45;
    int deckY = height / 2 - 65;
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
    text("DECK", deckX + 25, deckY + 70);

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
        text("Your Hand (Total: " + blackjack.getPlayer().getHand().getValue() + ")", 50, 470);
        // Call helper: player cards are always fully visible
        drawHandVisuals(blackjack.getPlayer().getHand(), 50, 490, false);
        
        // Display hotkeys prompt while hand is open
        if (!blackjack.isRoundOver()) {
            fill(255, 255, 150);
            textSize(16);
            text("Options: Press 'H' to Hit  |  Press 'S' to Stand", width - 350, height - 40);
        }
    }
}

## 6 
use these images (i put in the image files i had saved)

Let's write out the full, complete version of your Game.java file so everything connects cleanly.

To make sure your card images load perfectly, check your file names. Your files are named like 2_of_Clubs.png and 3_of_Hearts.png. This means inside setup(), we can build paths that perfectly match that layout (e.g., "2_of_Clubs.png").

Here is your updated, fully integrated Game.java class using the custom Processing engine framework:

Java
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PImage;

public class Game extends PApplet {

  // --- Core Layout Sizing Constants ---
  public static final int APP_WIDTH = 1024;
  public static final int APP_HEIGHT = 768;

  // --- Backend Blackjack Engine Data ---
  private Blackjack blackjack;
  private String bettingInput = "";     // Temporary container for typed numbers
  private boolean enteringBet = true;   // State tracker for betting vs playing

  // --- Graphic Image Tables ---
  private HashMap<String, PImage> cardImages;
  private PImage backOfCardImage;

  // Title elements inherited from your base project framework
  String titleText = "BLACKJACK FELT TABLE";
  String name = "Undefined";

  @Override
  public void setup() {
    // 1. Initialize our refactored Blackjack instance with $1000 bankroll
    blackjack = new Blackjack(1000);
    cardImages = new HashMap<String, PImage>();

    // 2. Pre-load your card back design file
    // Note: Change this path string if your card back asset name is different!
    backOfCardImage = loadImage("Card_Back.png");

    // 3. Automate image pre-loading to match your file naming schema
    String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
    String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    for (String suit : suits) {
      for (String rank : ranks) {
        String fullRank = rank;
        if (rank.equals("A")) fullRank = "Ace";
        else if (rank.equals("J")) fullRank = "Jack";
        else if (rank.equals("Q")) fullRank = "Queen";
        else if (rank.equals("K")) fullRank = "King";

        // Generates strings matching your files: "2_of_Clubs.png", "3_of_Hearts.png", etc.
        String fileName = fullRank + "_of_" + suit + ".png";
        PImage img = loadImage(fileName);

        if (img != null) {
          // Use a key combination matching your Card object's fields (e.g., "2Clubs")
          cardImages.put(rank + suit, img);
        }
      }
    }
  }

  @Override
  public void draw() {
    // Clear screen with a rich green casino table tint
    background(20, 110, 50);

    // --- 1. HUD HEADERS ---
    textSize(26);
    fill(255);
    text("Bankroll Balance: $" + blackjack.getBalance(), 50, 50);
    
    textSize(20);
    fill(240, 240, 240);
    text("Status: " + blackjack.getGameMessage(), 50, 95);

    // --- 2. DECK GRAPHIC (Center Stage) ---
    int deckX = width / 2 - 45;
    int deckY = height / 2 - 65;
    drawDeckStack(deckX, deckY);

    // --- 3. STATE CONTROLLER RENDERS ---
    
    // VIEW A: Broken / Bankrupt Phase
    if (blackjack.getBalance() <= 0 && blackjack.isRoundOver()) {
      fill(255, 70, 70);
      textSize(32);
      text("BANKRUPT!", 50, 160);
      textSize(18);
      fill(255);
      text("Press 'R' to wipe your profile and fetch a clean $1000 bankroll.", 50, 205);
    }
    
    // VIEW B: Collecting Custom Input Bet Amount
    else if (enteringBet) {
      fill(255, 255, 140);
      textSize(24);
      text("Type your bet amount: $" + bettingInput + "_", 50, 160);
      
      textSize(14);
      fill(210);
      text("Use keys [0-9] to build your wager, BACKSPACE to fix, and ENTER to deal.", 50, 200);
      text("Or press 'R' to completely reset your cash pool balance to $1000.", 50, 225);
    }

    // --- 4. CARD TABLE RENDERING ---
    // If cards have been dealt, display them physically onto the table
    if (blackjack.getPlayer().getHand().getCards().size() > 0) {
      
      // Dealer Section (Top row)
      fill(255);
      textSize(18);
      if (blackjack.isRoundOver()) {
        text("Dealer Hand (Total: " + blackjack.getDealerHand().getValue() + ")", 50, 260);
      } else {
        text("Dealer Hand (Showing Card)", 50, 260);
      }
      // Passing true hides the first card until the round concludes
      drawHandVisuals(blackjack.getDealerHand(), 50, 280, true);

      // Player Section (Bottom row)
      fill(255);
      textSize(18);
      text("Your Hand (Total: " + blackjack.getPlayer().getHand().getValue() + ")", 50, 490);
      // Passing false leaves all player cards open
      drawHandVisuals(blackjack.getPlayer().getHand(), 50, 510, false);

      // Controls guide overlay
      if (!blackjack.isRoundOver()) {
        fill(255, 255, 140);
        textSize(16);
        text("Options: Press 'H' to Hit  |  Press 'S' to Stand", width - 380, height - 50);
      }
    }
  }

  @Override
  public void keyPressed() {
    // Global profile reset checker when a round isn't active
    if (blackjack.isRoundOver() && (key == 'r' || key == 'R')) {
      blackjack = new Blackjack(1000);
      bettingInput = "";
      enteringBet = true;
      return;
    }

    // PHASE 1: Typing Bet Input
    if (enteringBet) {
      if (key >= '0' && key <= '9') {
        bettingInput += key;
      } 
      else if (key == BACKSPACE && bettingInput.length() > 0) {
        bettingInput = bettingInput.substring(0, bettingInput.length() - 1);
      } 
      else if (key == ENTER || key == RETURN) {
        if (bettingInput.length() > 0) {
          int customBet = Integer.parseInt(bettingInput);
          boolean success = blackjack.startRound(customBet);
          if (success) {
            enteringBet = false; 
          } else {
            bettingInput = ""; 
          }
        }
      }
    } 
    
    // PHASE 2: Live Action Hand Options (Hit or Stand)
    else {
      if (key == 'h' || key == 'H') {
        blackjack.hit();
        if (blackjack.isRoundOver()) {
          processRoundConclusion();
        }
      } 
      else if (key == 's' || key == 'S') {
        blackjack.stand();
        processRoundConclusion();
      }
    }
  }

  /**
   * Evaluates bankroll limits to determine next graphical layout phase
   */
  private void processRoundConclusion() {
    if (blackjack.getBalance() <= 0) {
      enteringBet = false; // Player is broke, locking controls
    } else {
      bettingInput = "";    // Clear out last bet text buffer
      enteringBet = true;   // Bring up typing box for the next loop
    }
  }

  /**
   * Helper routine to lay out a Hand array horizontally across the felt
   */
  public void drawHandVisuals(Hand hand, float startX, float startY, boolean hideFirstCard) {
    float xOffset = 0;
    int cardWidth = 100;
    int cardHeight = 145;

    for (int i = 0; i < hand.getCards().size(); i++) {
      Card card = hand.getCards().get(i);
      String lookupKey = card.getRank() + card.getSuit();
      PImage cardImg = cardImages.get(lookupKey);

      float currentX = startX + xOffset;

      // Mask dealer hole card if the deal phase is unresolved
      if (i == 0 && hideFirstCard && !blackjack.isRoundOver()) {
        if (backOfCardImage != null) {
          image(backOfCardImage, currentX, startY, cardWidth, cardHeight);
        } else {
          fill(30, 60, 140);
          rect(currentX, startY, cardWidth, cardHeight, 7);
        }
      } else {
        if (cardImg != null) {
          image(cardImg, currentX, startY, cardWidth, cardHeight);
        } else {
          // Robust text rectangle fail-safe if file setup path contains an error
          fill(255);
          stroke(0);
          rect(currentX, startY, cardWidth, cardHeight, 7);
          fill(0);
          textSize(18);
          text(card.getRank(), currentX + 12, startY + 30);
          textSize(12);
          text(card.getSuit(), currentX + 12, startY + 60);
        }
      }
      xOffset += 120; // Sets uniform card separation space
    }
  }

  /**
   * Renders a small visual card cluster stack representing the table deck
   */
  private void drawDeckStack(int x, int y) {
    int w = 100;
    int h = 145;
    if (backOfCardImage != null) {
      image(backOfCardImage, x + 4, y + 4, w, h);
      image(backOfCardImage, x + 2, y + 2, w, h);
      image(backOfCardImage, x, y, w, h);
    } else {
      fill(50);
      rect(x, y, w, h, 7);
    }
    fill(255);
    textSize(12);
    text("DECK", x + 32, y + 78);
  }
}

## 7
make it so that the user can continue playing even if either they or the dealer has a natural 21

To allow the player to continue playing even if a natural 21 is dealt off the bat, you need to remove the automatic check that immediately ends the round.

In your Blackjack.java file, the startRound() method currently checks for a natural 21 right after dealing the first four cards and automatically calls evaluateWinners().

Here is the modified startRound() method with that automatic check removed. You can completely replace your existing method with this one:

Updated Blackjack.java
Java
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
Why this works:
By removing the if (player.getHand().isNatural21() || dealerHand.isNatural21()) { evaluateWinners(); } block, isRoundOver stays false. This leaves control in the hands of the player in Game.java, where they can press 'H' to Hit or 'S' to Stand. Once they eventually choose to stand, the existing evaluateWinners() logic will still correctly identify the natural 21s and distribute the payouts accordingly.

## 8
edit the game class so that the bet amount is displayed throughout the round, then changes into amount won or amount lost if the user wins or loses a round (if it;s a tie display no change)

To dynamically display the bet and the resulting win/loss amounts, we need to make a small addition to your Blackjack.java backend to expose the bet amount, and then update the rendering logic in Game.java.

1. Add a Getter to Blackjack.java
Currently, Blackjack.java tracks the currentBet, but Game.java has no way to read it once the round starts.

Add this single getter method to the bottom of your Blackjack.java file alongside your other getters:

Java
    public int getCurrentBet() { 
        return currentBet; 
    }
2. Update the Draw Loop in Game.java
Now, we can update Phase 3 of the draw() method in Game.java. We will use standard Processing text(), fill(), and textAlign() methods to center the bet amount on the screen.

When blackjack.isRoundOver() becomes true, we can check the blackjack.getGameMessage() to determine if the text should turn green for a win, red for a loss, or gray for a tie.

Replace your existing // --- PHASE 3: Active Card Dealing Hands --- block with this updated version:

Java
    // --- PHASE 3: Active Card Dealing Hands ---
    if (!blackjack.getPlayer().getHand().getCards().isEmpty()) {
        
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
        
        // --- NEW: Bet / Outcome Display in Middle of Screen ---
        textSize(24);
        textAlign(CENTER); // Center align for middle table text
        
        if (!blackjack.isRoundOver()) {
            // Round is active: show the current bet in Gold
            fill(255, 215, 0); 
            text("Current Bet: $" + blackjack.getCurrentBet(), width / 2, 360);
        } else {
            // Round is over: determine win, loss, or tie based on the game message
            String outcomeMsg = blackjack.getGameMessage();
            
            if (outcomeMsg.contains("You win")) {
                fill(50, 255, 50); // Green for Win
                text("Amount Won: +$" + blackjack.getCurrentBet(), width / 2, 360);
            } else if (outcomeMsg.contains("Tie") || outcomeMsg.contains("Push")) {
                fill(200); // Gray for Tie
                text("Push: No Change", width / 2, 360);
            } else {
                fill(255, 50, 50); // Red for Loss
                text("Amount Lost: -$" + blackjack.getCurrentBet(), width / 2, 360);
            }
        }
        
        textAlign(LEFT); // Reset text alignment to standard left for other UI elements
        
        // Display hotkeys prompt while hand is open
        if (!blackjack.isRoundOver()) {
            fill(255, 255, 150);
            textSize(16);
            text("Options: Press 'H' to Hit  |  Press 'S' to Stand", width - 350, height - 40);
        }
    }

## 9
create a high or low game using dice where when the user plays the first round, they are guessing if the result from the dice is going to be higher or lower than the number randomly generated (between 2-12) by the dealer (that's visible to the user), after that first round the result of the dice turns into the number that the user is going to continue guessing whether or not the new result of the dice will be higher or lower than it (so if the number the dealer has is 4, the result of the first round is 2, if the user continues playing, than they will be guessing if the new result from the dice is higher or lower than 2), ensure that the user can still bet and that the bet is visible to the user throughout the game until the results are in (then you say the amount either gained/lost/or given back to the user) --> if the user wins the round they get double the amount they put in as a bet, if the dice results in the same value as the value being compared to (trying to guess if the dice will be higher or lower than 2, but you get 2 with the dice) the user will get their bet returned to them, and if the user loses the round they will lose the amount they put in as a bet, make this a visual game using the images provided where the cup image starts the round being in the center of the user's screen, then after the user enters in their bet, the cup moves aside to reveal two dice with a value being displayed (so two of the dice images), ensure that there's a text displaying the calculated value of the dice with the images matching the value being displayed (the value should be no less than 2, but not greater than 12), ensure that the user can only bet positive numbers that is less or equal to their current balance

generate necessary classes to accomplish this

Here is the complete, refactored setup to transform your game into a High or Low Dice Game.

We will structure this cleanly across two files: Game.java (handling the visual rendering, inputs, animations, and image assets using Processing) and a brand-new backend engine, DiceGame.java, which tracks your banking math, win conditions, and core rules.

1. The Core Game View: Game.java
This replaces your previous file. It manages typing your bets, animating the cup image away from the center when a round begins, displaying the matching pair of dice graphics, and controlling states.

Java
import processing.core.PApplet;
import processing.core.PImage;
import java.util.HashMap;

public class Game extends PApplet {

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;

    // --- Media Assets ---
    PImage cupImage;
    HashMap<Integer, PImage> diceImages;

    // --- Backend Object ---
    DiceGame backend;

    // --- UI/Input States ---
    String bettingInput = "";     
    boolean enteringBet = true;   
    char currentGuess = ' '; // 'H' for High, 'L' for Low
    
    // --- Animation Fields ---
    float cupX, cupY;
    float targetCupX, targetCupY;

    @Override
    public void settings() {
        size(APP_WIDTH, APP_HEIGHT, JAVA2D);
    }

    @Override
    public void setup() {
        backend = new DiceGame(1000); // Start with $1000 balance
        diceImages = new HashMap<>();

        // Load provided PNG assets
        cupImage = loadImage("cup.png");
        diceImages.put(1, loadImage("oneSide.png"));
        diceImages.put(2, loadImage("twoSide.png"));
        diceImages.put(3, loadImage("threeSide.png"));
        diceImages.put(4, loadImage("fourSide.png"));
        diceImages.put(5, loadImage("fiveSide.png"));
        diceImages.put(6, loadImage("sixSide.png"));

        // Initialize cup position to the exact center of the screen
        cupX = width / 2f;
        cupY = height / 2f;
        targetCupX = cupX;
        targetCupY = cupY;
    }

    @Override
    public void draw() {
        background(20, 110, 50); // Classic felt green

        // --- Smooth Cup Move Animation ---
        cupX = lerp(cupX, targetCupX, 0.1f);
        cupY = lerp(cupY, targetCupY, 0.1f);

        // --- Persistent Top Bar Information ---
        textSize(24);
        fill(255);
        text("Bankroll Balance: $" + backend.getBalance(), 50, 40);
        text("Target Number to Beat: " + backend.getTargetValue(), 50, 80);

        // --- Phase 1: Typing Wager Amount ---
        if (enteringBet && backend.getBalance() > 0) {
            fill(255, 255, 150);
            textSize(24);
            text("Type your bet amount: $" + bettingInput + "_", 50, 140);
            
            textSize(14);
            fill(200);
            text("Use digits [0-9], BACKSPACE to edit, ENTER to lock bet.", 50, 180);
            text("Press 'R' at any time to reset profile.", 50, 200);
            
            // Bring cup back to center during betting phase
            targetCupX = width / 2f;
            targetCupY = height / 2f;
        } 
        // --- Phase 2: Choosing High or Low Guess ---
        else if (!enteringBet && !backend.isRoundOver()) {
            fill(255, 255, 150);
            textSize(22);
            text("Will the dice roll HIGHER or LOWER than " + backend.getTargetValue() + "?", 50, 140);
            
            textSize(18);
            fill(255);
            text("Press 'H' for HIGHER  |  Press 'L' for LOWER", 50, 180);
            
            fill(255, 215, 0); // Gold
            text("Current Bet Locked: $" + backend.getCurrentBet(), 50, 220);

            // Shove cup out of the center to reveal underneath slots
            targetCupX = width - 150;
            targetCupY = 150;
        }
        // --- Phase 3: Display Results ---
        else if (backend.isRoundOver() && backend.getBalance() > 0) {
            // Draw Outcome Text in center layout
            textSize(28);
            String msg = backend.getGameMessage();
            if (msg.contains("Won")) fill(50, 255, 50);      // Green
            else if (msg.contains("Returned")) fill(200);   // Gray
            else fill(255, 50, 50);                         // Red
            
            text(msg, 50, 140);
            
            textSize(16);
            fill(255);
            text("Press ENTER to transition your target value and bet again!", 50, 180);
        }
        // --- Phase 4: Bankruptcy ---
        else if (backend.getBalance() <= 0) {
            fill(255, 50, 50);
            textSize(32);
            text("BANKRUPT!", 50, 140);
            textSize(18);
            fill(255);
            text("Press 'R' to wipe profile and claim a fresh $1000 balance.", 50, 180);
        }

        // --- DRAW DICE SLOTS ---
        // Dice slots are drawn in the center, visible only when cup slides aside
        if (!enteringBet) {
            drawDiceVisuals(width / 2 - 110, height / 2 - 50);
        }

        // --- DRAW THE CUP ASSET ---
        if (cupImage != null) {
            imageMode(CENTER);
            image(cupImage, cupX, cupY, 180, 180);
            imageMode(CORNER); // Reset default positioning mode
        } else {
            // Backup square visual asset if cup file is missing
            fill(30, 30, 30, 220);
            rectMode(CENTER);
            rect(cupX, cupY, 140, 140, 15);
            rectMode(CORNER);
        }
    }

    /**
     * Renders both dice images Side-by-Side and prints the combined total score underneath
     */
    private void drawDiceVisuals(float startX, float startY) {
        int d1 = backend.getDie1();
        int d2 = backend.getDie2();

        PImage img1 = diceImages.get(d1);
        PImage img2 = diceImages.get(d2);

        // Render Die 1
        if (img1 != null) {
            image(img1, startX, startY, 100, 100);
        } else {
            fill(255); rect(startX, startY, 100, 100, 10);
            fill(0); textSize(20); text(d1, startX + 40, startY + 55);
        }

        // Render Die 2
        if (img2 != null) {
            image(img2, startX + 120, startY, 100, 100);
        } else {
            fill(255); rect(startX + 120, startY, 100, 100, 10);
            fill(0); textSize(20); text(d2, startX + 160, startY + 55);
        }

        // Total Dice value text overlay below images
        fill(255);
        textSize(22);
        textAlign(CENTER);
        text("Dice Total: " + (d1 + d2), startX + 110, startY + 140);
        textAlign(LEFT);
    }

    @Override
    public void keyPressed() {
        // Master Reset Catch Rule
        if (key == 'r' || key == 'R') {
            backend = new DiceGame(1000);
            bettingInput = "";
            enteringBet = true;
            return;
        }

        // --- Key handler: Placing Wager Mode ---
        if (enteringBet) {
            if (key >= '0' && key <= '9') {
                bettingInput += key;
            } 
            else if (key == BACKSPACE && bettingInput.length() > 0) {
                bettingInput = bettingInput.substring(0, bettingInput.length() - 1);
            } 
            else if (key == ENTER || key == RETURN) {
                if (bettingInput.length() > 0) {
                    int betAmount = Integer.parseInt(bettingInput);
                    // Input Validation Check
                    if (backend.lockBet(betAmount)) {
                        enteringBet = false; 
                    } else {
                        bettingInput = ""; // Clear out input fields on error
                    }
                }
            }
        } 
        // --- Key handler: Choosing Direction ---
        else if (!backend.isRoundOver()) {
            if (key == 'h' || key == 'H') {
                backend.playRound(true); // Guess High
            } 
            else if (key == 'l' || key == 'L') {
                backend.playRound(false); // Guess Low
            }
        } 
        // --- Key handler: Processing Results Phase ---
        else {
            if (key == ENTER || key == RETURN) {
                backend.prepareNextRound();
                bettingInput = "";
                enteringBet = true;
            }
        }
    }
}
2. The Logic Architecture: DiceGame.java
This class acts as your isolated calculations core module. It calculates rolls, verifies that bets do not exceed balances, handles the changing targets from round to round, and processes payout rules.

Java
import java.util.Random;

public class DiceGame {

    private int balance;
    private int currentBet;
    private int targetValue;
    
    private int die1;
    private int die2;
    
    private String gameMessage;
    private boolean isRoundOver;
    private Random rand;

    public DiceGame(int startingBalance) {
        this.balance = startingBalance;
        this.rand = new Random();
        this.isRoundOver = true;
        this.gameMessage = "Enter your bet amount to start!";
        
        // Setup initial dealer seed value between [2-12]
        this.targetValue = rand.nextInt(11) + 2;
        
        // Default placeholders for images
        this.die1 = 1;
        this.die2 = 1;
    }

    /**
     * Validates and locks in player bet amount.
     * Checks if positive and lower than or equal to balance.
     */
    public boolean lockBet(int bet) {
        if (bet <= 0 || bet > balance) {
            gameMessage = "Invalid Bet! Check your bank balance.";
            return false;
        }
        this.currentBet = bet;
        this.isRoundOver = false;
        return true;
    }

    /**
     * Executes the dice logic calculation 
     * @param guessHigh True if player guessed higher, false if low
     */
    public void playRound(boolean guessHigh) {
        // Roll two separate dice items
        this.die1 = rand.nextInt(6) + 1;
        this.die2 = rand.nextInt(6) + 1;
        int totalRoll = die1 + die2;

        // Rule Case A: Push condition (Equal values)
        if (totalRoll == targetValue) {
            gameMessage = "Push! Dice matched target value. Bet Returned.";
            isRoundOver = true;
        }
        // Rule Case B: Player Guess Win Condition (Double Bet Payout)
        else if ((guessHigh && totalRoll > targetValue) || (!guessHigh && totalRoll < targetValue)) {
            int netWin = currentBet * 2;
            balance += netWin;
            gameMessage = "You Won! Payout added: +$" + netWin;
            isRoundOver = true;
        }
        // Rule Case C: Failure State (Lose Bet)
        else {
            balance -= currentBet;
            gameMessage = "You Lost! Amount Deducted: -$" + currentBet;
            isRoundOver = true;
        }
    }

    /**
     * Shifts the current dice roll total to be the next target benchmark
     */
    public void prepareNextRound() {
        if (balance > 0) {
            this.targetValue = die1 + die2;
        }
    }

    // --- State Getters ---
    public int getBalance() { return balance; }
    public int getCurrentBet() { return currentBet; }
    public int getTargetValue() { return targetValue; }
    public int getDie1() { return die1; }
    public int getDie2() { return die2; }
    public String getGameMessage() { return gameMessage; }
    public boolean isRoundOver() { return isRoundOver; }
}

## 10
make it so that the high or low game exists in parallel to the blackjack game with only the user's balance transferring if the user plays between both games 

To allow both games to exist in parallel while sharing a synchronized, single source of truth for the player's bankroll balance, we can refactor the architecture into three components:

BankAccount.java: A shared object instance that stores the unified balance. Both backend engines will mutate this object directly so any gains or losses transfer instantly.

DiceGame.java: Modifies the dice mechanics to accept the reference of our shared wallet.

Game.java: Acts as a state controller, rendering user interface elements and switching back-and-forth between the games using the keyboard ('1' for Blackjack, '2' for High-Low Dice).

Here is the implementation across the updated and new classes:

1. The Shared State Wrapper: BankAccount.java
Create this new class to hold the balance reference shared across both games.

Java
public class BankAccount {
    private int balance;

    public BankAccount(int startingBalance) {
        this.balance = startingBalance;
    }

    public int getBalance() {
        return this.balance;
    }

    public void deposit(int amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public void withdraw(int amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
        }
    }
}
2. The Refactored Backend Logic: DiceGame.java
Update your dice module to bind to the mutual BankAccount engine state instead of carrying an isolated balance variable.

Java
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
            gameMessage = "Push! Dice matched target value. Bet Returned.";
            isRoundOver = true;
        }
        // Rule Case B: Player Guess Win Condition (Double Bet Payout)
        else if ((guessHigh && totalRoll > targetValue) || (!guessHigh && totalRoll < targetValue)) {
            int netWin = currentBet * 2;
            account.deposit(netWin);
            gameMessage = "You Won! Payout added: +$" + netWin;
            isRoundOver = true;
        }
        // Rule Case C: Failure State (Lose Bet)
        else {
            account.withdraw(currentBet);
            gameMessage = "You Lost! Amount Deducted: -$" + currentBet;
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
(Note: You will want to apply a similar adjustment to your Blackjack.java class constructor to take a BankAccount account parameter and replace internal balance modifications with account.deposit() and account.withdraw() calls).

3. Integrated Controller: Game.java
This script handles rendering updates dynamically depending on the selected mode. It preserves the graphical animations and user-input fields safely for both game instances.

Java
import processing.core.PApplet;
import processing.core.PImage;
import java.util.HashMap;

public class Game extends PApplet {

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;

    // --- Global Game Mode Engine State ---
    // 1 = Blackjack, 2 = High-Low Dice Game
    int gameMode = 1; 

    // --- Shared Bank System ---
    BankAccount sharedAccount;

    // --- Backend References ---
    Blackjack blackjackBackend;
    DiceGame diceBackend;

    // --- Graphical Media Assets ---
    PImage cupImage;
    HashMap<Integer, PImage> diceImages;
    // (Incorporate your existing blackjack card assets field mappings here)

    // --- Shared Input Variables ---
    String bettingInput = "";     
    boolean enteringBet = true;   
    
    // --- Dice Animation Fields ---
    float cupX, cupY;
    float targetCupX, targetCupY;

    @Override
    public void settings() {
        size(APP_WIDTH, APP_HEIGHT, JAVA2D);
    }

    @Override
    public void setup() {
        // 1. Initialize centralized tracking wallet
        sharedAccount = new BankAccount(1000); 

        // 2. Instantiate both games passing the shared wallet instance
        blackjackBackend = new Blackjack(sharedAccount); 
        diceBackend = new DiceGame(sharedAccount);

        // 3. Load Assets
        diceImages = new HashMap<>();
        cupImage = loadImage("cup.png");
        diceImages.put(1, loadImage("oneSide.png"));
        diceImages.put(2, loadImage("twoSide.png"));
        diceImages.put(3, loadImage("threeSide.png"));
        diceImages.put(4, loadImage("fourSide.png"));
        diceImages.put(5, loadImage("fiveSide.png"));
        diceImages.put(6, loadImage("sixSide.png"));

        // Setup asset variables
        cupX = width / 2f;
        cupY = height / 2f;
        targetCupX = cupX;
        targetCupY = cupY;
    }

    @Override
    public void draw() {
        // Render common table environment
        background(20, 110, 50); 

        // Draw Shared HUD Controls Top Bar
        drawGlobalHeader();

        if (sharedAccount.getBalance() <= 0) {
            drawBankruptcyScreen();
            return;
        }

        // --- Render Based on Selected Mode ---
        if (gameMode == 1) {
            drawBlackjackScreen();
        } else if (gameMode == 2) {
            drawDiceScreen();
        }
    }

    private void drawGlobalHeader() {
        fill(0, 50);
        rect(0, 0, width, 60);
        fill(255);
        textSize(16);
        text("SHARED BANK BALANCE: $" + sharedAccount.getBalance(), 20, 35);
        
        // Mode indicator configuration
        textAlign(RIGHT);
        fill(gameMode == 1 ? color(255, 215, 0) : 200);
        text("[Press 1] Blackjack Table", width - 240, 35);
        fill(gameMode == 2 ? color(255, 215, 0) : 200);
        text("[Press 2] High-Low Dice", width - 40, 35);
        textAlign(LEFT);
    }

    private void drawBlackjackScreen() {
        fill(255);
        textSize(22);
        text("Blackjack Table Room", 50, 100);
        
        // Render implementation hook using layout rules from your original design
        textSize(16);
        if (enteringBet) {
            text("Type your Blackjack bet: $" + bettingInput + "_", 50, 150);
        } else {
            text("Game Message: " + blackjackBackend.getGameMessage(), 50, 150);
            // Include your standard card layout rendering methods here
        }
    }

    private void drawDiceScreen() {
        // --- Smooth Cup Move Animation Calculations ---
        cupX = lerp(cupX, targetCupX, 0.1f);
        cupY = lerp(cupY, targetCupY, 0.1f);

        textSize(22);
        fill(255);
        text("Target Number to Beat: " + diceBackend.getTargetValue(), 50, 100);

        if (enteringBet) {
            fill(255, 255, 150);
            text("Type your High-Low bet: $" + bettingInput + "_", 50, 150);
            targetCupX = width / 2f;
            targetCupY = height / 2f;
        } 
        else if (!enteringBet && !diceBackend.isRoundOver()) {
            fill(255, 255, 150);
            text("Will dice roll HIGHER or LOWER than " + diceBackend.getTargetValue() + "?", 50, 150);
            textSize(16);
            fill(255);
            text("Press 'H' for HIGHER  |  Press 'L' for LOWER", 50, 190);
            text("Current Locked Wager: $" + diceBackend.getCurrentBet(), 50, 220);
            
            targetCupX = width - 150;
            targetCupY = 150;
        } 
        else if (diceBackend.isRoundOver()) {
            String msg = diceBackend.getGameMessage();
            if (msg.contains("Won")) fill(50, 255, 50);
            else if (msg.contains("Returned")) fill(200);
            else fill(255, 50, 50);
            
            text(msg, 50, 150);
            textSize(16);
            fill(255);
            text("Press ENTER to transition target and clear room.", 50, 190);
        }

        // Draw active dice slots under the cup
        if (!enteringBet) {
            drawDiceVisuals(width / 2 - 110, height / 2 - 50);
        }

        // Draw Cup
        if (cupImage != null) {
            imageMode(CENTER);
            image(cupImage, cupX, cupY, 180, 180);
            imageMode(CORNER);
        }
    }

    private void drawDiceVisuals(float startX, float startY) {
        int d1 = diceBackend.getDie1();
        int d2 = diceBackend.getDie2();
        PImage img1 = diceImages.get(d1);
        PImage img2 = diceImages.get(d2);

        if (img1 != null) image(img1, startX, startY, 100, 100);
        if (img2 != null) image(img2, startX + 120, startY, 100, 100);

        fill(255);
        textSize(22);
        textAlign(CENTER);
        text("Dice Total: " + (d1 + d2), startX + 110, startY + 140);
        textAlign(LEFT);
    }

    private void drawBankruptcyScreen() {
        fill(255, 50, 50);
        textSize(32);
        text("BANKRUPT!", width / 2 - 100, height / 2 - 20);
        textSize(18);
        fill(255);
        text("Press 'R' to clear tables and acquire fresh $1000 credit profile.", width / 2 - 240, height / 2 + 30);
    }

    @Override
    public void keyPressed() {
        // Universal Reset Profile Key
        if (key == 'r' || key == 'R') {
            sharedAccount = new BankAccount(1000);
            blackjackBackend = new Blackjack(sharedAccount);
            diceBackend = new DiceGame(sharedAccount);
            bettingInput = "";
            enteringBet = true;
            return;
        }

        // --- Room Intercept Switches ---
        if (key == '1') {
            gameMode = 1;
            bettingInput = "";
            enteringBet = true;
            return;
        }
        if (key == '2') {
            gameMode = 2;
            bettingInput = "";
            enteringBet = true;
            return;
        }

        // --- Context Inputs Routing ---
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
                    if (blackjackBackend.startRound(bet)) { // Modified backend interaction
                        enteringBet = false;
                    } else {
                        bettingInput = "";
                    }
                }
            }
        } else {
            // Include your standard blackjack keyboard bindings (e.g., Hit/Stand logic) here
        }
    }

    private void handleDiceInput() {
        if (enteringBet) {
            if (key >= '0' && key <= '9') bettingInput += key;
            else if (key == BACKSPACE && bettingInput.length() > 0) bettingInput = bettingInput.substring(0, bettingInput.length() - 1);
            else if (key == ENTER || key == RETURN) {
                if (bettingInput.length() > 0) {
                    int bet = Integer.parseInt(bettingInput);
                    if (diceBackend.lockBet(bet)) {
                        enteringBet = false;
                    } else {
                        bettingInput = "";
                    }
                }
            }
        } 
        else if (!diceBackend.isRoundOver()) {
            if (key == 'h' || key == 'H') diceBackend.playRound(true);
            else if (key == 'l' || key == 'L') diceBackend.playRound(false);
        } 
        else {
            if (key == ENTER || key == RETURN) {
                diceBackend.prepareNextRound();
                bettingInput = "";
                enteringBet = true;
            }
        }
    }
}

## 11
make the needed changes to the blackjack class

To successfully integrate your Blackjack game with the new parallel model, we need to adapt it to use the shared BankAccount reference instead of managing its own isolated balance field or a separate Player banking account object.

Here is the fully modified Blackjack.java class, updated to share the universal BankAccount instance:

Java
public class Blackjack {

    // --- Fields to keep track of the current game state ---
    private BankAccount account; // Reference to the shared bank account
    private Hand dealerHand;
    private Deck deck;
    private int currentBet;
    private String gameMessage;
    private boolean isRoundOver;
    private Hand playerHand; // Tracks cards for the player's active hand

    /**
     * Constructor accepts the central shared BankAccount object instance
     */
    public Blackjack(BankAccount account) {
        this.account = account;
        this.playerHand = new Hand();
        this.dealerHand = new Hand();
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
        if (bet > account.getBalance() || bet <= 0) {
            gameMessage = "Invalid bet amount.";
            return false;
        }

        this.currentBet = bet;
        this.deck = new Deck();
        
        // Reset hands for a fresh round
        this.playerHand = new Hand();
        this.dealerHand = new Hand();
        this.isRoundOver = false;

        // Deal initial 2 cards to each
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        gameMessage = "Hit or Stand?";
        return true;
    }

    /**
     * Player requests another card.
     */
    public void hit() {
        if (isRoundOver) return;

        playerHand.addCard(deck.drawCard());

        if (playerHand.isBust()) {
            gameMessage = "You busted! Dealer wins.";
            account.withdraw(currentBet); // Deduct bet immediately on bust
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
     * Contains the outcome comparison logic connected to the shared BankAccount
     */
    private void evaluateWinners() {
        isRoundOver = true;

        int playerTotal = playerHand.getValue();
        int dealerTotal = dealerHand.getValue();
        boolean playerNatural = playerHand.isNatural21();
        boolean dealerNatural = dealerHand.isNatural21();
        boolean playerMade21 = (playerTotal == 21 && !playerNatural);
        boolean dealerMade21 = (dealerTotal == 21 && !dealerNatural);

        if (playerHand.isBust()) {
            gameMessage = "You busted! Dealer wins.";
            account.withdraw(currentBet);
        } 
        else if (dealerHand.isBust()) {
            gameMessage = "Dealer busted! You win!";
            account.deposit(currentBet * 2); // Net gain of double the bet payout
        } 
        else if (playerNatural && !dealerNatural) {
            gameMessage = "Natural 21! You win!";
            account.deposit(currentBet * 2);
        } 
        else if (dealerNatural && !playerNatural) {
            gameMessage = "Dealer natural 21 wins.";
            account.withdraw(currentBet);
        } 
        else if (playerNatural && dealerNatural) {
            gameMessage = "Tie! Both got Natural 21."; // Push: money returns, no change
        } 
        else if (playerMade21 && dealerMade21) {
            gameMessage = "Tie! Both made 21."; // Push: money returns, no change
        } 
        else if (playerTotal > dealerTotal) {
            gameMessage = "You win!";
            account.deposit(currentBet * 2);
        } 
        else if (playerTotal < dealerTotal) {
            gameMessage = "Dealer wins.";
            account.withdraw(currentBet);
        } 
        else {
            gameMessage = "Push (Tie).";
        }
    }

    // --- Getters to allow Game.java to read the current state for drawing ---

    public Hand getPlayerHand() { return playerHand; }
    public Hand getDealerHand() { return dealerHand; }
    public String getGameMessage() { return gameMessage; }
    public boolean isRoundOver() { return isRoundOver; }
    public int getBalance() { return account.getBalance(); }
    public int getCurrentBet() { return currentBet; }
}
What changed?
Removed Player Class Dependency: The internal state tracking inside your custom Player class wrapper has been cleaned up. The monetary portion now routes through BankAccount, and the active card list is now handled seamlessly inside the local playerHand instance variable.

Synchronized Money Influx/Deflux: player.winBet(...) and player.loseBet(...) expressions have been updated to direct account.deposit(...) and account.withdraw(...) mutations on the unified bank balance. This ensures that any chips won or lost at the Blackjack table are immediately updated when transitioning to High-Low Dice.

## 12 
update the player class

Here is the updated Player.java class.

To maintain clean object-oriented design and ensure compatibility with the updated Blackjack logic, we remove the isolated balance field and its direct banking methods from this class. Instead, the Player is initialized with a reference to the shared BankAccount object so that card actions can remain separate from the centralized monetary tracking.

Java
public class Player {

    private Hand hand;
    private BankAccount account; // Reference to the centralized bank account

    /**
     * Constructor links the player to their hand and the shared financial account
     */
    public Player(BankAccount account) {
        this.hand = new Hand();
        this.account = account;
    }

    /**
     * Retrieves the current hand of cards for rendering or logic evaluation
     */
    public Hand getHand() {
        return this.hand;
    }

    /**
     * Helper method to seamlessly fetch the synchronized balance from the shared account
     */
    public int getBalance() {
        return this.account.getBalance();
    }

    /**
     * Resets the player's cards for a brand-new round of dealing
     */
    public void resetHand() {
        this.hand = new Hand();
    }
}
Key Refactoring Details
Single Source of Truth: Removed the local balance primitive variable, preventing bugs where one game updates its balance while the other remains unchanged.

Delegated Banking: The responsibility for adjusting capital has been moved out of this class. The Blackjack backend now handles direct calls via account.deposit() and account.withdraw(), mirroring the behavior of the DiceGame engine.

## 13 
make it two seperate screens so that the blackjack game is like how it was before  the chaneg

To keep both games running smoothly side-by-side with a synchronized balance, we can fully integrate them into your Game.java file using a simple gameMode state variable.

By utilizing your original visual layout for Blackjack exactly as it was, and switching over to the complete High-Low Dice graphics when requested, you get the best of both worlds.

Here is the updated, unified Game.java class that matches your original code structure while adding the parallel dice mode.

Java
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
    text("[Press 1] Blackjack Table", width - 220, 28);
    fill(gameMode == 2 ? color(255, 215, 0) : 180);
    text("[Press 2] High-Low Dice", width - 30, 28);
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
        text("Type your blackjack bet: $" + bettingInput + "_", 50, 180);
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

  /**
   * Screen 2: High or Low Dice Layout
   */
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

    // Reveal hidden dice underlying layout slot area
    if (!enteringBet) {
        drawDiceVisuals(width / 2 - 110, height / 2 - 40);
    }

    // Draw Shaking Cup Overlay
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
    text("Press 'R' to completely wipe profiles and claim a fresh $1000 credit line.", 50, 180);
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

    // Menu Room Navigation Keys
    if (key == '1' && (blackjack.isRoundOver() && diceGame.isRoundOver())) {
        gameMode = 1;
        bettingInput = "";
        enteringBet = true;
        return;
    }
    if (key == '2' && (blackjack.isRoundOver() && diceGame.isRoundOver())) {
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

## 14
change it so that to switch between high or low and blackjack the user clicks b (for blackjack) and d (for high or low)

Here are the updated Game.java, Player.java, and Blackjack.java classes configured so that the two screens function exactly as they did originally, with the shared wallet switching smoothly when pressing B (for Blackjack) and D (for High or Low).

1. The Controller Screen: Game.java
This script updates the keyboard event handlers (keyPressed) to listen for 'b' and 'd' instead of numbers, while drawing the original Blackjack layout exactly how it was configured before.

Java
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
        text("Type your blackjack bet: $" + bettingInput + "_", 50, 180);
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

  /**
   * Screen 2: High or Low Dice Layout
   */
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
    text("Press 'R' to completely wipe profiles and claim a fresh $1000 credit line.", 50, 180);
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
2. The Original Core State Handler: Blackjack.java
This ensures your original layout elements render cards through the original Player object configuration.

Java
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
3. The Re-linked Account Model: Player.java
This maps your original layout code cleanly back into place, forwarding the accounting actions directly to the synchronized BankAccount instance wrapper.

Java
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
        account.deposit(bet * 2); // Net addition matching original payout mechanics
    }

    public void loseBet(int bet) {
        account.withdraw(bet);
    }

    public void resetHand() {
        hand = new Hand();
    }
}

## 15
fix the way that the images arent showing up for the dice game

The reason the dice images were not displaying correctly is because they were being loaded without specifying the images/ directory prefix in their path strings, while the blackjack images correctly included it (e.g., "images/cardBack.png"). Since all game asset assets are placed in the local asset directory structure together, updating the diceImages mapping to load from the correct "images/" folder path fixes the issue.

Additionally, when the images are successfully loaded from their path, your sketch will display the matching pair of visual assets instead of the text boxes.

Here is the fully fixed and consolidated Game.java class:

Java
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
    
    // 4. Load High-Low Dice Assets (FIXED PATH DIRECTIONS HERE)
    diceImages = new HashMap<Integer, PImage>();
    cupImage = loadImage("images/cup.png");
    diceImages.put(1, loadImage("images/oneSide.png"));
    diceImages.put(2, loadImage("images/twoSide.png"));
    diceImages.put(3, loadImage("images/threeSide.png"));
    diceImages.put(4, loadImage("images/fourSide.png"));
    diceImages.put(5, loadImage("images/fiveSide.png"));
    diceImages.put(6, loadImage("images/sixSide.png"));

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
        text("Type your blackjack bet: $" + bettingInput + "_", 50, 180);
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

  /**
   * Screen 2: High or Low Dice Layout
   */
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


