import cards.StandardCard;
import deck.Deck;
import player.Dealer;
import player.Player;

public class Assignment2_3 {

	public static void main(String[] args) {
		Deck deck = new Deck(52, false);
		Dealer dealer = new Dealer(deck);
		printDeck(dealer, "Before Deal ");
		Player player1 = new Player("00001", "FastFreddy", 2000);

		for (int i = 0; i < 5; i++) {
			dealer.dealCard(player1);
			dealer.dealCard(dealer);
		}
		printDeck(dealer, "\nAfter Deal ");
		evaluateHand(player1);
		evaluateHand(dealer);
		System.out.println("\n");
		printPlayerHand(player1);
		printPlayerHand(dealer);
		compareHands(player1, dealer);
		System.out.println();
		printCardRanks(player1);
		System.out.println();
		printCardRanks(dealer);
		dealer.gatherUsedCards(player1);
		dealer.gatherUsedCards(dealer);
		printDeck(dealer, "\n\nAfter Discard ");
		System.out.println();
		dealer.getDeck().restack();
		printDeck(dealer, "\nAfter Restack ");

		Deck unoDeck = new Deck(108, true);
		Dealer unoDealer = new Dealer(unoDeck);
		printDeck(unoDealer, "\nUno ");
	}

	public static void evaluateHand(Player player) {
		player.getHand().evaluateHand();
	}

	public static void printPlayerHand(Player player) {
		System.out.println(player.getName() + "'s Hand: " + player.getHand() + "\t" + player.getHand().getHandDescr());
	}

	public static void compareHands(Player player1, Player player2) {
		System.out.println();
		int result = player1.getHand().compareHand(player2.getHand());
		if (result == 1) {
			System.out.println(player1.getName() + " wins!");
		} else if (result == -1) {
			System.out.println(player2.getName() + " wins!");
		} else {
			System.out.println("It's a tie!");
		}
	}

	public static void printDeck(Dealer dealer, String title) {
		System.out.print(title + dealer.getDeck().toString());
	}

	public static void printCardRanks(Player player) {
		System.out.print(player.getName() + "'s Card Ranks: ");
		for (int i = 0; i < 5; i++) {
			System.out.print(((StandardCard) player.getHand().getCard(i)).getCardRank() + " ");

		}
	}

}
