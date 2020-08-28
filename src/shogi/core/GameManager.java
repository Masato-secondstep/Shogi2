package shogi.core;

import java.util.List;

import shogi.core.Player.TurnType;
import shogi.graphics.AbstractGraphicsManager;
import shogi.graphics.FieldClickObserver;
import shogi.koma.Koma;
import shogi.state.GameState;
import shogi.state.GameState.NextAction;
import shogi.state.GameStateCommon;
import shogi.state.MovingState;
import shogi.state.SelectingState;
import shogi.util.LoadedData;
import shogi.util.ShogiProperty;

public class GameManager implements FieldClickObserver{

	//Singleton
	private static final GameManager instance = new GameManager();

	//フィールド
	private Field field;

	//先手・後手プレイヤー
	private static Player sente;
	private static Player gote;

	//ゲームの状態
	private GameState state;

	//駒選択待ち状態
	private GameState selectingState;

	//駒選択後、移動先待ち状態
	private GameState movingState;

	//グラフィック管理
	private AbstractGraphicsManager gm;

	private GameManager() {
	}

	//Singleton
	public static GameManager getInstance() {
		return instance;
	}

	public void gameStart(AbstractGraphicsManager gm) {
		this.gm = gm;

		ShogiProperty prop = new ShogiProperty();
		LoadedData ld = prop.load("Custom1");

		this.field = new Field(ld.getField());
		this.field.setFieldClickObserver((FieldClickObserver)this);
		gm.addField(field);

		//List<Koma> allKoma = field.createAllKoma();
		gm.addAllKoma(field.getPlayerKomas(TurnType.SENTE));
		gm.addAllKoma(field.getPlayerKomas(TurnType.GOTE));

		//先手・後手　駒生成
		sente = new Player(Player.TurnType.SENTE, ld.getSenteKomas(), (FieldClickObserver)this);
		gote = new Player(Player.TurnType.GOTE, ld.getGoteKomas(), (FieldClickObserver)this);
		gm.addAllKoma(ld.getSenteKomas());
		gm.addAllKoma(ld.getGoteKomas());
		gm.addPlayer(sente);
		gm.addPlayer(gote);

		//State間共通使用クラス
		GameStateCommon gsc = GameStateCommon.getInstance(this, field);

		//先手番の設定
		selectingState = new SelectingState(gsc, Player.TurnType.SENTE);
		movingState = new MovingState(gsc, Player.TurnType.SENTE);

		//先手番の駒選択からスタート
		state = selectingState;
		field.gameStart();

	}

	@Override
	public void fieldClicked(int x, int y) {

		//x,y位置をクリックした結果のNextActionを取得
		NextAction na = state.play(x, y);

		switch(na) {
			case SAMEPLAYER_SELECTING -> {

				//移動→選択
				state = selectingState;
			}
			case SAMEPLAYER_MOVING -> {

				//選択→移動
				state = movingState;
			}

			case NEXTPLAYER_SELECTING -> {

				//次のプレーヤーへ
				state.switchTurnPlayer();

				//移動→選択
				state = selectingState;
			}
			case TSUMI -> {
				System.out.println("詰みです");
			}
		}

		System.out.println(state);
	}

	@Override
	public void komadaiClicked(Koma koma) {

		System.out.println("KOMADAI CLICKED");

		//駒台の駒をクリックした結果のNextActionを取得
		NextAction na = state.selectKomadai(koma);

		switch(na) {
			case SAMEPLAYER_SELECTING -> {

				//移動→選択
				state = selectingState;
			}
			case SAMEPLAYER_MOVING -> {

				//選択→移動
				state = movingState;
			}
		}
	}

	//turnTypeに応じたPlayerを返す
	public Player getPlayer(TurnType turnType) {

		switch(turnType) {
			case SENTE -> {
				return sente;
			}
			case GOTE -> {
				return gote;
			}
		}
		return null;
	}

	public AbstractGraphicsManager getAbstractGraphicsManager() {
		return gm;
	}

	public Field getField() {
		return field;
	}

	public boolean judgeTsumi(TurnType turnType) {
		System.out.println("詰みチェック");
		Player turnPlayer = getPlayer(turnType);

		List<Koma> turnPlayerMochiKomas = turnPlayer.getMochiKoma();
		for(Koma aKoma : turnPlayerMochiKomas) {


			//1箇所でも動ける場所があれば、詰んでいない
			if(field.getPlaceablePosition(aKoma).size() > 0) {
				System.out.println("持ち駒" + aKoma + ":" + field.getPlaceablePosition(aKoma).size() + "箇所");
				return false;
			}

		}

		List<Koma> turnPlayerFieldKomas = field.getPlayerKomas(turnType);
		for(Koma aKoma : turnPlayerFieldKomas) {

			//1箇所でも動ける場所があれば、詰んでいない
			if(field.getMovablePositionsFromKoma(aKoma, true).size() > 0) {
				System.out.println("移動" + aKoma + ":" + field.getMovablePositionsFromKoma(aKoma, true).size() + "箇所");
				return false;
			}
		}
		return true;
	}

}
