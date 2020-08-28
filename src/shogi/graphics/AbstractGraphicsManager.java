package shogi.graphics;

import java.util.List;

import shogi.core.Field;
import shogi.core.Player;
import shogi.koma.Koma;

public interface AbstractGraphicsManager {

	public AbstractDialog getAbstractDialog();
	public void addField(Field field);
	public void addPlayer(Player player);
	public void addAllKoma(List<Koma> allKoma);

}
