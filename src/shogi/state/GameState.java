package shogi.state;

import java.util.List;

import shogi.core.Player;
import shogi.core.Player.TurnType;
import shogi.core.Position;
import shogi.koma.Koma;

public abstract class GameState {

	public enum NextAction{
		DO_NOTHING,
		NEXTPLAYER_SELECTING,
		SAMEPLAYER_SELECTING,
		SAMEPLAYER_MOVING,
		TSUMI,
	}

	//フィールドクリック
	public abstract NextAction play(int x, int y);

	//駒台クリック
	public abstract NextAction selectKomadai(Koma koma);

	//ステート間共通使用クラス
	private GameStateCommon gsc;

	//先行をセットして初期化
	public GameState(GameStateCommon gsc, TurnType turnPlayer) {
		this.gsc = gsc;
		gsc.setTurnType(turnPlayer);
	}

	//GameStateCommonの取得
	public GameStateCommon getGameStateCommon() {
		return gsc;
	}

	//プレイヤーチェンジ
	public void switchTurnPlayer() {
		gsc.setTurnType(Player.TurnType.oppositePlayer(gsc.getTurnType()));
	}

	//つかめることが確定後に使用すること
	//フィールド上の駒をつかむ
	protected void holdKoma(int x, int y) {

		//駒を取得
		Koma koma = gsc.getField().getKoma(x, y);

		//選択中の駒に設定
		gsc.setSelectedKoma(koma);

		//置き場所候補をクリア
		gsc.getField().clearMovablePosition();

		//駒をつかむ（クリック位置の駒を四角で囲む）
		gsc.getField().drawKomaHolding(new Position(x, y));

		//クリック位置の駒の移動可能位置を取得
		//List<Position> movablePosition = gsc.getSelectedKoma().getMovablePosition(true);//, x, y);
		List<Position> movablePositions = gsc.getField().getMovablePositionsFromKoma(koma, true);
		gsc.setMovablePositions(movablePositions);

		//フィールド上の駒を選択（駒台の選択ではない）
		gsc.setFromKomadai(false);

		//移動可能場所を表示
		gsc.getField().drawMovablePosition(gsc.getMovablePositions());
	}

	//駒台の駒をつかむ
	public void holdKomadaiKoma(Koma koma) {
		//選択中の駒に設定
		gsc.setSelectedKoma(koma);

		//置き場所候補をクリア
		gsc.getField().clearMovablePosition();

		//駒台の駒をつかむ
		gsc.getGameManager().getPlayer(koma.getTurnType()).holdKomadaiKoma(koma);

		//駒の配置可能位置を取得
		List<Position> placeable = gsc.getField().getPlaceablePosition(koma);
		gsc.setMovablePositions(placeable);

		//駒台の駒を選択
		gsc.setFromKomadai(true);

		//移動可能場所を表示
		gsc.getField().drawMovablePosition(gsc.getMovablePositions());
	}

	@Override
	public String toString() {
		return gsc.getTurnType().toString();
	}
}
