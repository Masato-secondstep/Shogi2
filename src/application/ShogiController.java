package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import shogi.core.GameManager;

public class ShogiController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox startMenu;

    @FXML
    private ChoiceBox<String> goteKindChoice;

    @FXML
    private ChoiceBox<String> teaiChoice;

    @FXML
    private Pane masterPane;

    @FXML
    private ChoiceBox<String> senteKindChoice;

    @FXML
    private Button gameStartButton;

    @FXML
    void gameStartButtonClicked(ActionEvent event) {
    	startMenu.setVisible(false);
    	System.out.println(resources);

    	JavaFxGraphicsManager gm = new JavaFxGraphicsManager(masterPane);
    	GameManager.getInstance().gameStart(gm);
    }

    @FXML
    void initialize() {
        assert startMenu != null : "fx:id=\"startMenu\" was not injected: check your FXML file 'Shogi.fxml'.";
        assert goteKindChoice != null : "fx:id=\"goteKindChoice\" was not injected: check your FXML file 'Shogi.fxml'.";
        assert teaiChoice != null : "fx:id=\"teaiChoice\" was not injected: check your FXML file 'Shogi.fxml'.";
        assert masterPane != null : "fx:id=\"masterPane\" was not injected: check your FXML file 'Shogi.fxml'.";
        assert senteKindChoice != null : "fx:id=\"senteKindChoice\" was not injected: check your FXML file 'Shogi.fxml'.";
        assert gameStartButton != null : "fx:id=\"gameStartButton\" was not injected: check your FXML file 'Shogi.fxml'.";

        senteKindChoice.setValue("プレイヤー");
        goteKindChoice.setValue("プレイヤー");
        teaiChoice.setValue("平手");

		/*
		//背景色の設定
		sentePane.setBackground(new Background(new BackgroundFill(
				FieldPane.getBackGroundColor(), null, null)));;

		gotePane.setBackground(new Background(new BackgroundFill(
						FieldPane.getBackGroundColor(), null, null)));;
		*/    }
}
