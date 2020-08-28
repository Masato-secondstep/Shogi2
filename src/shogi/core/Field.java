package shogi.core;

import java.util.LinkedList;
import java.util.List;

import shogi.core.Player.TurnType;
import shogi.graphics.AbstractFieldPane;
import shogi.graphics.FieldClickObserver;
import shogi.koma.Fu;
import shogi.koma.Hisha;
import shogi.koma.Kaku;
import shogi.koma.Kei;
import shogi.koma.Koma;
import shogi.koma.Koma.KomaType;
import shogi.koma.KomaFactory;
import shogi.koma.Kyo;
import shogi.koma.Ou;


public class Field {

	//フィールド縦横配列サイズ
	public static final int FIELD_ARR_X_SIZE = 9;
	public static final int FIELD_ARR_Y_SIZE = 9;

	//フィールドクリック時呼び出し用
	private FieldClickObserver fco;

	//フィールド
	private Koma[][] field = new Koma[FIELD_ARR_X_SIZE][FIELD_ARR_Y_SIZE];

	//抽象フィールドグラフィック
	private AbstractFieldPane fieldPane;

	public Field(Koma[][] field) {
		this.field = field;
	}

	public void setFieldPane(AbstractFieldPane fieldPane) {
		this.fieldPane = fieldPane;
	}

	//コンストラクタ
	//public Field() {
	//}

	public List<Koma> createAllKoma(){
		KomaFactory kf = new KomaFactory();

		List<Koma> allKoma = kf.createAllKoma();

		for(Koma aKoma : allKoma) {
			field[aKoma.getFieldX()][aKoma.getFieldY()] = aKoma;
		}

		return allKoma;
	}

	//ゲーム開始時処理（クリックを受け付けるようにする）
	public void gameStart() {
		this.setFieldClickable(true);
	}

	//フィールドをクリック可能にする
	private void setFieldClickable(boolean clickable) {
		fieldPane.setOnMouseClicked(clickable, fco);
	}

	//指定された場所に駒を置けるかどうかを判定
	public boolean isAbleToPutKoma(Koma.KomaType komaType, int x, int y) {

		//既に駒が置いてある場所には置けない
		if(field[x][y] != null) return false;

		return	false;

	}

	//指定された駒を指定された位置に移動する
	//移動先に駒が存在する場合、取得して返す
	public Koma moveKoma(Koma selectedKoma, int x, int y) {
		//移動先の駒を取得
		Koma acquiredKoma = getKoma(x, y);

		//取った駒がある場合
		if(acquiredKoma != null) {
			//フィールド上からとった駒を消す
			fieldPane.remove(acquiredKoma.getKomaImage());
		}

		//移動前の位置を覚えておく
		int beforeX = selectedKoma.getFieldX();
		int beforeY = selectedKoma.getFieldY();

		//フィールド上を移動させる
		field[x][y] = selectedKoma;
		field[beforeX][beforeY] = null;

		//駒の位置情報を更新する
		selectedKoma.setFieldX(x);
		selectedKoma.setFieldY(y);

		//取った駒を返す
		return acquiredKoma;
	}

	//指定された駒を指定された位置に移動する
	//駒台からの移動
	public void moveFromKomadaiKoma(Koma selectedKoma, int x, int y) {
		//フィールド上を移動させる
		field[x][y] = selectedKoma;

		//駒の位置情報を更新する
		selectedKoma.setFieldX(x);
		selectedKoma.setFieldY(y);

		//フィールド上に取った駒を表示する
		fieldPane.add(selectedKoma.getKomaImage());
	}

	//getMovablePositionFromKomaから呼び出して使用する
	//香・飛・角に使用する
	//x方向のStep、y方向のStepを指定して検索し、移動不可能なマスを除外する
	private void removeDirection(
			List<Position> removeList, Koma koma, int xStep, int yStep ){

		int x = koma.getFieldX() + xStep;
		int y = koma.getFieldY() + yStep;

		//除外フラグ
		boolean except = false;

		//フィールド端まで探索
		for(; x >= 0 && y >= 0 && x < FIELD_ARR_X_SIZE && y < FIELD_ARR_Y_SIZE;
				x += xStep, y += yStep) {
			if(except) {
				//既に除外状態の場合、そのまま端までずっと除外対象
				removeList.add(new Position(x, y));
			} else {
				if(getKoma(x, y) != null) {
					if(getKoma(x, y).getTurnType() == koma.getTurnType()) {
						//自分の駒の場合は、その時点から除外
						removeList.add(new Position(x, y));
						except = true;
					} else {
						//相手の駒の場合は、その先の駒から除外
						except = true;
					}
				}
			}
		}
	}

