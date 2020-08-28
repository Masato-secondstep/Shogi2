package application;

import javafx.scene.paint.Color;

public class Constants {

	//駒台
	public static final double KOMADAI_WIDTH = 280;
	public static final double KOMADAI_HEIGHT = 400;
	public static final double KOMADAI_LAYOUT_X_SENTE = 920;
	public static final double KOMADAI_LAYOUT_Y_SENTE = 240;
	public static final double KOMADAI_LAYOUT_X_GOTE = 10;
	public static final double KOMADAI_LAYOUT_Y_GOTE = 30;

	//駒画像切り取りサイズ
	public static final double ORG_KOMA_WIDTH = 43;
	public static final double ORG_KOMA_HEIGHT = 48;

	//駒拡大倍率
	public static final double IMAGE_MAGNIFICATION = 1.25;

	//1駒サイズ
	public static final double IMAGE_WIDTH = 54 * IMAGE_MAGNIFICATION;
	public static final double IMAGE_HEIGHT = 54 * IMAGE_MAGNIFICATION;

	//駒台の行列数
	public static final int KOMADAI_COLS = (int)(KOMADAI_WIDTH / IMAGE_WIDTH);
	public static final int KOMADAI_ROWS = (int)(KOMADAI_HEIGHT / IMAGE_HEIGHT);

	//盤位置
	public static final double FIELD_OFFSET_X = KOMADAI_LAYOUT_X_GOTE + KOMADAI_WIDTH + 13;
	public static final double FIELD_OFFSET_Y = KOMADAI_LAYOUT_Y_GOTE;



	//画像ファイル名
	public static final String IMG_FILENAME = "ryokored.bmp";

	//読込元駒画像サイズ
	public static final double KOMA_BMP_WIDTH = 344;
	public static final double KOMA_BPM_HEIGHT = 288;



	//線カラー
	public static final Color BG_LINE_COLOR = Color.BLACK;





	//盤面・駒台背景色
	public static final int BG_COLOR_R = 255;
	public static final int BG_COLOR_G = 209;
	public static final int BG_COLOR_B = 122;

	//駒をつかむ（クリック位置の駒を囲む）
	public static final Color HOLDING_RECT_COLOR = Color.BLUE;
	public static final double HOLDING_RECT_WIDTH = 3;
	public static final double HOLDING_RECT_SIZE = 44 * IMAGE_MAGNIFICATION;
	public static final double HOLDING_RECT_PADDING =  (IMAGE_WIDTH - HOLDING_RECT_SIZE) / 2.0;

	//駒の動ける場所表示
	public static final Color MOVABLE_RECT_COLOR = Color.GREEN;
	public static final double MOVABLE_RECT_WIDTH = 3;
	public static final double MOVABLE_RECT_SIZE = 44 * IMAGE_MAGNIFICATION;
	public static final double MOVABLE_RECT_PADDING = (IMAGE_WIDTH - MOVABLE_RECT_SIZE) / 2.0;


}
