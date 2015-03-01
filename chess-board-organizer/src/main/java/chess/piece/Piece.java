package chess.piece;

import java.util.List;

/**
 * Defines a chess piece with methods and attributes common to all pieces to be placed on a chess board 
 * @author Willie
 *
 */
public abstract class Piece {
	/**
	 * The list of capture position for the piece and the 
	 * current capture position coordinate
	 */
	protected List<Coordinate> capturePositions;
	/**
	 * The capture position coordinate used to calculate the capture positions list
	 */
	protected Coordinate capturePositionCoordenate;
	/**
	 * Returns the list of the slots where the piece can move on a chess board based on the 
	 * piece type, current piece position and the size of the chess board
	 * @param position the position of the piece on the board
	 * @param size the size of the chess board <code>(m x n-->(m,n))</code>
	 * @return the list of the positions in coordinates where the piece may move, an empty list
	 * in case there are no capture slots
	 */
	public final List<Coordinate> getCaptureSlotsPositions(Coordinate position, Coordinate size)
	{
		if(capturePositions== null || 
				(capturePositionCoordenate!=null && !capturePositionCoordenate.equals(position)))
		{
			capturePositions= calculateCapturePositions(position, size);
			capturePositionCoordenate=position;
		}
		return capturePositions;
	}
	
	/**
	 * Calculates the list with the coordinates (positions) where the piece can move on a chess board based on the 
	 * piece type, the current piece position and the size of the chess board
	 * @param position the position of the piece on the board
	 * @param size the size of the chess board <code>(m x n-->(m,n))</code>
	 * @return the list of the positions in coordinates where the piece may move, an empty list
	 * in case there are no capture slots
	 */
	protected  abstract List<Coordinate> calculateCapturePositions(Coordinate position, Coordinate size);
	
	/**
	 * Returns the abbreviated piece name for example:<br/>
	 * <li>King --> K
	 * <li> Knight --> N
	 * @return the abbreviated piece name
	 */
	public abstract String getAbbreviatedName();
}
