package application;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import shogi.graphics.AbstractDialog;

public class AlertManager implements AbstractDialog{

	@Override
	public boolean ask(String title, String content) {
		//成り判定
		Alert alrt = new Alert(AlertType.CONFIRMATION);
		alrt.setTitle(title);
		alrt.setHeaderText(null);
		alrt.setContentText(content);
		Optional<ButtonType> result = alrt.showAndWait();

		//OKボタンの場合はtrueを返す
		if (result.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}
}
