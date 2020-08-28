package shogi.state;

import java.util.List;

import shogi.core.Field;
import shogi.core.GameManager;
import shogi.core.Player;
import shogi.core.Position;
import shogi.koma.Koma;

//ゲームステート共通使用クラス
public class GameStateCommon{

	//Singleton
	private static GameStateCommon instance;
	private GameStateCommon(GameManager gm, Field field) {
		this.gm = gm;
		this.field = field;
	}
	public static GameStateCommon getInstance(GameManager gm, Field field) {
		if(instance == null) {
			instance = new GameStateCommon(gm, field);
		}
		return instance;
	}

	//現在プレイ中のプレイヤーのタイプ（先手／後手）
	private Player.TurnType turnType;

	//ゲーム管理者
	private GameManager gm;

	//フィールド
	private Field field;

	//駒置き場所候補キャンバス（四角形描画用）
	private List<Position> movablePositions;

	//選択中の駒
	private Koma selectedKoma = null;

	//駒台の駒を選択しているかどうか
	private boolean fromKomadai = false;

	//駒台getter,setter
	public boolean isFromKomadai() {
		return fromKomadai;
	}
	public void setFromKomadai(boolean fromKomadai) {
		this.fromKomadai = fromKomadai;
	}

	//置き場所候補getter,setter
	public List<Position> getMovablePositions() {
		return movablePositions;
	}
	public void setMovablePositions(List<Position> candidates) {
		this.movablePositions = candidates;
	}

	//選択中の駒getter,setter
	public Koma getSelectedKoma() {
		return selectedKoma;
	}
	public void setSelectedKoma(Koma selectedKoma) {
		this.selectedKoma = selectedKoma;
	}

	//現在プレイ中のプレイヤーのタイプ（先手／後手）getter,setter
	public Player.TurnType getTurnType() {
		return turnType;
	}
	public void setTurnType(Player.TurnType turnPlayer) {
		this.turnType = turnPlayer;
	}

	//Field getter
	public Field getField() {
		return field;
	}

	//GameManager getter
	public GameManager getGameManager() {
		return gm;
	}

}
