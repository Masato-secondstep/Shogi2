package shogi.graphics;

import shogi.koma.Koma;

//フィールドクリック検知インタフェース
public interface FieldClickObserver {
	public void fieldClicked(int x, int y);
	public void komadaiClicked(Koma koma);
}
