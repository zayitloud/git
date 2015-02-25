package chess.piece;

/**
 * Represents a coordinate <code>(x,y)</code> to indicate a position
 * @author 30032751
 *
 */
public class Coordinate {

	private int x;
	private int y;

	/**
	 * Constructor where the x and y coordinates must be provided
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Coordinate(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return String.valueOf(x)+String.valueOf(y);
	}
	
	
}
