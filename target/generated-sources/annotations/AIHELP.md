# Making the BlackJack game

* These be the assigned test-prompts

#### Q. what classes would i need to make a blackjack game in java?

* Would need a card class that represents a card with fileds such as String suit and int rank as well as methods like getting the value of a card

* Woud need a deck class that manages a collection of card objects with fields such as ArrayList<card> to hold the cards and methods such as shuffle() that randomizes the order of the cards and draw() that removes and returns the top car from the list

* Would need a hand class with fields such as ArrayList<Card> and methods like addCard(Card c) that would add a card to the hand and calculateValue() that would add up the cards and handle the case of the Ace being 1 or 11 to avoid busting

* Could make a participant class for the Player and dealer to inherit from where a player could have betting logic while the dealer has house rules logic

* Would need a game class that would comparing the scores, instantating the deck and players, and managing the game loop when players are hitting, standing, and checking game values

#### Q. how would the logic for house rules for the dealer class look like?

* "Soft 17" vs. "Hard 17"
* Hard 17: Dealer stands (e.g., 10 + 7).
* Soft 17: Dealer hits (e.g., Ace + 6).

* Adding two cards to the dealerHand at the start.
*Creating a displayPartialHand() method that only prints the first card in the list.
*Revealing the full hand only when playDealerTurn() begins.

#### Q. how would the betting logic be implemented where a player's bet is doubled when they win and the bet amount is lost when the player loses?

* Your player needs to track two specific numbers: their total "bankroll" (how much money they have left) and their "current bet" for the round
* Make a recieve method that accpets a double as a multiplier and a method that takes the money away from the player's accoutn
* Make an area to visualize the bets
* Consistently check the user's balance; if the user is bankrupt the loop ends