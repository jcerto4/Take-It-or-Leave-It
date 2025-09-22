import cards.Card;
import player.Player;

public class JoeCard {

	public static void main(String[] args) {
		Card[] deck = new Card[52];
		for (int i = 0; i < deck.length; i++) {
			deck[i] = new Card(i + 1); // Start at 1 rather than 0
		}

		System.out.println("Deck Before Shuffle: ");
		printDeck(deck);

		shuffle(deck);
		System.out.println("Shuffled Deck: ");
		printDeck(deck);

		Player one = new Player("FastFreddy", "00007", 1000);
		Player two = new Player("OneEyedJack", "00001", 2000);
		Card[] playerOneHand = new Card[5];
		Card[] playerTwoHand = new Card[5];

		deal(deck, playerOneHand, playerTwoHand);

		printPlayerDeck(one, playerOneHand);
		System.out.println();
		printPlayerDeck(two, playerTwoHand);
	}

	public static void printDeck(Card[] deck) {
		for (int i = 0; i < deck.length; i++) {
			System.out.print(deck[i].toString() + " ");

			// line break every 13 cards
			if ((i + 1) % 13 == 0) {
				System.out.println();
			}
		}
	}

	public static void printPlayerDeck(Player data, Card[] playerHand) {
		System.out.print(data.getName() + "'s Hand: ");
		for (int i = 0; i < playerHand.length; i++) {
			System.out.print(playerHand[i] + " ");
		}
	}

	public static void shuffle(Card[] deck) {
		for (int i = deck.length - 1; i > 0; i--) {
			int j = (int) (Math.random() * (i + 1));
			Card temp = deck[i];
			deck[i] = deck[j];
			deck[j] = temp;
		}
	}

	public static void deal(Card[] deck, Card[] playerOneHand, Card[] playerTwoHand) {
		for (int i = 0; i < playerOneHand.length; i++) {
			playerOneHand[i] = deck[i * 2];
			playerTwoHand[i] = deck[i * 2 + 1];
		}
	}

}