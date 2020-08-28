package shogi.koma;

import java.util.LinkedList;
import java.util.List;

import shogi.core.Player;
import shogi.core.Player.TurnType;

//駒生成用ファクトリ
public class KomaFactory{

	//プレイヤー毎20個の駒を定義
	private Koma.KomaType[] komaTypeArray = {
			Koma.KomaType.FU,
			Koma.KomaType.FU,
			Koma.KomaType.FU,
			Koma.KomaType.FU,
			Koma.KomaType.FU,
			Koma.KomaType.FU,
			Koma.KomaType.FU,
			Koma.KomaType.FU,
			Koma.KomaType.FU,
			Koma.KomaType.KAKU,
			Koma.KomaType.HISHA,
			Koma.KomaType.KYO,
			Koma.KomaType.KEI,
			Koma.KomaType.GIN,
			Koma.KomaType.KIN,
			Koma.KomaType.OU,
			Koma.KomaType.KIN,
			Koma.KomaType.GIN,
			Koma.KomaType.KEI,
			Koma.KomaType.KYO
			};

	//プレイヤー毎20個の駒の初期位置を定義
	private int[][] xPosArray = {{0,1,2,3,4,5,6,7,8,1,7,0,1,2,3,4,5,6,7,8},
								  {0,1,2,3,4,5,6,7,8,7,1,0,1,2,3,4,5,6,7,8}};
	private int[][] yPosArray = {{6,6,6,6,6,6,6,6,6,7,7,8,8,8,8,8,8,8,8,8},
								  {2,2,2,2,2,2,2,2,2,1,1,0,0,0,0,0,0,0,0,0}};

	public List<Koma> createAllKoma() {

		List<Koma> ret = new LinkedList<Koma>();

		ret.addAll(createPlayerKoma(TurnType.SENTE));
		ret.addAll(createPlayerKoma(TurnType.GOTE));

		return ret;
	}

	private List<Koma> createPlayerKoma(TurnType turnType) {
		List<Koma> ret = new LinkedList<Koma>();

		for(int i = 0; i < komaTypeArray.length; i++) {
			ret.add(createKoma(
					turnType,
					komaTypeArray[i],
					false,
					xPosArray[turnType.getIndex()][i],
					yPosArray[turnType.getIndex()][i]//,
					//fco
					));
		}
		return ret;
	}

	//指定位置に駒を生成
	public Koma createKoma(
			Player.TurnType turnType, Koma.KomaType komaType,
			boolean nari, int x, int y) {

		//新たな駒を生成
		Koma koma = switch(komaType) {
			case FU -> new Fu(turnType, nari, x, y);
			case HISHA -> new Hisha(turnType, nari, x, y);
			case KAKU -> new Kaku(turnType, nari, x, y);
			case KYO -> new Kyo(turnType, nari, x, y);
			case KEI -> new Kei(turnType, nari, x, y);
			case GIN -> new Gin(turnType, nari, x, y);
			case KIN -> new Kin(turnType, nari, x, y);
			case OU -> new Ou(turnType, nari, x, y);
			default -> throw new IllegalArgumentException("Unexpected value: " + komaType);
		};

		//フィールドにセット
		//field[x][y] = koma;

		return koma;

	}
}
