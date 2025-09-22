package game;


import java.io.File;

import cards.Card;
import deck.Deck;
import gameobjects.GameOptions;
import gameobjects.GameTimer;
import gameobjects.PayoutTable;
import gameobjects.ScoreBoard;
import gameobjects.Wager;
import gameoutput.GameData;
import gameoutput.GameFile;
import gameoutput.RandomPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import player.Dealer;
import player.Player;
import reports.GameReport;

public class Tioli {
	
	private Player player;
	private Player dealer;
	
	private PlayerArea playerArea;
	private DealerArea dealerArea;
	
	private BorderPane gameScreen = new BorderPane();
	private HBox header;
	private VBox centerSection;
	private VBox rightSection;
	
	private PayoutTable payoutTable;
	private Wager wager;
	private ScoreBoard scoreBoard;
	
	private Stage primaryStage;
	
	private Button btnDeal = new Button("Deal");
	private Button btnTakeIt = new Button("Take It");
	private Button btnLeaveIt = new Button("Leave It");
	private Button btnExit = new Button("Exit");
	private Button btnReport = new Button("Report");
	
	private GameTimer timerObj;
	
	private static int maxTioliCards = 5;
	
	private int tioliCardsDealt = 0;
	
	private Media dealSoundMedia;
	private MediaPlayer dealSoundPlayer;
	private Media takeItSoundMedia;
	private MediaPlayer takeItSoundPlayer;
	private Media leaveItSoundMedia;
	private MediaPlayer leaveItSoundPlayer;
	private Media coinPayoutSoundMedia;
	private MediaPlayer coinPayoutSoundPlayer;
	private Media loseSoundMedia;
	private MediaPlayer loseSoundPlayer;
	private Media reportSoundMedia;
	private MediaPlayer reportSoundPlayer;
	
	private GameOptions gameOptions;
	
	
	public Tioli() {
		instantiateObjects();
		createHeader();
		createCenterSection();
		createRightSection();
		styleButtons();
		loadSounds();
		setGameScreen();
		setBackGroundImage();
		showGame();
		createDealButtonListeners();
		createTakeItButtonListeners();
		createLeaveItButtonListeners();
		createExitButtonListeners();
		createReportButtonListeners();
		disableTioliButtons();
	}
	
	private void startDeal() {
			dealPlayer();
	}
	private void dealPlayer() {
		stopPayoutSound();
		disableWagerButtons();
		disableTioliButtons();
		disableGameOptions();
		clearHandDescr();
		setScoreBoard(0);
		animateCardsDealt();
	}
	private void takeIt() {	
		if(isNotSelected()) {
			return;
		}else {
			takeItMagic();
		}
		checkTioliCardsDealt();
	}
	private void leaveIt() {
		stopTimer();
		clearSelected();
		leaveItMagic();
		evaluateHand();
		checkTioliCardsDealt();
	}
	public void endHand() {
		clearSelected();
		disableCardSelect();
		refreshTimerDisplay();
		stopTimer();
		updateBank();
		tioliCardsDealt = 0;
		disableTioliButtons();
		enableGameOptions();
		enableWagerButtons();
		enableDealButton();
		enableReportButton();
		//writeCSVData("playerdata.txt");
		//writeBinaryData("playerdata.dat");
		writeGameData();
	}
	
