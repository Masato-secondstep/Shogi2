package shogi.state;

import shogi.core.Field;
import shogi.core.Player;
import shogi.core.Player.TurnType;
import shogi.core.Position;
import shogi.koma.Koma;
import shogi.koma.Koma.KomaType;

public class MovingState extends GameState {

	public MovingState(GameStateCommon gsc, TurnType turnPlayer) {
		super(gsc, turnPlayer);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public NextAction play(int x, int y) {
		//GameStateCommonの取得
		GameStateCommon gsc = getGameStateCommon();

		Koma selectedKoma = gsc.getSelectedKoma();

		//駒は絶対に選択中
		assert selectedKoma != null;

		System.out.println(selectedKoma);

		Koma clickedKoma = gsc.getField().getKoma(x, y);

		if(selectedKoma.equals(clickedKoma)) {
			//同じ駒を選択して戻した
			System.out.println("同じ駒を選択して戻した");

			//movablePositionをクリアする
			gsc.getField().clearMovablePosition();

			//駒を選びなおす
			return NextAction.SAMEPLAYER_SELECTING;
		}

		//置き場所候補がnullの時は何もしない
		if(gsc.getMovablePositions() == null) {
			return NextAction.DO_NOTHING;
		}

		//ターンプレイヤーを取得
		Player turnPlayer = gsc.getGameManager().getPlayer(gsc.getTurnType());

		//置き場所候補に存在する場合、移動する
		if(gsc.getMovablePositions().contains(new Position(x, y))) {

			//駒台から置いたかどうか
			if(gsc.isFromKomadai()) {

				//駒台から駒を削除
				turnPlayer.removeKomadaiKoma(selectedKoma);

				//駒台からフィールドに移動
				gsc.getField().moveFromKomadaiKoma(selectedKoma, x, y);

			} else {
				//フィールド上での移動

				//成り可能な場合
				if(selectedKoma.isNariable()) {
					switch(gsc.getTurnType()) {
						case SENTE -> {
							if(selectedKoma.getKomaType() == KomaType.FU
								|| selectedKoma.getKomaType() == KomaType.KYO
								|| selectedKoma.getKomaType() == KomaType.KEI) {

								//歩・香・桂の場合、強制成り判定
								switch(selectedKoma.getKomaType()) {
									case FU, KYO ->{
										//歩・香は1段目(y=0)
										if(y == 0) selectedKoma.setNari(true);
									}
									case KEI -> {
										//桂は1・2段目(y=1～2)
										if(y <= 1) selectedKoma.setNari(true);
									}
								}

							}

							//先手なら3段目以内に入った時または3段目以内から出た時(y=0～2)
							if(!selectedKoma.isNari() && (selectedKoma.getFieldY() < 3 || y < 3)) {
								askNari(gsc);
							}
						}
						case GOTE -> {
							if(selectedKoma.getKomaType() == KomaType.FU
									|| selectedKoma.getKomaType() == KomaType.KYO
									|| selectedKoma.getKomaType() == KomaType.KEI) {

									//歩・香・桂の場合、強制成り判定
									switch(selectedKoma.getKomaType()) {
										case FU, KYO ->{
											//歩・香は9段目(y=8)
											if(y == Field.FIELD_ARR_Y_SIZE - 1) selectedKoma.setNari(true);
										}
										case KEI -> {
											//桂は8・9段目(y=7～8)
											if(y >= Field.FIELD_ARR_Y_SIZE - 2) selectedKoma.setNari(true);
										}
									}

							}

							//後手なら7段目以降に入った時または7段目以内から出た時(y=6～8)
							if(!selectedKoma.isNari() && (selectedKoma.getFieldY() > 5  || y > 5)) {
								askNari(gsc);
							}
						}
					}
				}

				//駒を移動して、移動先の駒を取る
				Koma acquiredKoma = gsc.getField().moveKoma(selectedKoma, x, y);

				//取った駒がある場合
				if(acquiredKoma != null) {

					//ターンプレイヤーに取った駒を追加
					turnPlayer.acquireKoma(acquiredKoma);
				}

			}

			//置き場所候補をクリア
			gsc.getField().clearMovablePosition();

			//ターンプレイヤーの駒台ホールド状態をクリア
			turnPlayer.clearKomadaiHolding();

			//相手プレイヤーの詰み判定
			boolean tsumi = gsc.getGameManager().judgeTsumi(TurnType.oppositePlayer(turnPlayer.getTurnType()));

			if(tsumi) {
				//詰み
				return NextAction.TSUMI;
			} else {
				//次のプレイヤーへ
				return NextAction.NEXTPLAYER_SELECTING;
			}


		} else {

			//同じプレイヤーの別の駒を選択
			if(clickedKoma != null &&
					gsc.getTurnType() == gsc.getField().getKoma(x, y).getTurnType()) {

				//別の駒をつかむ
				super.holdKoma(x, y);
			}

			//引き続きMovingStateのまま
			return NextAction.SAMEPLAYER_MOVING;

		}
	}


	@Override
	public NextAction selectKomadai(Koma koma) {

		System.out.println("SELECTING: KOMADAI CLICKED");

		//MovingState状態で駒台がクリックされた
		//同じ駒クリック→Selectingに戻る
		//それ以外→それをSelectしてHolding

		//GameStateCommonの取得
		GameStateCommon gsc = super.getGameStateCommon();

		//駒は絶対に選択中
		assert gsc.getSelectedKoma() != null;

		//違うプレイヤーの駒はつかめない
		if(koma.getTurnType() != super.getGameStateCommon().getTurnType()) {
			System.out.println("違うプレイヤーの駒台の駒はつかめません");
			return NextAction.DO_NOTHING;
		}

		//同じ駒：Hold解除してSELECTINGに戻る
		if(koma.equals(gsc.getSelectedKoma())) {
			//同じ駒を選択して戻した
			System.out.println("同じ駒を選択して戻した");

			//movablePositionをクリアする
			gsc.getField().clearMovablePosition();

			//駒台をクリアする
			gsc.getGameManager().getPlayer(koma.getTurnType()).clearKomadaiHolding();

			//駒を選びなおす
			return NextAction.SAMEPLAYER_SELECTING;
		}

		//別の駒
		System.out.println("別の駒台の駒を選択した");

		//movablePositionをクリアする
		gsc.getField().clearMovablePosition();

		//駒台をクリアする
		gsc.getGameManager().getPlayer(koma.getTurnType()).clearKomadaiHolding();

		//駒台の駒をつかむ
		super.holdKomadaiKoma(koma);

		//次のステートへ
		return NextAction.SAMEPLAYER_MOVING;

	}

	//成り確認
	private void askNari(GameStateCommon gsc) {

		//成り確認ダイアログを表示
		if(gsc.getGameManager().getAbstractGraphicsManager().getAbstractDialog().ask("成り確認", "成りますか？")) {
			gsc.getSelectedKoma().setNari(true);
		}

	}

	@Override
	public String toString() {
		return super.toString() + "移動中";
	}
}
