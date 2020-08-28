package shogi.core;

public class Position {

	private int x;
	private int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "x:" + x + " y:" + y;
	}

	@Override
	public boolean equals(Object o) {

		//Positionのインスタンスで、xとyが一致していればTrueを返す
		if(o instanceof Position) {
			Position p  = (Position)o;
			return this.x == p.x && this.y == p.y;
		} else {
			return false;
		}
	}



}
