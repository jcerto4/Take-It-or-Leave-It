package reports;
import player.Player;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import gameoutput.GameData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameReport {
	private int windowWidth = 1400;
	private int windowHeight = 600;
	
	private Stage reportStage = new Stage();

	private BorderPane reportPane = new BorderPane();  
	private ScrollPane scrollPane = new ScrollPane();
	
	private Button btnSave = new Button("Save");
	private Button btnExit = new Button("Exit");
	private VBox btnAndMessageContainer = new VBox();
	
	private Player player;
	
	private GameData gameData = new GameData();
	
	private Media printSoundMedia;
	private MediaPlayer printSoundPlayer;
	
	public GameReport(Player player) {
		this.player = player;
		
		createHeader();
		styleButtons();
		showData();
		loadPrintSound();
		createSaveButtonListeners();
		createExitButtonListeners();
		showScene();
	}
	private void showData() {
		VBox contentBox = new VBox(5);
		ResultSet results = gameData.getReportData(player);
		try {
		while(results.next()) {
			HBox rows = new HBox(10);
			rows.getChildren().addAll(
                    createStyledText(String.valueOf(results.getInt("game_id")), 16, 300, Color.BLACK),
                    createStyledText(results.getString("hand_descr"), 16, 300, Color.BLACK),
                    createStyledText(String.valueOf(results.getInt("amount_won")), 16, 300, Color.BLACK),
                    createStyledText(String.valueOf(results.getInt("player_bank")), 16, 300, Color.BLACK)
                );
			rows.setPadding(new Insets(5,5,5,5));
			contentBox.getChildren().add(rows);
		}
	}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		scrollPane.setContent(contentBox);
		reportPane.setCenter(scrollPane);
		reportPane.setPadding(new Insets(20));
	}
	private Text createStyledText(String content, int fontSize, int width, Color color) {
		Text text = new Text(content);
		text.setFont(Font.font("Georgia", fontSize));
		text.setWrappingWidth(width);
		text.setFill(color);
		text.setTextAlignment(TextAlignment.CENTER);
		return text;
	}
	private void createHeader() {
		Text mainTitle = new Text(player.getName() + "'s Game Data");
		mainTitle.setFont(Font.font("Arial", 36));
		mainTitle.setTextAlignment(TextAlignment.CENTER);
		mainTitle.setTranslateY(-10);
		VBox headerContainer = new VBox(10);
		HBox scrollPaneHeader = new HBox(10);
		scrollPaneHeader.getChildren().addAll(
	            createStyledText("Game ID", 24, 300, Color.RED),
	            createStyledText("Hand Description", 24, 300, Color.RED),
	            createStyledText("Amount Won", 24, 300, Color.RED),
	            createStyledText("Player Bank", 24, 300, Color.RED)
	        );
		headerContainer.getChildren().addAll(mainTitle, scrollPaneHeader);
		headerContainer.setAlignment(Pos.BASELINE_CENTER);
		reportPane.setTop(headerContainer);
	}
	private void createSaveButtonListeners() {
		btnSave.setOnAction(event -> save());
	}
	private void createExitButtonListeners() {
		btnExit.setOnAction(event -> exitReport());
	}
	private void styleButtons() {
		btnSave.setFont(Font.font("Arial", 16));
		btnExit.setFont(Font.font("Arial", 16));
		btnSave.setPrefSize(120, 40);
		btnExit.setPrefSize(120, 40);
		
		HBox btnContainer = new HBox(20);
		btnContainer.getChildren().addAll(btnSave, btnExit);
		
		btnContainer.setAlignment(Pos.CENTER);
		
		btnContainer.setPadding(new Insets(20,25,20,25));
		
		btnAndMessageContainer.getChildren().add(0, btnContainer);
		btnAndMessageContainer.setAlignment(Pos.CENTER);
		reportPane.setBottom(btnAndMessageContainer);
	}
	private void exitReport() {
		gameData.close();
	
		reportStage.close();
	}
	
	
	private void showScene() {
		Scene scene = new Scene(reportPane, windowWidth, windowHeight);

		reportStage.setTitle("Tioli Poker Data for " + player.getName());
		
		reportStage.setScene(scene);
	
		reportStage.show();
	}
	private void save() {
		try(DataOutputStream output = new DataOutputStream(new FileOutputStream("files/report.dat"))){
			ResultSet results = gameData.getReportData(player);
			while(results.next()) {
				output.writeUTF(String.valueOf(results.getInt("game_id")));
				output.writeUTF(player.getName());
				output.writeUTF(results.getString("hand_descr"));
				output.writeInt(results.getInt("amount_won"));
				output.writeInt(results.getInt("player_bank"));
			}
			System.out.println("File Created");
			if(btnAndMessageContainer.getChildren().size() > 1) {
				btnAndMessageContainer.getChildren().remove(1);
			}
			btnAndMessageContainer.getChildren().add(createStyledText("Data Saved to File...", 24, 0, Color.GREEN));
			btnAndMessageContainer.setPadding(new Insets(0,20,20,20));
			playPrintSound();
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	private void loadPrintSound() {
		String soundURL = "sounds/printSound.mp3";
		printSoundMedia = new Media(new File(soundURL).toURI().toString());
		printSoundPlayer = new MediaPlayer(printSoundMedia); 
	}
	private void playPrintSound() {
		printSoundPlayer.seek(Duration.ZERO);
		printSoundPlayer.play();
	}
}
