import cards.Card;
import cards.UnoCard;
import player.Player;

public class Assignment2_1 {

	public static void main(String[] args) {
		Card[] deck = new Card[52];
		UnoCard[] unoDeck = new UnoCard[108];

		for (int i = 0; i < deck.length; i++) {
			deck[i] = new Card(i + 1);
		}
		for (int i = 0; i < unoDeck.length; i++) {
			unoDeck[i] = new UnoCard(i + 1);
		}

		System.out.println("Standard Deck: ");
		printDeck(deck);
		System.out.println("\nUno Deck: ");
		printDeck(unoDeck);
		System.out.println("\n\nShuffled Standard Deck: ");
		shuffle(deck);
		printDeck(deck);
		System.out.println("\nShuffled Uno Deck: ");
		shuffle(unoDeck);
		printDeck(unoDeck);
		System.out.println("\n");
		Player player1 = new Player("Fast Freddy", "00001", 1000);
		Player player2 = new Player("OneEyedJack", "00002", 1500);
		deal(unoDeck, player1);
		deal(deck, player2);
		printPlayerHand(player1);
		printPlayerHand(player2);
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

	public static void shuffle(Card[] deck) {
		for (int i = deck.length - 1; i > 0; i--) {
			int j = (int) (Math.random() * (i + 1));
			Card temp = deck[i];
			deck[i] = deck[j];
			deck[j] = temp;
		}
	}

	public static void deal(UnoCard[] unoDeck, Player player) {
		for (int i = 0; i < 7; i++) {
			player.getHand().addCard(unoDeck[i]);
		}
	}

	public static void deal(Card[] deck, Player player) {
		for (int i = 0; i < 5; i++) {
			player.getHand().addCard(deck[i]);
		}
	}

	public static void printPlayerHand(Player player) {
		System.out.println(player.getName() + "'s Hand: " + player.getHand());
	}

}
