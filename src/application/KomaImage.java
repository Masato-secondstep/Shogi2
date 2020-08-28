package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import shogi.core.Player;
import shogi.core.Player.TurnType;
import shogi.graphics.AbstractKomaImage;
import shogi.graphics.KomaClickObserver;
import shogi.koma.Koma;

public class KomaImage extends ImageView implements AbstractKomaImage{

	private Player.TurnType turnType;
	private Koma.KomaType komaType;
	private boolean nari;
	//private int x;
	//private int y;
	//private KomaImageClickObserver kico;
	//private Pane masterPane;
	//private Canvas selectedRect = new Canvas(Constants.IMAGE_WIDTH * Field.FIELD_ARR_X_SIZE,
	//		Constants.IMAGE_HEIGHT * Field.FIELD_ARR_Y_SIZE);

	public KomaImage(Player.TurnType turnType, Koma.KomaType komaType,
			boolean nari, int x, int y, KomaClickObserver kico, Pane masterPane) {

		this.turnType = turnType;
		this.komaType = komaType;
		this.nari = nari;
		//this.x = x;
		//this.y = y;
		//this.masterPane = masterPane;

		//駒の場所に応じてX位置、Y位置をセット
		this.setFieldX(x);
		this.setFieldY(y);

		//画像をセット
		this.setImage(new KomaImageManager().getImage(turnType, komaType, nari));

	}

	public void setFieldX(int x) {
		//this.x = x;
		this.setLayoutX(Constants.IMAGE_WIDTH * x + 5);
	}

	public void setFieldY(int y) {
		//this.y = y;
		this.setLayoutY(Constants.IMAGE_HEIGHT * y + 2);
	}

	public void setTurnType(TurnType turnType) {
		this.turnType = turnType;
		this.setImage(new KomaImageManager().getImage(turnType, komaType, nari));
	}

	public void setNari(boolean nari) {
		this.nari = nari;
		this.setImage(new KomaImageManager().getImage(turnType, komaType, nari));
	}

	private static class KomaImageManager {
		//Singleton
		//private static final KomaImageManager instance = new KomaImageManager();

		//画像読み込み
		private Image img = new Image(getClass().getResourceAsStream(Constants.IMG_FILENAME),
				Constants.KOMA_BMP_WIDTH * Constants.IMAGE_MAGNIFICATION,
				Constants.KOMA_BPM_HEIGHT * Constants.IMAGE_MAGNIFICATION, true, true);

		//Singleton
		/*		public static KomaImageManager getInstance() {
					return instance;
				}*/

		public Image getImage(TurnType turnType, Koma.KomaType komaType, boolean nari) {
			return new WritableImage(
					img.getPixelReader(),
					(int) (komaType.getImageOffset() * Constants.ORG_KOMA_WIDTH * Constants.IMAGE_MAGNIFICATION),
					(int) (turnType.getImageOffset() * Constants.ORG_KOMA_HEIGHT * Constants.IMAGE_MAGNIFICATION * 3
							+ (nari ? Constants.ORG_KOMA_HEIGHT * Constants.IMAGE_MAGNIFICATION : 0)),
					(int) (Constants.ORG_KOMA_WIDTH * Constants.IMAGE_MAGNIFICATION),
					(int) (Constants.ORG_KOMA_HEIGHT * Constants.IMAGE_MAGNIFICATION));
		}
	}
}
