package chess.board;

import chess.piece.Coordinate;
import chess.piece.Piece;

/**
 * This class represents a slot on a chess board.
 * the slot has two states<br/>
 * <li>free
 * <li>occupied: in this case, the slot also contains a reference to the 
 * Piece occupying it<br/>
 * The slot also contains the coordinate where it is located on the chess board
 * @author 30032751
 *
 */
public class Slot {

	private Coordinate coordinate;
	private Piece piece;
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public Piece getPiece() {
		return piece;
	}

	/**
	 * This constructor takes two arguments: the position of the Slot in the chess board and
	 * the piece occupying it
	 * @param coordinate the coordinate representing the position of this slot 
	 * @param piece the piece occupying this slot, null in case the slot is available;
	 */
	public Slot(Coordinate coordinate, Piece piece) {
		super();
		this.coordinate = coordinate;
		this.piece = piece;
	}
	
	public boolean isAvailable(){
		return piece==null;
	}
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	
	
}