	private void createExitButtonListeners() {
		btnExit.setOnAction(event -> exitGame());
		
	}
	private void createLeaveItButtonListeners() {
		btnLeaveIt.setOnAction(event -> leaveIt());
		
	}
	private void createTakeItButtonListeners() {
		btnTakeIt.setOnAction(event -> takeIt());
		
	}
	private void createDealButtonListeners() {
		btnDeal.setOnAction(event -> {
		if(isWagerValid()) {
			startDeal();
			disableDealButton();
			}else {
				showAlert("Invalid Wager Amount");
			}
		});
		
	}
	private void createReportButtonListeners() {
		btnReport.setOnAction(event -> createGameReport());
	}
	private void createHeader() {
		Text text = new Text("Lucky Luciano's Fortune Club\n\t Welcome " + player.getName());
		text.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
		text.setFill(Color.WHITE);
		header = new HBox(text);
		header.setAlignment(Pos.CENTER_RIGHT);
		header.setPadding(new Insets(0, 20, 0, 0));
	}
	private void createCenterSection() {
		VBox takeItOrLeaveIt = new VBox(10);
		takeItOrLeaveIt.getChildren().addAll(btnTakeIt, btnLeaveIt);
		takeItOrLeaveIt.setAlignment(Pos.CENTER);
		dealerArea.getChildren().add(takeItOrLeaveIt);
		centerSection = new VBox();
		centerSection.getChildren().addAll(timerObj, dealerArea, playerArea, btnDeal);
		centerSection.setAlignment(Pos.CENTER);
		centerSection.setSpacing(30);
		dealerArea.setAlignment(Pos.CENTER);
		playerArea.setAlignment(Pos.CENTER);
		btnDeal.setAlignment(Pos.CENTER);
}
	private void createRightSection() {
		HBox btnExitReportContainer = new HBox(btnExit, btnReport);
		btnExitReportContainer.setAlignment(Pos.CENTER);
		btnExitReportContainer.setSpacing(20);
		rightSection = new VBox(10);
		rightSection.getChildren().addAll(payoutTable, wager, scoreBoard, btnExitReportContainer);
		rightSection.setAlignment(Pos.CENTER_RIGHT);
	}
	private void styleButtons() {
		btnDeal.setFont(Font.font("Georgia", 16));
		btnTakeIt.setFont(Font.font("Georgia", 16));
		btnLeaveIt.setFont(Font.font("Georgia", 16));
		btnExit.setFont(Font.font("Georgia", 16));
		btnReport.setFont(Font.font("Georgia", 16));
		
		btnDeal.setPrefWidth(100);
		btnTakeIt.setPrefWidth(100);
		btnLeaveIt.setPrefWidth(100);
		btnExit.setPrefWidth(100);
		btnReport.setPrefWidth(100);
		
		btnDeal.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
		btnTakeIt.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
		btnLeaveIt.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
		btnExit.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
		btnReport.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
	}
	private void showGame() {
		Scene scene = new Scene(gameScreen, 1200, 1000);
		primaryStage = new Stage();
		primaryStage.setTitle("Joe's Tioli");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	private void exitGame() {
		primaryStage.close();
	}
	private void setBackGroundImage() {
		String imageURL = "file:backgrounds/casinoBackground4.jpg";
		BackgroundImage casinoBackground = new BackgroundImage(
				new Image(imageURL, 1200, 1000, false, true),
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT
				);
		gameScreen.setBackground(new Background(casinoBackground));
	}
	private void instantiateObjects() {
		player = RandomPlayer.getPlayer();
		Deck deck = new Deck(52);
		dealer = new Dealer(deck);
		
		playerArea = new PlayerArea(player, 5, "tioli");
		dealerArea = new DealerArea(dealer, "tioli");
		
		payoutTable = new PayoutTable("tioli");
		payoutTable.setTextColors(Color.WHITE);
		wager = new Wager(player, 50, "red");
		scoreBoard = new ScoreBoard(player, "red");
		timerObj = new GameTimer(15, btnLeaveIt);
		gameOptions = new GameOptions(playerArea, dealerArea, timerObj);
	}
	
	private void loadDealSound() {
		String soundURL = "sounds/dealSound.mp3";
		dealSoundMedia = new Media(new File(soundURL).toURI().toString());
		dealSoundPlayer = new MediaPlayer(dealSoundMedia); 
	}
	private void loadTakeItSound() {
		String soundURL = "sounds/takeItSound.mp3";
		takeItSoundMedia = new Media(new File(soundURL).toURI().toString());
		takeItSoundPlayer = new MediaPlayer(takeItSoundMedia);
	}
	private void loadLeaveItSound() {
		String soundURL = "sounds/leaveItSound.mp3";
		leaveItSoundMedia = new Media(new File(soundURL).toURI().toString());
		leaveItSoundPlayer = new MediaPlayer(leaveItSoundMedia); 
	}
	private void loadCoinPayoutSound() {
		String soundURL = "sounds/coinPayout.mp3";
		coinPayoutSoundMedia = new Media(new File(soundURL).toURI().toString());
		coinPayoutSoundPlayer = new MediaPlayer(coinPayoutSoundMedia); 
	}
	private void loadLoseSound() {
		String soundURL = "sounds/loseSound.mp3";
		loseSoundMedia = new Media(new File(soundURL).toURI().toString());
		loseSoundPlayer = new MediaPlayer(loseSoundMedia); 
	}
	private void loadReportSound() {
		String soundURL = "sounds/reportSound.mp3";
		reportSoundMedia = new Media(new File(soundURL).toURI().toString());
		reportSoundPlayer = new MediaPlayer(reportSoundMedia); 
	}
	private void loadSounds() {
		loadDealSound();
		loadTakeItSound();
		loadLeaveItSound();
		loadCoinPayoutSound();
		loadLoseSound();
		loadReportSound();
	}
	
	private void playDealSound() {
		dealSoundPlayer.seek(Duration.ZERO);
		dealSoundPlayer.play();
	}
	private void playTakeItSound() {
		takeItSoundPlayer.seek(Duration.ZERO);
		takeItSoundPlayer.play();
	}
	private void playLeaveItSound() {
		leaveItSoundPlayer.seek(Duration.ZERO);
		leaveItSoundPlayer.play();
	}
	private void playCoinPayout() {
		coinPayoutSoundPlayer.seek(Duration.ZERO);
		coinPayoutSoundPlayer.play();
	}
	private void playLoseSound() {
		loseSoundPlayer.seek(Duration.ZERO);
		loseSoundPlayer.play();
	}
	private void playReportSound() {
		reportSoundPlayer.seek(Duration.ZERO);
		reportSoundPlayer.play();
	}
	private void stopPayoutSound() {
		coinPayoutSoundPlayer.stop();
	}
	
	public static void setMaxTioliCards(int maxTioliCards) {
		Tioli.maxTioliCards = maxTioliCards;
	}
	private void setScoreBoard(int num) {
		scoreBoard.setWinAmount(num);	
		}
	private void setGameScreen() {
		gameScreen.setTop(header);
		gameScreen.setCenter(centerSection);
		gameScreen.setRight(rightSection);
		gameScreen.setLeft(gameOptions);
	}
	
	
	private void disableDealButton() {
		btnDeal.setDisable(true);
	}
	private void disableCardSelect() {
		playerArea.disableCardSelect();
	}
	private void disableTioliButtons() {
		btnTakeIt.setDisable(true);
		btnLeaveIt.setDisable(true);
	}
	private void disableGameOptions() {
		gameOptions.disableGameOptions();

	}
	private void disableWagerButtons() {
		wager.disableWagerButtons();
	}
	private void disableReportButton() {
		btnReport.setDisable(true);
	}
	
	private void enableCardSelect() {
		playerArea.enableCardSelect();
	}
	private void enableTioliButtons() {
		btnTakeIt.setDisable(false);
		btnLeaveIt.setDisable(false);
	}
	private void enableDealButton() {
		btnDeal.setDisable(false);
	}
	private void enableWagerButtons() {
		wager.enableWagerButtons();
	}
	private void enableGameOptions() {
		gameOptions.enableGameOptions();

	}
	private void enableReportButton() {
		btnReport.setDisable(false);
	}
		
	private void clearCards() {
		((Dealer) dealer).gatherUsedCards(player); 
	}
	private void clearCardImages() {
		for(int i = 0; i < 5; i++) {
			playerArea.removeCardImage(i);
		}
	}
	private void clearHandDescr() {
		playerArea.clearHandDescr();
	}
	private void clearAnimation() {
		playerArea.clearAnimation();
	}
	private void clearSelected() {
		playerArea.clearSelected();
	}
	
	private void showPlayerCards() {
		playerArea.showCards();
	}
	private void showHandDescr() {
		 playerArea.showHandDescr();
	}
	private void showAlert(String text) {
		Platform.runLater(() -> {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
		alert.showAndWait();
		});
	}
	
	private void animateCardsDealt() {
		clearCardImages();
		sendCardsToDiscardPile();
		clearCards();
		reshuffleDeck();
		disableCardSelect();
		disableReportButton();
		int dealDelay = 1000;
		Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		for(int i = 0; i < 5; i++) {
			Duration delayDuration = Duration.millis(dealDelay * i);
			KeyFrame keyframe = new KeyFrame(delayDuration, event ->{
					((Dealer) dealer).dealCard(player);
					playDealSound();
					showPlayerCards();
			});
			timeline.getKeyFrames().add(keyframe);
		}
			Duration delayDuration = Duration.millis(dealDelay * 5);//show description after cards have been dealt
		    KeyFrame finalKeyFrame = new KeyFrame(delayDuration, event -> {
		        evaluateHand();
		        showHandDescr();
		    });
			timeline.getKeyFrames().add(finalKeyFrame);
			timeline.play();
		
		timeline.setOnFinished(event -> {
			timeline.stop();
			enableTioliButtons();
			enableCardSelect();
			dealDealer();
			startTimer();
		});
	}
	private void dealDealer() {
		((Dealer) dealer).dealCard(dealer);
		dealerArea.showTioliCard();
		tioliCardsDealt++;
	}
	
	private void stopTimer() {
		timerObj.stopTimer();
	}
	private void startTimer() {
		timerObj.startTimer();
	}
	private void refreshTimerDisplay() {
		timerObj.refreshTimerDisplay();
	}
	
	private void sendCardsToDiscardPile() {
		Card[] cards = player.getHand().getCards();
		if(cards.length < 5) {
			return;
		}
		for(int i = 0; i < 5; i++) {
			Card card = player.getHand().getCard(i);
			dealerArea.showDiscardedCard(card);
			((Dealer) dealer).getDeck().addUsedCards(card);
		}
	}
	
	private void reshuffleDeck() {
		if(((Dealer) dealer).reshuffleDeck()) {
			dealerArea.removeDiscardImage();
		}
	}
	
	private void updateBank() {
		int playerWager = wager.getWagerAmount();
		int payoutAmount = payoutTable.getPayout(player.getHand(), playerWager);
		int currentBank = player.getBank();
		player.setBank(currentBank + payoutAmount);
		scoreBoard.setWinAmount(payoutAmount);
		scoreBoard.updateBank();
		if(payoutAmount > 0) {
			playCoinPayout();
			showAlert("Congratulations " + 
			player.getName() + 
			"! \nYou won $" + payoutAmount +
			" \nPlace a new wager to continue or exit to leave."
			);
		}else {
			playLoseSound();
			showAlert("Better luck next time! \nPlace a new wager to continue or exit to leave.");
		}
	}
	private boolean isWagerValid() {
		if(wager.getWagerAmount() > player.getBank() || player.getBank() == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	private void takeItMagic() {
		boolean[] cardSelected = playerArea.getCardSelected();
		for(int i = 0; i < cardSelected.length; i++) {
			if(cardSelected[i]) {
				stopTimer();
				
				removeTioliImage();
				
				Card tioliCard = dealer.getHand().removeCard(0);
				playerArea.removeCardImage(i);
				playTakeItSound();
				Card removedCard = player.getHand().getCard(i);
				((Dealer) dealer).getDeck().addUsedCards(removedCard);
				dealerArea.showDiscardedCard(removedCard);
				
				player.getHand().setCard(i, tioliCard);
				
				showPlayerCards();
				
				evaluateHand();
				
				showHandDescr();
				
				clearSelected();
				
				clearAnimation();
				
				break;
			}
		}
	}	
	private void leaveItMagic() {
		Card temp = dealer.getHand().removeCard(0);
		removeTioliImage();
		dealerArea.showDiscardedCard(temp);
		((Dealer) dealer).getDeck().addUsedCards(temp);
		clearAnimation();
		playLeaveItSound();
	}
	
	private void removeTioliImage() {
		dealerArea.removeTioliImage();		
	}
	
	private void checkTioliCardsDealt() {
		if(tioliCardsDealt == maxTioliCards) {
			endHand();
		}else {
			startTimer();
			dealDealer();
		}
	}
	private boolean isNotSelected() {
		if(playerArea.getSelectedCount() == 0) {
			return true;
		}else {
			return false;
		}
	}
	
	private void evaluateHand() {
		player.getHand().evaluateHand();
	}
	private int getPayoutAmount() {
		int playerWager = wager.getWagerAmount();
		return payoutTable.getPayout(player.getHand(), playerWager);
	}
	
	private void writeCSVData(String filename) {
		GameFile.writeCSVData(filename, player, getPayoutAmount());
	}
	private void writeBinaryData(String filename) {
		GameFile.writeBinaryData(filename, player, getPayoutAmount());
	}
	private void writeGameData() {
		GameData data = new GameData();
		data.updateBank(player);
		data.insertResults(player, getPayoutAmount());
		data.close();
	}
	private void createGameReport() {
		GameReport gameReport = new GameReport(player);
		playReportSound();
	}
		
}
	