	//移動可能マスのうち、移動不可能マスを除外する
	public List<Position> getMovablePositionsFromKoma(Koma koma, boolean avoidOute) {

		List<Position> movablePosition = koma.getMovablePositionsWithoutConsiderField();

		List<Position> removeList = new LinkedList<Position>();

		//香・飛・角の場合のみ、自分の駒・相手の駒の先に行けない
		//香
		if(koma instanceof Kyo) {

			//SENTEならyマイナス方向、GOTEならyプラス方向に検索
			removeDirection(removeList, koma, 0, koma.getTurnType() == TurnType.SENTE ? -1 : 1);

		}

		//飛車
		if(koma instanceof Hisha) {
			removeDirection(removeList, koma,  0, -1);
			removeDirection(removeList, koma,  0,  1);
			removeDirection(removeList, koma, -1,  0);
			removeDirection(removeList, koma,  1,  0);
		}

		//角
		if(koma instanceof Kaku) {
			removeDirection(removeList, koma,  -1, -1);
			removeDirection(removeList, koma,  -1,  1);
			removeDirection(removeList, koma,   1, -1);
			removeDirection(removeList, koma,   1,  1);
		}

		//自分の駒が存在する場所を移動先から除外する
		for(Position aPos : movablePosition) {
			if(getKoma(aPos) != null
				&& getKoma(aPos).getTurnType() == koma.getTurnType() ) {
				removeList.add(aPos);
			}
		}

		//除外リストに存在する移動先を、移動先リストから除外する
		movablePosition.removeAll(removeList);

		//移動後、王手放置になる動きを除外する
		//GameStateからの呼び出し：avoidOute==true（王手放置を回避できる位置を取得）
		//再起呼び出し：avoidOute==false（王手放置の考慮は必要なし）
		if(avoidOute) {
			removeOuteHouchiPositions(koma, movablePosition, false);
		}

		return movablePosition;
	}

	//持ち駒を置ける場所候補一覧
	public List<Position> getPlaceablePosition(Koma koma) {
		List<Position> placeablePosition = new LinkedList<Position>();

		//行きどころのない駒は打てないため除外
		if(koma instanceof Kei) {
			switch(koma.getTurnType()) {
				case SENTE -> {
					for(int y = FIELD_ARR_Y_SIZE - 1; y >= 2; y--) {
						for(int x = 0; x < FIELD_ARR_X_SIZE; x++) {
							if(field[x][y] == null) placeablePosition.add(new Position(x, y));
						}
					}
				}
				case GOTE -> {
					for(int y = 0; y < FIELD_ARR_Y_SIZE - 2; y++) {
						for(int x = 0; x < FIELD_ARR_X_SIZE; x++) {
							if(field[x][y] == null) placeablePosition.add(new Position(x, y));
						}
					}
				}
			}
		} else if(koma instanceof Kyo) {
			switch(koma.getTurnType()) {
				case SENTE -> {
					for(int y = FIELD_ARR_Y_SIZE - 1; y >= 1; y--) {
						for(int x = 0; x < FIELD_ARR_X_SIZE; x++) {
							if(field[x][y] == null) placeablePosition.add(new Position(x, y));
						}
					}
				}
				case GOTE -> {
					for(int y = 0; y < FIELD_ARR_Y_SIZE - 1; y++) {
						for(int x = 0; x < FIELD_ARR_X_SIZE; x++) {
							if(field[x][y] == null) placeablePosition.add(new Position(x, y));
						}
					}
				}
			}
		} else if(koma instanceof Fu) {

			List<Integer> nifuColList = new LinkedList<Integer>();

			switch(koma.getTurnType()) {
				case SENTE -> {
					for(int x = 0; x < FIELD_ARR_X_SIZE; x++) {
						for(int y = FIELD_ARR_Y_SIZE - 1; y >= 1; y--) {
							if(field[x][y] == null) placeablePosition.add(new Position(x, y));
							if(field[x][y] != null && field[x][y].getKomaType() == KomaType.FU
								&& field[x][y].getTurnType() == koma.getTurnType()
								&& !field[x][y].isNari()) {
								System.out.println("二歩COL:" + x);
								nifuColList.add(x);
							}
						}
					}
				}
				case GOTE -> {
					for(int x = 0; x < FIELD_ARR_X_SIZE; x++) {
						for(int y = 0; y < FIELD_ARR_Y_SIZE - 1; y++) {
							if(field[x][y] == null) placeablePosition.add(new Position(x, y));
							if(field[x][y] != null && field[x][y].getKomaType() == KomaType.FU
								&& field[x][y].getTurnType() == koma.getTurnType()
								&& !field[x][y].isNari()) {
								System.out.println("二歩COL:" + x);

								nifuColList.add(x);
							}
						}
					}
				}
			}

			//二歩除外
			for(int x = 0; x < FIELD_ARR_X_SIZE; x++) {
				for(int y = 0; y < FIELD_ARR_Y_SIZE; y++) {
					Position pos = new Position(x, y);
					if(nifuColList.contains(x) && placeablePosition.contains(pos)) {
						placeablePosition.remove(pos);
					}
				}
			}
		} else  {

			//歩・桂・香以外
			for(int y = 0; y < FIELD_ARR_Y_SIZE; y++) {
				for(int x = 0; x < FIELD_ARR_X_SIZE; x++) {
					if(field[x][y] == null) placeablePosition.add(new Position(x, y));
				}
			}
		}

		//駒を打った後、王手放置になる所は除外する
		removeOuteHouchiPositions(koma, placeablePosition, true);

		return placeablePosition;
	}

