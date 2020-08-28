package application;

import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import shogi.core.Player.TurnType;
import shogi.graphics.AbstractKomadaiPane;
import shogi.graphics.FieldClickObserver;
import shogi.koma.Koma;

public class KomadaiPane extends Pane implements AbstractKomadaiPane{

	private TurnType turnType;

	private List<Koma> mochigoma;

	private FieldClickObserver clickObserver;

	private Canvas canvas;

	public KomadaiPane(TurnType turnType, List<Koma> mochigoma, FieldClickObserver clickObserver) {
		this.turnType = turnType;
		this.mochigoma = mochigoma;
		this.clickObserver = clickObserver;

		switch(turnType) {
			case SENTE -> {
				//先手駒台生成
				this.setLayoutX(Constants.KOMADAI_LAYOUT_X_SENTE);
				this.setLayoutY(Constants.KOMADAI_LAYOUT_Y_SENTE);
				this.setPrefWidth(Constants.KOMADAI_WIDTH);
				this.setPrefHeight(Constants.KOMADAI_HEIGHT);
				this.setBackground(new Background(new BackgroundFill(
						FieldPane.getBackGroundColor(), null, null)));;
			}
			case GOTE -> {
				//後手駒台生成
				this.setLayoutX(Constants.KOMADAI_LAYOUT_X_GOTE);
				this.setLayoutY(Constants.KOMADAI_LAYOUT_Y_GOTE);
				this.setPrefHeight(Constants.KOMADAI_HEIGHT);
				this.setPrefWidth(Constants.KOMADAI_WIDTH);
				this.setBackground(new Background(new BackgroundFill(
						FieldPane.getBackGroundColor(), null, null)));;
			}
		}

		//駒台選択キャンバス初期化
		this.canvas = new Canvas(Constants.KOMADAI_WIDTH, Constants.KOMADAI_HEIGHT);
		this.getChildren().add(canvas);

		this.canvas.setOnMouseClicked(e -> {
			Koma koma = getKomadaiKoma((int)e.getX(), (int)e.getY());
			if(koma != null) clickObserver.komadaiClicked(koma);
		});
	}

	private Koma getKomadaiKoma(int x, int y) {

		//クリック位置からmochigomaのindexを算出する
		int index = (int)(x / (Constants.KOMADAI_WIDTH / Constants.KOMADAI_COLS))
			+ (Constants.KOMADAI_COLS) * (int)(y / (Constants.KOMADAI_HEIGHT / Constants.KOMADAI_ROWS));

		//indexがmochigomaを超えていなければ、mochigomaのKomaを返す
		if(index < mochigoma.size()) {
			return mochigoma.get(index);
		} else {
			return null;
		}
	}


	//駒台X位置計算用 mochigomaのindex iからX位置を計算する
	//駒台は4列のため、4で割った余りを計算する
	private double calcLayoutX(int i) {

		//baseは1,3,5,7
		int xBase = ((i % Constants.KOMADAI_COLS) + 1) * 2 -1;
		System.out.println("baseX:" + xBase);

		//左端の中心
		double xCenter = Constants.KOMADAI_WIDTH / (Constants.KOMADAI_COLS * 2);
		System.out.println("xCenter:" + xCenter);

		//中心点からIMAGEの半分を引いた点が左端
		return xBase * xCenter - (Constants.IMAGE_WIDTH / 2) + 6;
	}

	//駒台Y位置計算用
	//駒台は4列のため、4で割った切り捨てで計算する
	private double calcLayoutY(int i) {

		//baseは1,3,5,7
		int yBase = ((int)(i / 4) + 1) * 2 - 1;
		System.out.println("baseY:" + yBase);

		//左端の中心
		double yCenter = Constants.KOMADAI_HEIGHT / (Constants.KOMADAI_ROWS * 2);
		System.out.println("yCenter:" + yCenter);

		return yBase * yCenter - (Constants.IMAGE_HEIGHT / 2) + 6;

		//return ((int)(i / Constants.KOMADAI_COLS)) * Constants.IMAGE_HEIGHT;
	}

	//持ち駒を更新
	public void refresh(List<Koma> mochigoma) {

		//先にremoveする
		for(Koma aKoma : mochigoma) {
			if(this.getChildren().contains(aKoma.getKomaImage())) {
				this.getChildren().remove(aKoma.getKomaImage());
			}
		}

		//改めて駒台に並べる
		for(int i = 0; i < mochigoma.size(); i++) {
			Koma koma = mochigoma.get(i);

			//駒台上の駒の位置をセット
			((KomaImage)(koma.getKomaImage())).setLayoutX(calcLayoutX(i));
			((KomaImage)(koma.getKomaImage())).setLayoutY(calcLayoutY(i));

			this.getChildren().add(((KomaImage)(koma.getKomaImage())));

			//全ての駒に
			((KomaImage)(koma.getKomaImage())).setOnMouseClicked(e -> {
				//clickObserver.komadaiClicked(koma);
			});
		}

		//Canvasを上に持ってくる
		this.getChildren().remove(canvas);
		this.getChildren().add(canvas);
	}

	//FCO -> Player -> GameController -> SelectingState ->
	public void holdKoma(Koma koma) {
		//駒の上にCanvasが来るよう、Paneにセットしなおす
		this.getChildren().remove(canvas);
		this.getChildren().add(canvas);

		//四角形を指定色で描画
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setLineWidth(Constants.HOLDING_RECT_WIDTH);
		g.setStroke(Color.BLUE);
		g.strokeRect(
			Constants.HOLDING_RECT_PADDING + (((KomaImage)(koma.getKomaImage())).getLayoutX()),
			Constants.HOLDING_RECT_PADDING + (((KomaImage)(koma.getKomaImage())).getLayoutY()),
			Constants.HOLDING_RECT_SIZE,
			Constants.HOLDING_RECT_SIZE);
	}

	//駒持ち描画をリセット
	public void clearHolding() {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	}





}
