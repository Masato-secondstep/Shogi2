package shogi.koma;

import java.util.LinkedList;
import java.util.List;

import shogi.core.Field;
import shogi.core.Player.TurnType;
import shogi.core.Position;
import shogi.graphics.FieldClickObserver;

public class Kaku extends Koma {

	public Kaku(TurnType turnType, KomaType komaType, boolean nari, int x, int y, FieldClickObserver co) {
		super(turnType, komaType, nari, x, y, co);
	}

	@Override
	public List<Position> getMovablePositionsWithoutConsiderField(){

		List<Position> ret = new LinkedList<Position>();

		int currentX = super.getFieldX();
		int currentY = super.getFieldY();

		for(int x = currentX - 1, y = currentY - 1; x >= 0 && y >= 0; x--, y--) {
			if(isValidPosition(x, y)) ret.add(new Position(x, y));
		}

		for(int x = currentX - 1, y = currentY + 1; x >= 0 && y < Field.FIELD_ARR_Y_SIZE; x--, y++) {
			if(isValidPosition(x, y)) ret.add(new Position(x, y));
		}

		for(int x = currentX + 1, y = currentY - 1; x < Field.FIELD_ARR_X_SIZE && y >= 0; x++, y--) {
			if(isValidPosition(x, y)) ret.add(new Position(x, y));
		}

		for(int x = currentX + 1, y = currentY + 1; x < Field.FIELD_ARR_X_SIZE && y < Field.FIELD_ARR_Y_SIZE; x++, y++) {
			if(isValidPosition(x, y)) ret.add(new Position(x, y));
		}

		if(isNari()) {
			//成っているとき
			if(isValidPosition(currentX - 1, currentY    )) ret.add(new Position(currentX - 1, currentY    ));
			if(isValidPosition(currentX + 1, currentY    )) ret.add(new Position(currentX + 1, currentY    ));
			if(isValidPosition(currentX    , currentY - 1)) ret.add(new Position(currentX    , currentY - 1));
			if(isValidPosition(currentX    , currentY + 1)) ret.add(new Position(currentX    , currentY + 1));
		}

		return ret;
	}

}
