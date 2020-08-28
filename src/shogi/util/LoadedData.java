package shogi.util;

import java.util.List;

import shogi.koma.Koma;

public class LoadedData {

	private final List<Koma> sente;
	private final List<Koma> gote;
	private final Koma[][] field;

	public LoadedData(List<Koma> sente, List<Koma> gote, Koma[][] field) {
		this.sente = sente;
		this.gote = gote;
		this.field = field;
	}

	public List<Koma> getSenteKomas() {
		return sente;
	}

	public List<Koma> getGoteKomas() {
		return gote;
	}

	public Koma[][] getField() {
		return field;
	}
}
