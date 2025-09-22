package game;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import cards.Card;
import hand.Hand;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import player.Player;

public class PlayerArea extends VBox {
	
	//Who does the player area belong to?
	private Player player;
	
	//How many cards in hand
	private int handSize;

	//An HBox to hold the cards
	private HBox playerCardArea = new HBox(10);

	//A Text box to hold the handDesr
	private Text handResults = new Text(" ");
	
	//An ArrayList of ImageViews for the cards
	private ArrayList<ImageView> cardImages = new ArrayList<ImageView>();
	
	//An array of ImageViews for the Selected Image
	private ImageView[] selected;
	
	//For Deuces Wild - and array of ImageViews for the Wild image.
	private ImageView[] wild;
	
	//A StackPane array to hold the image views
	StackPane[] stackPane;
	
	//For Assignment 3.2
	boolean[] cardSelected;
	int selectedCount;
	int maxSelect;
	
	//For mine only
	boolean deucesWild = false;
	boolean poker = false;
	
	private Media selectSoundMedia;
	private MediaPlayer selectSoundPlayer;
	
	public PlayerArea(Player player, int handSize, String gameType) {
		//Note which player owns this object
		this.player = player;
		this.handSize = handSize;
		
		maxSelect = (gameType == "tioli") ? 1 : 4;
				
		deucesWild = (gameType == "deuceswild") ? true : false;
		poker = (gameType == "poker") ? true : false;
		loadSelectSound();
		buildArea();
	}
	private void buildArea() {
		
		stackPane = new StackPane[handSize];
		selected = new ImageView[handSize];
		
		
		cardSelected = new boolean[handSize];  //Assignment 3.2
		
		if(deucesWild) wild = new ImageView[handSize];
		
		createImageViews();
		styleCardImages();
		createStackPanes();
		
		if(!poker) addSelectedImageToStackPanes();
		
		addCardImageViewsToStackPanes();
		if(deucesWild) addWildImageToStackPanes();
		
		styleHandResults();
		styleCardHolder();
		
		
		this.getChildren().addAll(handResults, playerCardArea);

		//Assignment 3.2
		if(!poker) clearSelected();
		if(deucesWild) clearWild();  //Deuces Wild only
		createCardListeners();
		
	}
	
	private void createImageViews() {

		for(int i=0; i<handSize; i++) {
			cardImages.add(new ImageView());
		}		
	}
	
	private void createStackPanes() {
		for(int i=0; i<stackPane.length; i++) {
			stackPane[i] = new StackPane();
			
			//Add to the HBox container
			playerCardArea.getChildren().add(stackPane[i]);
			
			
		}
	}
	
	private void addSelectedImageToStackPanes() {
		for(int i=0; i<stackPane.length; i++) {
			selected[i] = new ImageView(new Image("file:images/selected.png"));
			stackPane[i].getChildren().add(selected[i]);
		}		
	}
	
	private void addCardImageViewsToStackPanes() {
		for(int i=0; i<stackPane.length; i++) {
			stackPane[i].getChildren().add(cardImages.get(i));			
		}
	}
	
	private void addWildImageToStackPanes() {
		for(int i=0; i<stackPane.length; i++) {
			wild[i] = new ImageView(new Image("file:images/wild.png"));
			stackPane[i].getChildren().add(wild[i]);			
		}
	}
	
