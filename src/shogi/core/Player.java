package shogi.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import shogi.graphics.AbstractKomadaiPane;
import shogi.graphics.FieldClickObserver;
import shogi.koma.Koma;

public class Player {

	public enum TurnType{
		SENTE("先手", 0, 0), GOTE("後手", 1, 1);

	    private final String str;
	    private final int imgOffset;
	    private final int index;

	    private TurnType(final String str, final int imgOffset, int index) {
	        this.str = str;
	        this.imgOffset = imgOffset;
	        this.index = index;
	    }
		@Override
		public String toString() {
			return str;
		}

		public int getImageOffset() {
			return imgOffset;
		}

		public int getIndex() {
			return index;
		}

		public static TurnType oppositePlayer(TurnType turnPlayer) {
			return turnPlayer == SENTE ? GOTE : SENTE;
		}
	}

	private TurnType turnType;

	//駒台の持ち駒
	private List<Koma> mochigoma;

	//駒台Pane
	private AbstractKomadaiPane abstractKomadaiPane;

	private FieldClickObserver fco;

	public Player(TurnType turnType, FieldClickObserver clickObserver) {
		this.mochigoma = new LinkedList<Koma>();
		this.turnType = turnType;
		this.fco = clickObserver;

		//this.abstractKomadaiPane =
		//		GraphicsManager.createKomadaiPane(turnType, mochigoma, clickObserver);
	}

	//相手の駒を取った時
	public void acquireKoma(Koma koma) {
		//駒の持ち主を変える
		koma.setTurnType(turnType);

		//駒をナラズ状態に戻す
		koma.setNari(false);

		//持ち駒に追加
		this.mochigoma.add(koma);

		//駒をソートする
		Collections.sort(mochigoma, (koma1, koma2) ->
			koma2.getKomaType().getScore() - koma1.getKomaType().getScore());

		//駒台を更新
		abstractKomadaiPane.refresh(mochigoma);
	}

	//駒台の駒を使ったとき
	public void removeKomadaiKoma(Koma koma) {
		//持ち駒から削除
		this.mochigoma.remove(koma);

		//駒台を更新
		abstractKomadaiPane.refresh(mochigoma);
	}

	//駒台の駒をつかむ
	public void holdKomadaiKoma(Koma koma) {
		abstractKomadaiPane.holdKoma(koma);
	}

	//駒台の駒を離す
	public void clearKomadaiHolding() {
		abstractKomadaiPane.clearHolding();
	}

	//フィールド上の自分の駒をすべて取得
	public List<Koma> getMochiKoma(){
		return mochigoma;
	}

	public AbstractKomadaiPane getAbstractKomadaiPane() {
		return abstractKomadaiPane;
	}

	public void setAbstractKomadaiPane(AbstractKomadaiPane abstractKomadaiPane) {
		this.abstractKomadaiPane = abstractKomadaiPane;
	}

	public TurnType getTurnType() {
		return turnType;
	}

	public FieldClickObserver getFieldClickObserver() {
		return fco;
	}
}
