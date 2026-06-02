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