	private void styleHandResults() {
		//Create an object for the Hand Results (the handDescr object from PokerHand)
		handResults.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.ITALIC, 32));
		handResults.setFill(Color.RED);
		VBox.setMargin(handResults, new Insets(0, 0, 0, 0));
	}

	private void createCardListeners() {
		 for (int i = 0; i < cardImages.size(); i++) {
		        ImageView cardImage = cardImages.get(i);
		        int index = i; 

		        cardImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
		        	if (!cardSelected[index]) {
		                if (selectedCount >= maxSelect) {
		                    return;
		                }
		                for (int j = 0; j < cardImages.size(); j++) {
		                    if (cardSelected[j]) {
		                        selected[j].setVisible(false);
		                        cardSelected[j] = false;
		                        selectedCount--;
		                    }
		                }
		                selected[index].setVisible(true);
		                cardSelected[index] = true;
		                selectedCount++;
		                playSelectSound();
		              
		                ScaleTransition scaleTransitionCards = new ScaleTransition(Duration.millis(200), cardImage);
		                scaleTransitionCards.setToX(1.1);
		                scaleTransitionCards.setToY(1.1);
		                scaleTransitionCards.play();
		                ScaleTransition scaleTransitionSelected = new ScaleTransition(Duration.millis(200), selected[index]);
		                scaleTransitionSelected.setToX(1.1);
		                scaleTransitionSelected.setToY(1.1);
		                scaleTransitionSelected.play();
		             
		            } else {
		                
		                selected[index].setVisible(false);
		                cardSelected[index] = false;
		                selectedCount--;
		                
		                ScaleTransition scaleTransitionCards = new ScaleTransition(Duration.millis(200), cardImage);
		                scaleTransitionCards.setToX(1.0);
		                scaleTransitionCards.setToY(1.0);
		                scaleTransitionCards.play();
		                ScaleTransition scaleTransitionSelected = new ScaleTransition(Duration.millis(200), selected[index]);
		                scaleTransitionSelected.setToX(1.0);
		                scaleTransitionSelected.setToY(1.0);
		                scaleTransitionSelected.play();
		            }
		        });
		    }
		}


	//This adds the appropriate card images to the screen
	protected void showCards() {
		
		Hand playerHand = player.getHand();
		Card[] playerCard = playerHand.getCards();  //To use to get the image url
		String imageURL; //To hold the image url
		
		for(int i=0; i<playerCard.length; i++) {
			imageURL = "file:images/card/" + playerCard[i].getCardImage(); //Get the cardImage value from the card object
			cardImages.get(i).setImage(new Image(imageURL));
		}
	}
	
	protected void showHandDescr() {
		//We need to get the handDesr from the player's hand object
		//Remember, the player has a Hand but the descr sits on PokerHand
		
		Hand playerHand = player.getHand();  //Returns Hand object
		
		//Now access the handDecr
		handResults.setText(playerHand.getHandDescr());
	}

	//Assignment 3.2
	protected void clearSelected() {
		
		for (int i=0; i<selected.length; i++) {
			selected[i].setVisible(false);
		}
		
		for(int i=0; i<cardSelected.length; i++) {
			cardSelected[i] = false;
		}
		
		selectedCount = 0;
	}
	
	//Assignment 3.2
	protected void removeCardImage(int index) {
		cardImages.get(index).setImage(null);
	}


	//Assignment 3.2 - Deuces Wild only
	protected void clearWild() {
		for(int i=0; i<wild.length; i++) {
			wild[i].setVisible(false);
		}
	}
	
	//Assignment 3.2 - Deuces Wild only
	protected void showWild(int index) {
		wild[index].setVisible(true);
	}
	
	//Assignment 3.2 - Deuces Wild only
	protected void disableCardSelect(int index) {
		cardImages.get(index).setDisable(true);
	}
	
	//Assignment 3.2
	protected void disableCardSelect() {
		for(int i=0; i<cardImages.size(); i++) {
			cardImages.get(i).setDisable(true);
		}
	}
	
	//Assignment 3.2
	protected void enableCardSelect() {
		for(int i=0; i<cardImages.size(); i++) {
			cardImages.get(i).setDisable(false);
		}
	}
	
	private void styleCardHolder() {
		String cssLayout = 
				"-fx-border-color: red;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 2;\n" +
                "-fx-border-style: solid;\n";

		playerCardArea.setStyle(cssLayout);
		playerCardArea.setPrefWidth(450);
		playerCardArea.setPrefHeight(130);
		playerCardArea.setMinHeight(USE_PREF_SIZE);
		playerCardArea.setMinWidth(USE_PREF_SIZE);
		playerCardArea.setMaxHeight(USE_PREF_SIZE);
		playerCardArea.setMaxWidth(USE_PREF_SIZE);
		
		playerCardArea.setAlignment(Pos.CENTER);
		VBox.setMargin(playerCardArea, new Insets(5, 0, 0, 0));
	}

	public boolean[] getCardSelected() {
		return cardSelected;
	}

	public void setMaxSelect(int maxSelect) {
		this.maxSelect = maxSelect;
//		System.out.println("Max Select: " + this.maxSelect);
	}

	public int getSelectedCount() {
		return selectedCount;
	}
	protected void clearHandDescr() {
		handResults.setText("");
	}
	private void styleCardImages() {
		for(int i = 0; i < cardImages.size(); i++) {
			ImageView imageView = cardImages.get(i);
			imageView.setStyle("fx-border-color: black; -fx-border-width: 3px; -fx-border-radius: 5px");
		}
	}
	private void loadSelectSound() {
		String soundURL = "sounds/selectSound.mp3";
		selectSoundMedia = new Media(new File(soundURL).toURI().toString());
		selectSoundPlayer = new MediaPlayer(selectSoundMedia); 
	}
	private void playSelectSound() {
			selectSoundPlayer.seek(Duration.ZERO);
			selectSoundPlayer.play();
	}
	protected void clearAnimation() {
		 for (int i = 0; i < cardImages.size(); i++) {
		        ImageView cardImage = cardImages.get(i);
		        int index = i; 
		ScaleTransition scaleTransitionCards = new ScaleTransition(Duration.millis(200), cardImage);
        scaleTransitionCards.setToX(1.0);
        scaleTransitionCards.setToY(1.0);
        scaleTransitionCards.play();
        ScaleTransition scaleTransitionSelected = new ScaleTransition(Duration.millis(200), selected[index]);
        scaleTransitionSelected.setToX(1.0);
        scaleTransitionSelected.setToY(1.0);
        scaleTransitionSelected.play();
	}
	}
	

}
