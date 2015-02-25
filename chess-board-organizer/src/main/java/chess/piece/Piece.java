package chess.piece;

import java.util.List;

/**
 * Define the chess piece methods common to all pieces to be placed on a chess board 
 * @author Willie
 *
 */
public interface Piece {
	//Dimensions and position
	/**
	 * Returns a list of the slots where the piece can move on a chess board based on the 
	 * current piece position and the size of the chess board
	 * @param position the position of the piece on the board
	 * @param size the size of the chess board <code>(m x n-->(m,n))</code>
	 * @return the list of the positions in coordinates where the piece may move, en empty list
	 * in case there are no capture slots
	 */
	List<Coordinate> getCaptureSlotsPositions(Coordinate position, Coordinate size);
}
