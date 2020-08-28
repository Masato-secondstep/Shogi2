package shogi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import shogi.core.Player.TurnType;
import shogi.koma.Koma;
import shogi.koma.Koma.KomaType;
import shogi.koma.KomaFactory;

public class ShogiProperty extends Properties{

	public LoadedData load(String fileName) {
		try {
			super.load(new FileInputStream(new File(
					getClass().getResource(fileName).getFile())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Koma[][] field = new Koma[9][9];
		for(int y = 0; y < 9; y++) {
			System.out.println(this.getProperty("field" + y));
			char[] aRow = this.getProperty("field" + y).toCharArray();
			for(int x = 0; x < aRow.length; x++) {
				field[x][y] = charToKoma(aRow[x], x, y);
			}
		}

		List<Koma> sente = new LinkedList<Koma>();
		String senteStr = this.getProperty("sente");
		if(senteStr != null) {
			for(char c : senteStr.toCharArray()) {
				sente.add(charToKoma(c, -1, -1));
			}
		}


		List<Koma> gote = new LinkedList<Koma>();
		String goteStr = this.getProperty("gote");
		if(goteStr != null) {
			for(char c : goteStr.toCharArray()) {
				gote.add(charToKoma(c, -1, -1));
			}
		}

		System.out.println(this.getProperty("sente"));
		System.out.println(this.getProperty("gote"));

		return new LoadedData(sente, gote, field);
	}

	private Koma charToKoma(char c, int x, int y) {

		if(c == '0') return null;

		KomaType komaType = null;

		//文字に応じたKomaTypeを判定
		switch(c) {
			case 'a', 'A', 'm', 'M' -> komaType = KomaType.FU;
			case 'b', 'B', 'n', 'N' -> komaType = KomaType.KYO;
			case 'c', 'C', 'o', 'O' -> komaType = KomaType.KEI;
			case 'd', 'D', 'p', 'P' -> komaType = KomaType.GIN;
			case 'e'     , 'q'      -> komaType = KomaType.KIN;
			case 'f', 'F', 'r', 'R' -> komaType = KomaType.KAKU;
			case 'g', 'G', 's', 'S' -> komaType = KomaType.HISHA;
			case 'h'     , 't'      -> komaType = KomaType.OU;
		}

		TurnType turnType = Character.compare('k', Character.toLowerCase(c)) > 0 ? TurnType.SENTE : TurnType.GOTE;

		return new KomaFactory().createKoma(turnType, komaType, Character.isUpperCase(c), x, y);
	}
}
