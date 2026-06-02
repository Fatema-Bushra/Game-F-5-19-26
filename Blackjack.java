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
