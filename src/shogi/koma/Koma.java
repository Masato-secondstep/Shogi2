package shogi.koma;

import java.util.List;

import shogi.core.Field;
import shogi.core.Player.TurnType;
import shogi.core.Position;
import shogi.graphics.AbstractKomaImage;
import shogi.graphics.FieldClickObserver;
import shogi.graphics.KomaClickObserver;

public abstract class Koma implements KomaClickObserver {

	public enum KomaType {
		FU("歩", 7, 1),
		KYO("香", 6, 2),
		KEI("桂", 5, 3),
		GIN("銀", 4, 4),
		KIN("金", 3, 5),
		KAKU("角", 2, 6),
		HISHA("飛", 1,7),
		OU("玉", 0, 8);

		private final String str;
		private final int imgOffset;
		private final int score;

		private KomaType(final String str, int imgOffset, int score) {
			this.str = str;
			this.imgOffset = imgOffset;
			this.score = score;
		}

		@Override
		public String toString() {
			return str;
		}

		public int getImageOffset() {
			return imgOffset;
		}

		public int getScore() {
			return score;
		}
	};

	private KomaType komaType;
	private AbstractKomaImage komaImage;
	private TurnType turnType;
	private boolean nari;
	private Position position;
	private FieldClickObserver co;

	//駒自身の動ける場所リストを取得する(移動不可の場所は考慮しない)
	public abstract List<Position> getMovablePositionsWithoutConsiderField();

	//コンストラクタ
	Koma(TurnType turnType, KomaType komaType, boolean nari, int x, int y) {

		this.turnType = turnType;
		this.komaType = komaType;
		this.nari = nari;
		this.position = new Position(x, y);
		//this.x = x;
		//this.y = y;
		//this.co = co;

	}

	public void setFieldClickObserver(FieldClickObserver co) {
		this.co = co;
	}

	protected boolean isValidPosition(int tryX, int tryY) {

		//x,yが範囲内か判定し、有効範囲内であればtrueを返す
		return tryX >= 0 && tryY >= 0
				&& tryX < Field.FIELD_ARR_X_SIZE
				&& tryY < Field.FIELD_ARR_Y_SIZE;

	}

	//成り可能かどうか
	//王、金はOverrideで常にfalseとする
	public boolean isNariable() {
		return !isNari();
	}

	//駒台　画像クリック時
	@Override
	public void komaImageClicked() {
		co.komadaiClicked(this);
	}

	@Override
	public String toString() {
		return "" + turnType + komaType + (nari ? "成" : "") +
				" x:" + position.getX() + " y: " + position.getY();
	}

	public KomaType getKomaType() {
		return komaType;
	}

	public void setKomaType(KomaType komaType) {
		this.komaType = komaType;
	}

	public AbstractKomaImage getKomaImage() {
		return komaImage;
	}

	public void setKomaImage(AbstractKomaImage komaImage) {
		this.komaImage = komaImage;
	}

	public TurnType getTurnType() {
		return turnType;
	}

	public void setTurnType(TurnType turnType) {
		this.turnType = turnType;
		this.komaImage.setTurnType(turnType);
	}

	public boolean isNari() {
		return nari;
	}

	public void setNari(boolean nari) {
		this.nari = nari;
		this.komaImage.setNari(nari);
	}

	public int getFieldX() {
		return position.getX();
	}

	public void setFieldX(int x) {
		this.position.setX(x);
		this.komaImage.setFieldX(x);
	}

	public int getFieldY() {
		return position.getY();
	}

	public void setFieldY(int y) {
		this.position.setY(y);
		this.komaImage.setFieldY(y);
	}

	public Position getPosition() {
		return this.position;
	}

}
