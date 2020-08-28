package shogi.koma;

import java.util.LinkedList;
import java.util.List;

import shogi.core.Field;
import shogi.core.Player.TurnType;
import shogi.core.Position;

public class Kyo extends Koma {

	Kyo(TurnType turnType, boolean nari, int x, int y) {
		super(turnType, KomaType.KYO, nari, x, y);
	}

	@Override
	public List<Position> getMovablePositionsWithoutConsiderField(){

		List<Position> ret = new LinkedList<Position>();

		int currentX = super.getFieldX();
		int currentY = super.getFieldY();

		if(isNari()) {
			//成っているとき
			switch(getTurnType()) {
				case SENTE -> {
					if(isValidPosition(currentX - 1, currentY - 1)) ret.add(new Position(currentX - 1, currentY - 1));
					if(isValidPosition(currentX    , currentY - 1)) ret.add(new Position(currentX    , currentY - 1));
					if(isValidPosition(currentX + 1, currentY - 1)) ret.add(new Position(currentX + 1, currentY - 1));
					if(isValidPosition(currentX - 1, currentY    )) ret.add(new Position(currentX - 1, currentY    ));
					if(isValidPosition(currentX + 1, currentY    )) ret.add(new Position(currentX + 1, currentY    ));
					if(isValidPosition(currentX    , currentY + 1)) ret.add(new Position(currentX    , currentY + 1));
				}
				case GOTE -> {
					if(isValidPosition(currentX - 1, currentY + 1)) ret.add(new Position(currentX - 1, currentY + 1));
					if(isValidPosition(currentX    , currentY + 1)) ret.add(new Position(currentX    , currentY + 1));
					if(isValidPosition(currentX + 1, currentY + 1)) ret.add(new Position(currentX + 1, currentY + 1));
					if(isValidPosition(currentX - 1, currentY    )) ret.add(new Position(currentX - 1, currentY    ));
					if(isValidPosition(currentX + 1, currentY    )) ret.add(new Position(currentX + 1, currentY    ));
					if(isValidPosition(currentX    , currentY - 1)) ret.add(new Position(currentX    , currentY - 1));
				}
			}
		} else {
			//成っていないとき
			switch(getTurnType()) {
				case SENTE -> {
					for(int y = currentY - 1; y >= 0; y--) {
						if(isValidPosition(currentX, y)) ret.add(new Position(currentX, y));
					}
				}
				case GOTE -> {
					for(int y = currentY + 1; y < Field.FIELD_ARR_Y_SIZE; y++) {
						if(isValidPosition(currentX, y)) ret.add(new Position(currentX, y));
					}
				}
			}
		}

		return ret;
	}

}
