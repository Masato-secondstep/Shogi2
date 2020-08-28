package shogi.koma;

import java.util.LinkedList;
import java.util.List;

import shogi.core.Player.TurnType;
import shogi.core.Position;

public class Fu extends Koma {

	Fu(TurnType turnType, boolean nari, int x, int y) {
		super(turnType, KomaType.FU , nari, x, y);
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
					if(super.isValidPosition(currentX - 1, currentY - 1)) ret.add(new Position(currentX - 1, currentY - 1));
					if(super.isValidPosition(currentX    , currentY - 1)) ret.add(new Position(currentX    , currentY - 1));
					if(super.isValidPosition(currentX + 1, currentY - 1)) ret.add(new Position(currentX + 1, currentY - 1));
					if(super.isValidPosition(currentX - 1, currentY    )) ret.add(new Position(currentX - 1, currentY    ));
					if(super.isValidPosition(currentX + 1, currentY    )) ret.add(new Position(currentX + 1, currentY    ));
					if(super.isValidPosition(currentX    , currentY + 1)) ret.add(new Position(currentX    , currentY + 1));
				}
				case GOTE -> {
					if(super.isValidPosition(currentX - 1, currentY + 1)) ret.add(new Position(currentX - 1, currentY + 1));
					if(super.isValidPosition(currentX    , currentY + 1)) ret.add(new Position(currentX    , currentY + 1));
					if(super.isValidPosition(currentX + 1, currentY + 1)) ret.add(new Position(currentX + 1, currentY + 1));
					if(super.isValidPosition(currentX - 1, currentY    )) ret.add(new Position(currentX - 1, currentY    ));
					if(super.isValidPosition(currentX + 1, currentY    )) ret.add(new Position(currentX + 1, currentY    ));
					if(super.isValidPosition(currentX    , currentY - 1)) ret.add(new Position(currentX    , currentY - 1));
				}
			}
		} else {
			//成っていないとき
			switch(getTurnType()) {
				case SENTE -> {
					if(super.isValidPosition(currentX, currentY - 1)) ret.add(new Position(currentX, currentY - 1));
				}
				case GOTE -> {
					if(super.isValidPosition(currentX, currentY + 1)) ret.add(new Position(currentX, currentY + 1));
				}
			}
		}

		return ret;
	}

}
