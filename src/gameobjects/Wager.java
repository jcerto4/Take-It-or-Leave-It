package gameobjects;

import java.io.File;
import java.text.DecimalFormat;

//import com.sun.prism.paint.Color;

import player.Player;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.paint.Color;

public class Wager extends VBox {

	private TextField wagerAmountText = new TextField();
	private Button btnAdd = new Button("+");
	private Button btnSubtract = new Button("-");

	private HBox wagerContainer = new HBox(10);
	private HBox buttonContainer = new HBox(20);

	private String borderColor = "green";

	private Player player;
	private int changeAmount;
	private int wagerAmount = 100;
	
	private Media positiveSoundMedia;
	private Media negativeSoundMedia;
	private MediaPlayer positiveSoundPlayer;
	private MediaPlayer negativeSoundPlayer;

	public Wager(Player player, int changeAmount) {
		this.player = player;
		this.changeAmount = changeAmount;
		buildWager();
	}

	public Wager(Player player, int changeAmount, String borderColor) {
		this.player = player;
		this.changeAmount = changeAmount;
		this.borderColor = borderColor;
		buildWager();
	}
	private void loadPositiveNegativeSounds(){
		String positiveSoundURL = "sounds/positiveSound.wav";
		String negativeSoundURL = "sounds/negativeSound.wav";
		positiveSoundMedia = new Media(new File(positiveSoundURL).toURI().toString());
		negativeSoundMedia = new Media(new File(negativeSoundURL).toURI().toString());
		positiveSoundPlayer = new MediaPlayer(positiveSoundMedia);
		negativeSoundPlayer = new MediaPlayer(negativeSoundMedia);
	}
	private void playPositiveSound() {
		positiveSoundPlayer.seek(Duration.ZERO);
		positiveSoundPlayer.play();
	}
	private void playNegativeSound() {
		negativeSoundPlayer.seek(Duration.ZERO);
		negativeSoundPlayer.play();
	}
	public int getWagerAmount() {
		return wagerAmount;
	}

	private void buildWager() {
		updateWagerText();
		createWagerArea();
		createButtons();
		createButtonListeners();
		addToVBox();
		styleVBox();
		loadPositiveNegativeSounds();
	}

	private void createWagerArea() {
		Label betLabel = new Label("Bet: ");
		betLabel.setFont(Font.font("Georgia", 20));
		betLabel.setTextFill(Color.WHITE);
		betLabel.setPadding((new Insets(0, 0, 0, 10)));

		wagerAmountText.setFont(Font.font("Georgia", 20));
		wagerAmountText.setPrefColumnCount(5);
		wagerAmountText.setEditable(false);
		wagerAmountText.setPadding((new Insets(0, 0, 0, 0)));

		wagerContainer.getChildren().addAll(betLabel, wagerAmountText);
		wagerContainer.setPadding((new Insets(2, 0, 0, 50)));

	}

	private void createButtons() {
		// Style the buttons
		btnAdd.setStyle("-fx-font-size:20; -fx-border-color: black; -fx-border-width: 2px");
		btnSubtract.setStyle("-fx-font-size:20; -fx-border-color: black; -fx-border-width: 2px");

		buttonContainer.getChildren().addAll(btnAdd, btnSubtract);
		HBox.setMargin(btnAdd, new Insets(5, 0, 0, 100));
		HBox.setMargin(btnSubtract, new Insets(5, 0, 0, 0));

	}

	private void createButtonListeners() {
		// Assignment 3.2 Create listener for btnAdd
	    btnAdd.setOnAction(event -> {
	        changeWager(changeAmount);    
	        playPositiveSound();
	    });

	    // Listener for btnSubtract
	    btnSubtract.setOnAction(event -> { 
	        changeWager(changeAmount * -1);
	        playNegativeSound();
	    });
	}
	private void changeWager(int change) {
		int newWager;

		if (this.wagerAmount + change <= 0) {
			newWager = 0;
		} else if (this.wagerAmount + change >= player.getBank()) {
			newWager = player.getBank();
		} else {
			newWager = this.wagerAmount + change;
		}

		this.wagerAmount = newWager;

		updateWagerText();
	}

	private void updateWagerText() {
		wagerAmountText.setText(formattedNumber(wagerAmount));
	}

	private void addToVBox() {
		this.setPadding((new Insets(10, 0, 10, 0)));

		this.getChildren().addAll(wagerContainer, buttonContainer);

	}

	private void styleVBox() {
		if (borderColor.toLowerCase() != "none") {
			String cssLayout = "-fx-border-color: " + borderColor + ";\n" + "-fx-border-insets: 5;\n"
					+ "-fx-border-width: 2;\n" + "-fx-border-style: solid;\n";

			this.setStyle(cssLayout);
			this.setPrefWidth(280);
			this.setPrefHeight(80);
			this.setMaxHeight(USE_PREF_SIZE);
		}
	}

	private String formattedNumber(int number) {
		DecimalFormat formatter = new DecimalFormat("#,###");
		return formatter.format(number);
	}
	public void disableWagerButtons() {
		btnAdd.setDisable(true);
		btnSubtract.setDisable(true);
	}
	public void enableWagerButtons() {
		btnAdd.setDisable(false);
		btnSubtract.setDisable(false);
	}

}
