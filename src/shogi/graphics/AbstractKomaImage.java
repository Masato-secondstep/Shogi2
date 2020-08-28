package shogi.graphics;

import shogi.core.Player.TurnType;

public interface AbstractKomaImage {

	//shogiパッケージ側から使用するもののみ記載
	//環境依存側はキャストする
	public void setTurnType(TurnType turnType);
	public void setNari(boolean nari);
	public void setFieldX(int x);
	public void setFieldY(int y);

}
