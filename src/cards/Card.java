package cards;

public abstract class Card {
	protected int cardNumber;
	protected String color;
	protected String face;
	protected String cardImage;
	private static int cardsCreated;

	// constructor
	public Card(int cardNumber) {
		this.cardNumber = cardNumber;
		cardsCreated++;
		createCard();
	}

	protected abstract void createCard();

	// accessors
	public int getCardNumber() {
		return cardNumber;
	}

	public String getColor() {
		return color;
	}

	public String getFace() {
		return face;
	}

	public String getCardImage() {
		return cardImage;
	}

	public static int getCardsCreated() {
		return cardsCreated;
	}
}
