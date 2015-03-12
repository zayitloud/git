package chess.piece;

import java.util.ArrayList;
import java.util.List;
/**
 * This class represents the Queen on a chess game
 * @author Willie
 *
 */
public class Queen extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3960786082296972901L;

	@Override
	public List<Coordinate> calculateCapturePositions(Coordinate position,
			Coordinate size) {
		List<Coordinate> coordinates= new ArrayList<Coordinate>();
		coordinates.addAll(new Rook().getCaptureSlotsPositions(position, size));
		coordinates.addAll(new Bishop().getCaptureSlotsPositions(position, size));
		return coordinates;
	}

	@Override
	public String getAbbreviatedName() {
		return "Q";
	}

}
