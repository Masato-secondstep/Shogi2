package shogi.graphics;

import java.util.List;

import shogi.koma.Koma;

public interface AbstractKomadaiPane {

	public void refresh(List<Koma> mochigoma);

	public void holdKoma(Koma koma);

	public void clearHolding();

}
