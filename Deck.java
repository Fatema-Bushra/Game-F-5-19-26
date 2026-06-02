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