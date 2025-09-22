package deck;

import java.util.ArrayList;
import cards.Card;
import cards.StandardCard;
import cards.UnoCard;

public class Deck {
	private ArrayList<Card> cards = new ArrayList<Card>();
	private ArrayList<Card> usedCards = new ArrayList<Card>();

	public Deck(int deckSize) {
		for (int i = 0; i < deckSize; i++) {
				cards.add(new StandardCard(i + 1));
			}
	}
	public Deck(int deckSize, boolean isUnoDeck) {
		if (isUnoDeck) {
			for (int i = 0; i < deckSize; i++) {
				cards.add(new UnoCard(i + 1));
			}
		} else {
			for (int i = 0; i < deckSize; i++) {
				cards.add(new StandardCard(i + 1));
			}
		}
	}

	public void shuffleDeck() {
		for (int i = cards.size() - 1; i > 0; i--) {
			int j = (int) (Math.random() * (i + 1));
			Card temp = cards.get(i);
			cards.set(i, cards.get(j));
			cards.set(j, temp);
		}
	}

	public void restack() {
		for (int i = usedCards.size() - 1; i >= 0; i--) {
			Card temp = usedCards.get(i);
			cards.add(temp);
			usedCards.remove(i);
		}
		shuffleDeck();
	}

	public Card dealCard(int index) {
		return cards.remove(index);
	}

	public Card getCard(int index) {
		return cards.get(index);
	}

	public void addUsedCards(Card card) {
		usedCards.add(card);
	}

	public Card[] getCards() {
		Card[] cardsArray = new Card[cards.size()];
		return cards.toArray(cardsArray);
	}

	public Card[] getUsedCards() {
		Card[] cardsArray = new Card[usedCards.size()];
		return usedCards.toArray(cardsArray);
	}

	public String toString() {
		String result = "Deck: \n";
		for (int i = 0; i < cards.size(); i++) {
			result += cards.get(i).toString() + " ";
			if ((i + 1) % 13 == 0) {
				result += "\n";
			}
		}
		if (usedCards.size() > 0) {
			result += "\n\nUsed Cards: \n";
			for (int i = 0; i < usedCards.size(); i++) {
				result += usedCards.get(i).toString() + " ";
				if ((i + 1) % 13 == 0) {
					result += "\n";
				}
			}
		}
		return result;
	}

}
