package application;

import java.util.List;

import javafx.scene.layout.Pane;
import shogi.core.Field;
import shogi.core.Player;
import shogi.core.Player.TurnType;
import shogi.graphics.AbstractDialog;
import shogi.graphics.AbstractGraphicsManager;
import shogi.graphics.AbstractKomaImage;
import shogi.graphics.AbstractKomadaiPane;
import shogi.graphics.FieldClickObserver;
import shogi.graphics.KomaClickObserver;
import shogi.koma.Koma;

//グラフィックの環境依存部分を記載
public class JavaFxGraphicsManager implements AbstractGraphicsManager{


	//ダイアログ
	private AbstractDialog dialog;

	//masterPane
	private static Pane masterPane;

	public JavaFxGraphicsManager(Pane masterPane) {
		this.dialog = new AlertManager();
		JavaFxGraphicsManager.masterPane = masterPane;
	}

	@Override
	public AbstractDialog getAbstractDialog() {
		return dialog;
	}

	@Override
	public void addField(Field field) {
		masterPane.getChildren().add(FieldPane.getInstance());
		field.setFieldPane(FieldPane.getInstance());
	}

	@Override
	public void addPlayer(Player player) {
		player.setAbstractKomadaiPane(createKomadaiPane(
				player.getTurnType(), player.getMochiKoma(), player.getFieldClickObserver()
				));
	}

	@Override
	public void addAllKoma(List<Koma> allKoma) {
		for(Koma aKoma : allKoma) {
			aKoma.setKomaImage(createKomaImage(aKoma));
			FieldPane.getInstance().getChildren().add((KomaImage)aKoma.getKomaImage());
		}
	}

	private AbstractKomaImage createKomaImage(Koma koma) {
		return new KomaImage(
				koma.getTurnType(),
				koma.getKomaType(),
				koma.isNari(),
				koma.getFieldX(),
				koma.getFieldY(),
				(KomaClickObserver)koma,
				masterPane);
	}


	private AbstractKomadaiPane createKomadaiPane(TurnType turnType, List<Koma> mochigoma, FieldClickObserver clickObserver) {
		KomadaiPane ret;
		ret = new KomadaiPane(turnType, mochigoma, clickObserver);

		masterPane.getChildren().add(ret);

		return ret;
	}

}
