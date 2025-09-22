package gameobjects;

import game.DealerArea;
import game.PlayerArea;
import game.Tioli;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GameOptions extends VBox{
	
	private RadioButton rbRed = new RadioButton("Red Card Back");
	private RadioButton rbBlue = new RadioButton("Blue Card Back");
	
	private CheckBox showTimer = new CheckBox("Timer Visible");
	private CheckBox useTimer = new CheckBox("Use Timer");
	
	private Label lblSetTime = new Label("Set Timer To:");
	private TextField txtSetTime;
	
	private ComboBox<Integer> cbDrawCount = new ComboBox<Integer>();
	private Label lblDrawCounts;
	
	private Text headerText = new Text("Game Options");
	
	private PlayerArea playerArea;
	private DealerArea dealerArea;
	private GameTimer timer;
	
	private VBox btnVBox = new VBox(10);
	private VBox timeCheckVBox = new VBox(10);
	private HBox timeLabelHBox = new HBox(10);
	private VBox comboBoxVBox = new VBox(10);
	
	public GameOptions(PlayerArea playerArea, DealerArea dealerArea, GameTimer timer) {
		this.playerArea = playerArea;
		this.dealerArea = dealerArea;
		this.timer = timer;
		createCardBackOptions();
		createShowTimerOption();
		createUseTimerOption();
		createSetTimer();
		createComboBox();
		styleTextLabels();
		styleBtnContainer();
		styleTimeLabelContainer();
		styleShowUseTimeContainer();
		styleComboBox();
		this.getChildren().addAll(headerText, btnVBox, timeCheckVBox, timeLabelHBox, comboBoxVBox);
		this.setSpacing(10);
		this.setPadding(new Insets(10));
		this.setAlignment(Pos.CENTER_LEFT);
	}
	private void createComboBox() {
		cbDrawCount.getItems().addAll(4, 5, 6);
		cbDrawCount.setValue(5);
		lblDrawCounts = new Label("Draw Amount", cbDrawCount);
		lblDrawCounts.setContentDisplay(ContentDisplay.BOTTOM);
		cbDrawCount.setOnAction(event -> Tioli.setMaxTioliCards(cbDrawCount.getValue()));
		
	}

	private void createSetTimer() {
		Integer defaultSecondsInt = timer.getStartSeconds();
		String defaultSecondsString = defaultSecondsInt.toString();
		txtSetTime = new TextField(defaultSecondsString);
		txtSetTime.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
				int txtSetTimerValue = Integer.parseInt(txtSetTime.getText());
				timer.setStartSeconds(txtSetTimerValue);
				timer.refreshTimerDisplay();
			}
		});
		
	}

	private void createUseTimerOption() {
		useTimer.setSelected(true);
		
		useTimer.setOnAction(event -> {
			if(useTimer.isSelected()) {
				timer.setUseTimer(true);
				timer.setVisible(true);
				lblSetTime.setVisible(true);
				txtSetTime.setVisible(true);
				showTimer.setSelected(true);
				showTimer.setDisable(false);
			}else {
				timer.setUseTimer(false);
				timer.setVisible(false);
				lblSetTime.setVisible(false);
				txtSetTime.setVisible(false);
				showTimer.setSelected(false);
				showTimer.setDisable(true);
			}
		});
		
	}

	private void createShowTimerOption() {
		showTimer.setSelected(true);
		
		showTimer.setOnAction(event -> {
			if(showTimer.isSelected()) {
				timer.setVisible(true);
			}else {
				timer.setVisible(false);
			}
			
				
				
		});
		
	}
	private void createCardBackOptions() {
		ToggleGroup toggleGroup = new ToggleGroup();
		rbRed.setToggleGroup(toggleGroup);
		rbBlue.setToggleGroup(toggleGroup);
		rbRed.setSelected(true);
		
		rbRed.setOnAction(event -> dealerArea.setCardBack("red"));
		rbBlue.setOnAction(event -> dealerArea.setCardBack("blue"));
		
	}
	private void styleComboBox() {
		lblDrawCounts.setFont(Font.font("Georgia", 16));
		lblDrawCounts.setTextFill(Color.RED);
		comboBoxVBox.getChildren().addAll(lblDrawCounts);
		
		
	}
	private void styleTimeLabelContainer() {
		timeLabelHBox.getChildren().addAll(lblSetTime, txtSetTime);
		timeLabelHBox.setAlignment(Pos.CENTER_LEFT);
		
	}
	private void styleShowUseTimeContainer() {
		showTimer.setTextFill(Color.RED);
		useTimer.setTextFill(Color.RED);
		showTimer.setFont(Font.font("Georgia", 16));
		useTimer.setFont(Font.font("Georgia", 16));
		timeCheckVBox.getChildren().addAll(showTimer, useTimer);
		timeCheckVBox.setAlignment(Pos.CENTER_LEFT);
		
	}
	private void styleBtnContainer() {
		rbRed.setFont(Font.font("Georgia", 16));
		rbBlue.setFont(Font.font("Georgia", 16));
		rbRed.setTextFill(Color.RED);
		rbBlue.setTextFill(Color.RED);
		btnVBox.getChildren().addAll(rbRed, rbBlue);
		btnVBox.setAlignment(Pos.CENTER_LEFT);
	}
	private void styleTextLabels() {
		lblSetTime.setFont(Font.font("Georgia", 16));
		txtSetTime.setFont(Font.font("Georgia", 16));
		headerText.setFont(Font.font("Georgia", 18));
		lblSetTime.setTextFill(Color.RED);
		txtSetTime.setPrefWidth(50);
		headerText.setFill(Color.RED);
		headerText.setUnderline(true); 
		headerText.setTranslateX(10);
	}
	public void disableGameOptions() {
		useTimer.setVisible(false);
		lblSetTime.setVisible(false);
		lblDrawCounts.setVisible(false);
		txtSetTime.setVisible(false);

	}

	public void enableGameOptions() {
		useTimer.setVisible(true);
		lblSetTime.setVisible(true);
		lblDrawCounts.setVisible(true);
		txtSetTime.setVisible(true);

	}
}
