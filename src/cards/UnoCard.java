package cards;

public class UnoCard extends Card {
	private boolean reverse;
	private int drawAmount;
	private boolean wild;
	private boolean skip;

	public UnoCard(int cardNumber) {
		super(cardNumber);
		createCard();
	}

	@Override
	protected void createCard() {
		final String[] colors = { "r", "y", "g", "b" };
		final String[] faces = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "SK", "RV", "D2" };

		if (cardNumber < 97) {
			int colorIndex = (cardNumber - 1) / 24;
			int faceIndex = (cardNumber - 1) % 12;
			color = colors[colorIndex];
			face = faces[faceIndex];
			switch (face) {
			case "SK":
				skip = true;
				break;
			case "RV":
				reverse = true;
				break;
			case "D2":
				drawAmount = 2;
				break;
			}
		} else {
			switch (cardNumber) {
			case 97:
				face = "0";
				color = "r";
				break;
			case 98:
				face = "0";
				color = "y";
				break;
			case 99:
				face = "0";
				color = "g";
				break;
			case 100:
				face = "0";
				color = "b";
				break;
			case 101:
			case 102:
			case 103:
			case 104:
				face = "WD";
				color = "w";
				wild = true;
				break;
			case 105:
			case 106:
			case 107:
			case 108:
				face = "D4";
				color = "w";
				wild = true;
				drawAmount = 4;
				break;

			}
		}
		cardImage = cardNumber + ".png";
	}// end createCard

	public boolean isReverse() {
		return reverse;
	}

	public int getDrawAmount() {
		return drawAmount;
	}

	public boolean isWild() {
		return wild;
	}

	public boolean isSkip() {
		return skip;
	}

	public String toString() {
		if (isWild()) {
			return super.getFace();
		} else {
			return super.getFace() + super.getColor();
		}
	}

}