	private void removeOuteHouchiPositions(Koma koma, List<Position> movableList, boolean fromKomadai) {

		//王手放置チェック
		List<Position> removeList = new LinkedList<Position>();

		for(Position aPos : movableList) {
			System.out.println(koma + "の置き場所候補：" + aPos + "に設置した場合");

			int komaOrgX = -1;
			int komaOrgY = -1;
			if(!fromKomadai) {
				//元の値を取っておく
				komaOrgX = koma.getFieldX();
				komaOrgY = koma.getFieldY();

				//一時的に駒の位置を候補ポジションに動かす
				koma.setFieldX(aPos.getX());
				koma.setFieldY(aPos.getY());
				field[komaOrgX][komaOrgY] = null;
			}
			//一時的に駒の位置を候補ポジションに動かす
			Koma tempKoma = field[aPos.getX()][aPos.getY()];
			field[aPos.getX()][aPos.getY()] = koma;

			//相手のプレイヤーのすべての以降可能位置をリスト化する
			List<Position> opPlayerAllMovablePosition = new LinkedList<Position>();

			//相手プレイヤーのすべての駒を取得
			for(Koma anOpKoma : getPlayerKomas(TurnType.oppositePlayer(koma.getTurnType()))){

				System.out.println(anOpKoma + "の利き：");

				//相手プレイヤーのすべての駒に対して、全ての移動可能マスを表示
				for(Position anOpPos : getMovablePositionsFromKoma(anOpKoma, false)){
					System.out.println(anOpPos);
					//リストに追加
					opPlayerAllMovablePosition.add(anOpPos);
				}
			}

			//自プレイヤーの王を取得
			Koma ou = null;
			for(Koma aKoma : getPlayerKomas(koma.getTurnType())) {
				if(aKoma instanceof Ou) ou = aKoma;
			}
			System.out.println("自玉：" + ou);

			for(Position anOpPos : opPlayerAllMovablePosition) {
				if(anOpPos.equals(ou.getPosition())) {
					System.out.println("王手放置：" + anOpPos);
					removeList.add(aPos);
				}
			}

			if(!fromKomadai) {
				//駒の位置をもとに戻す
				koma.setFieldX(komaOrgX);
				koma.setFieldY(komaOrgY);
				field[komaOrgX][komaOrgY] = koma;
			}
			//駒の位置をもとに戻す
			field[aPos.getX()][aPos.getY()] = tempKoma;

		}

		//王手放置ポジションを、移動先リストから除外する
		movableList.removeAll(removeList);
	}


	public List<Koma> getPlayerKomas(TurnType turnType){
		List<Koma> ret = new LinkedList<Koma>();

		for(Koma[] aRow : field) {
			for(Koma aKoma : aRow) {
				if(aKoma != null && aKoma.getTurnType() == turnType) {
					ret.add(aKoma);
				}
			}
		}

		return ret;
	}

	//指定Positionの駒を取得
	private Koma getKoma(Position position) {
		return field[position.getX()][position.getY()];
	}

	//x,yで指定した位置の駒を取得
	public Koma getKoma(int x, int y) {
		return field[x][y];
	}

	//指定した駒が盤上に存在するか調べる
	/*	private boolean isKomaExistOnField(Koma koma) {
			boolean ret = false;

			if(koma == null) return false;

			for(Koma[] aRow : field) {
				for(Koma aCell : aRow) {
					if(koma.equals(aCell)) ret = true;
				}
			}

			return ret;
		}*/

	/*	public FieldClickObserver getClickObserver() {
			return fco;
		}*/

	public AbstractFieldPane getAbstractFieldPane() {
		return fieldPane;
	}



	//移動可能ポジションを消去
	public void clearMovablePosition() {
		fieldPane.clearMovablePosition();

	}

	//駒選択表示
	public void drawKomaHolding(Position position) {
		fieldPane.drawKomaHolding(position);
	}

	//移動可能位置表示
	public void drawMovablePosition(List<Position> candidates) {
		fieldPane.drawMovablePosition(candidates);
	}

	public void setFieldClickObserver(FieldClickObserver fco) {
		this.fco = fco;
	}




}
