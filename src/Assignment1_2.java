import cards.Card;
import hand.Hand;
import player.Player;
import helpers.PokerSolver;

public class Assignment1_2 {

	public static void main(String[] args) {
		Card[] deck = new Card[52];

		for (int i = 0; i < deck.length; i++) {
			deck[i] = new Card(i + 1);
		}
		System.out.println("Shuffled Deck: ");
		shuffle(deck);
		printDeck(deck);

		Player player1 = new Player("FastFreddy", "9765467", 2650);
		Player player2 = new Player("OneEyedJack", "2435573", 1400);

		deal(deck, player1, player2);
		evaluateHand(player1);
		evaluateHand(player2);

		System.out.println("\n5 Card Poker: ");
		printPlayerHand(player1);
		printPlayerHand(player2);
		System.out.print("\n5 Card Poker Results: ");
		compareHands(player1, player2);

		evaluateHandDeucesWild(player1);
		evaluateHandDeucesWild(player2);
		System.out.println("\nDeuces Wild:");
		printPlayerHand(player1);
		printPlayerHand(player2);
		System.out.print("\nDeuces Wild Results: ");
		compareHandsDeucesWild(player1, player2);
	}// end main

	public static void printDeck(Card[] deck) {
		for (int i = 0; i < deck.length; i++) {
			System.out.print(deck[i].toString() + " ");
			// line break every 13 cards
			if ((i + 1) % 13 == 0) {
				System.out.println();
			}
		}
	}

	public static void printPlayerHand(Player player) {
		System.out.println(player.getName() + "'s Hand: " + player.getHand() + "\t" + player.getHand().getHandDescr());
	}

	public static void shuffle(Card[] deck) {
		for (int i = deck.length - 1; i > 0; i--) {
			int j = (int) (Math.random() * (i + 1));
			Card temp = deck[i];
			deck[i] = deck[j];
			deck[j] = temp;
		}
	}

	public static void deal(Card[] deck, Player player1, Player player2) {
		for (int i = 0; i < 5; i++) {
			player1.getHand().addCard(deck[i * 2]);
			player2.getHand().addCard(deck[i * 2 + 1]);
		}
	}

	public static void evaluateHand(Player player) {
		player.getHand().evaluateHand();
	}

	public static void evaluateHandDeucesWild(Player player) {
		player.getHand().evaluateHand("DeucesWild");
	}

	public static void compareHands(Player player1, Player player2) {
		int result = player1.getHand().compareHand(player2.getHand());
		if (result == 1) {
			System.out.println(player1.getName() + " wins!");
		} else if (result == -1) {
			System.out.println(player2.getName() + " wins!");
		} else {
			System.out.println("It's a tie!");
		}
	}

	public static void compareHandsDeucesWild(Player player1, Player player2) {
		int result = player1.getHand().compareHand(player2.getHand(), "DeucesWild");
		if (result == 1) {
			System.out.println(player1.getName() + " wins!");
		} else if (result == -1) {
			System.out.println(player2.getName() + " wins!");
		} else {
			System.out.println("It's a tie!");
		}
	}
}
