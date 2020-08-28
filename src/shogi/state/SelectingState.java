package shogi.state;

import shogi.core.Player.TurnType;
import shogi.koma.Koma;

public class SelectingState extends GameState {

	public SelectingState(GameStateCommon gsc, TurnType turnPlayer) {
		super(gsc, turnPlayer);
	}

	@Override
	public NextAction play(int x, int y) {
		Koma koma = getGameStateCommon().getField().getKoma(x, y);

		//クリック位置に駒がなければ終了
		if(koma == null) {
			return NextAction.DO_NOTHING;
		}

		//違うプレイヤーの駒はつかめない
		if(koma.getTurnType() != getGameStateCommon().getTurnType()) {
			System.out.println("違うプレイヤーの駒はつかめません");
			return NextAction.DO_NOTHING;
		}

		//フィールドの駒をつかむ
		super.holdKoma(x, y);

		//次のステートへ
		return NextAction.SAMEPLAYER_MOVING;
	}

	@Override
	public NextAction selectKomadai(Koma koma) {

		//System.out.println("SELECTING: KOMADAI CLICKED");

		//違うプレイヤーの駒はつかめない
		if(koma.getTurnType() != super.getGameStateCommon().getTurnType()) {
			System.out.println("違うプレイヤーの駒台の駒はつかめません");
			return NextAction.DO_NOTHING;
		}

		//駒台の駒をつかむ
		super.holdKomadaiKoma(koma);

		//次のステートへ
		return NextAction.SAMEPLAYER_MOVING;
	}

	@Override
	public String toString() {
		return super.toString() + "選択中";
	}
}
