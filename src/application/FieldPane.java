package application;

import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import shogi.core.Field;
import shogi.core.Position;
import shogi.graphics.AbstractFieldPane;
import shogi.graphics.AbstractKomaImage;
import shogi.graphics.FieldClickObserver;

public class FieldPane extends Pane implements AbstractFieldPane {

	//Singleton
	private static FieldPane instance = new FieldPane();
	public static FieldPane getInstance() { return instance; }

	private Canvas movableCanvas;

	private FieldPane() {

		//フィールド縦横サイズ設定
		this.setPrefWidth(Constants.IMAGE_WIDTH * Field.FIELD_ARR_X_SIZE);
		this.setPrefHeight(Constants.IMAGE_HEIGHT * Field.FIELD_ARR_Y_SIZE);

		//レイアウトオフセット
		this.setLayoutX(Constants.FIELD_OFFSET_X);
		this.setLayoutY(Constants.FIELD_OFFSET_Y);

		//背景色の設定
		this.setBackground(new Background(new BackgroundFill(
				Color.rgb(Constants.BG_COLOR_R,
						Constants.BG_COLOR_G,
						Constants.BG_COLOR_B),
				null, null)));
		;

		//格子線の描画
		Canvas canvas = new Canvas(Constants.IMAGE_WIDTH * Field.FIELD_ARR_X_SIZE,
				Constants.IMAGE_HEIGHT * Field.FIELD_ARR_Y_SIZE);
		this.getChildren().add(canvas);
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setStroke(Constants.BG_LINE_COLOR);
		g.setLineWidth(2);
		for (int i = 0; i < Field.FIELD_ARR_X_SIZE + 1; i++) {
			g.strokeLine(i * Constants.IMAGE_WIDTH,
					0,
					i * Constants.IMAGE_WIDTH,
					Constants.IMAGE_WIDTH * (Field.FIELD_ARR_X_SIZE + 1));
			g.strokeLine(0,
					i * Constants.IMAGE_HEIGHT,
					Constants.IMAGE_HEIGHT * (Field.FIELD_ARR_Y_SIZE + 1),
					i * Constants.IMAGE_HEIGHT);
		}

		movableCanvas = new Canvas(Constants.IMAGE_WIDTH * Field.FIELD_ARR_X_SIZE,
				Constants.IMAGE_HEIGHT * Field.FIELD_ARR_Y_SIZE);

	}

	public static Color getBackGroundColor() {
		return Color.rgb(Constants.BG_COLOR_R,
				Constants.BG_COLOR_G,
				Constants.BG_COLOR_B);
	}

	public void setOnMouseClicked(boolean clickable, FieldClickObserver co) {
		if(clickable) {
		this.setOnMouseClicked(e -> {
			int x = (int)(e.getX() / Constants.IMAGE_WIDTH);
			int y = (int)(e.getY() / Constants.IMAGE_HEIGHT);
			co.fieldClicked(x, y);
		});
		} else {
			instance.setOnMouseClicked(e -> { });
		}
	}

	//指定位置の駒をつかむ（四角で囲む）
	public void drawKomaHolding(Position position) {

		//movableCanvasを一番上に持ってくる
		this.getChildren().remove(movableCanvas);
		this.getChildren().add(movableCanvas);

		//四角形を指定色で描画
		GraphicsContext g = movableCanvas.getGraphicsContext2D();
		g.setLineWidth(Constants.HOLDING_RECT_WIDTH);
		g.setStroke(Constants.HOLDING_RECT_COLOR);
		g.strokeRect(
				Constants.HOLDING_RECT_PADDING + (Constants.IMAGE_WIDTH * position.getX()),
				Constants.HOLDING_RECT_PADDING + (Constants.IMAGE_WIDTH * position.getY()),
				Constants.HOLDING_RECT_SIZE,
				Constants.HOLDING_RECT_SIZE);

		System.out.println("holding x:" + position.getX() + " y:" + position.getY());
	}

	//移動可能位置をmovableCanvasに描画
	public void drawMovablePosition(List<Position> posList) {
		if (posList == null)
			return;

		//movableCanvasを一番上に持ってくる
		this.getChildren().remove(movableCanvas);
		this.getChildren().add(movableCanvas);

		for (Position aPos : posList) {

			//四角形を指定色で描画
			GraphicsContext g = movableCanvas.getGraphicsContext2D();
			g.setLineWidth(Constants.MOVABLE_RECT_WIDTH);
			g.setStroke(Constants.MOVABLE_RECT_COLOR);
			g.strokeRect(
					Constants.MOVABLE_RECT_PADDING + (Constants.IMAGE_WIDTH * aPos.getX()),
					Constants.MOVABLE_RECT_PADDING + (Constants.IMAGE_WIDTH * aPos.getY()),
					Constants.MOVABLE_RECT_SIZE,
					Constants.MOVABLE_RECT_SIZE);
		}
	}

	//movableCanvasのクリア
	public void clearMovablePosition() {
		movableCanvas.getGraphicsContext2D().clearRect(0, 0, movableCanvas.getWidth(), movableCanvas.getHeight());
	}

	@Override
	public void add(AbstractKomaImage komaImage) {
		this.getChildren().add((KomaImage)komaImage);
	}

	@Override
	public void remove(AbstractKomaImage komaImage) {
		this.getChildren().remove((KomaImage)komaImage);
	}

	@Override
	public Object getFieldPane() {
		return this;
	}

}
