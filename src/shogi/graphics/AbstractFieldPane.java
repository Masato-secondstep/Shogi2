package shogi.graphics;

import java.util.List;

import shogi.core.Position;

public interface AbstractFieldPane {

	public void setOnMouseClicked(boolean clickable, FieldClickObserver fco);

	public void add(AbstractKomaImage komaImage);
	public void remove(AbstractKomaImage komaImage);

	public void drawKomaHolding(Position position);
	public void drawMovablePosition(List<Position> candidates);
	public void clearMovablePosition();

	public Object getFieldPane();
}
