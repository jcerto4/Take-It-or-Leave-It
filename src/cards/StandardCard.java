package cards;

public class StandardCard extends Card {
	private String suit;
	private int suitIndex;
	private int cardRank;

	public StandardCard(int cardNumber) {
		super(cardNumber);
	}

	@Override
	protected void createCard() {
		final String[] suits = { "s", "h", "d", "c" };
		final String[] colors = { "b", "r", "r", "b" };
		final String[] faces = { "", "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };
		suitIndex = (cardNumber - 1) / 13;
		suit = suits[suitIndex];
		color = colors[suitIndex];
		cardRank = (cardNumber - 1) % 13 + 1;
		face = faces[cardRank];
		cardImage = cardNumber + ".png";
	}

	public String getSuit() {
		return suit;
	}

	public int getSuitIndex() {
		return suitIndex;
	}

	public int getCardRank() {
		return cardRank;
	}

	public String toString() {
		return getFace() + getSuit();
	}
}
