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



## (4)





