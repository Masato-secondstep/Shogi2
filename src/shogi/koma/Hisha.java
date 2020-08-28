package shogi.koma;

import java.util.LinkedList;
import java.util.List;

import shogi.core.Field;
import shogi.core.Player.TurnType;
import shogi.core.Position;

public class Hisha extends Koma {

	Hisha(TurnType turnType, boolean nari, int x, int y) {
		super(turnType, KomaType.HISHA, nari, x, y);
	}

	@Override
	public List<Position> getMovablePositionsWithoutConsiderField(){

		List<Position> ret = new LinkedList<Position>();

		int currentX = super.getFieldX();
		int currentY = super.getFieldY();

		for(int x = currentX - 1; x >= 0; x--) {
			if(isValidPosition( x, currentY)) ret.add(new Position(x, currentY));
		}

		for(int x = currentX + 1; x < Field.FIELD_ARR_X_SIZE; x++) {
			if(isValidPosition(x, currentY)) ret.add(new Position(x, currentY));
		}

		for(int y = currentY - 1; y >= 0; y--) {
			if(isValidPosition(currentX, y)) ret.add(new Position(currentX, y));
		}

		for(int y = currentY + 1; y < Field.FIELD_ARR_Y_SIZE; y++) {
			if(isValidPosition(currentX, y)) ret.add(new Position(currentX, y));
		}

		if(isNari()) {
			//成っているとき
			if(isValidPosition(currentX - 1, currentY - 1)) ret.add(new Position(currentX - 1, currentY - 1));
			if(isValidPosition(currentX - 1, currentY + 1)) ret.add(new Position(currentX - 1, currentY + 1));
			if(isValidPosition(currentX + 1, currentY - 1)) ret.add(new Position(currentX + 1, currentY - 1));
			if(isValidPosition(currentX + 1, currentY + 1)) ret.add(new Position(currentX + 1, currentY + 1));
		}

		return ret;
	}

}
