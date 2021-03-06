package shogi.koma;

import java.util.LinkedList;
import java.util.List;

import shogi.core.Player.TurnType;
import shogi.core.Position;

public class Ou extends Koma {

	Ou(TurnType turnType, boolean nari, int x, int y) {
		super(turnType, KomaType.OU, nari, x, y);
	}

	@Override
	public List<Position> getMovablePositionsWithoutConsiderField(){
		List<Position> ret = new LinkedList<Position>();

		int currentX = super.getFieldX();
		int currentY = super.getFieldY();

		if(isValidPosition(currentX - 1, currentY - 1)) ret.add(new Position(currentX - 1, currentY - 1));
		if(isValidPosition(currentX    , currentY - 1)) ret.add(new Position(currentX    , currentY - 1));
		if(isValidPosition(currentX + 1, currentY - 1)) ret.add(new Position(currentX + 1, currentY - 1));
		if(isValidPosition(currentX - 1, currentY    )) ret.add(new Position(currentX - 1, currentY    ));
		if(isValidPosition(currentX + 1, currentY    )) ret.add(new Position(currentX + 1, currentY    ));
		if(isValidPosition(currentX - 1, currentY + 1)) ret.add(new Position(currentX - 1, currentY + 1));
		if(isValidPosition(currentX    , currentY + 1)) ret.add(new Position(currentX    , currentY + 1));
		if(isValidPosition(currentX + 1, currentY + 1)) ret.add(new Position(currentX + 1, currentY + 1));

		return ret;
	}

	@Override
	public boolean isNariable() {
		return false;
	}

}
