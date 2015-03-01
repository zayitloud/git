package chess.piece;

import java.util.ArrayList;
import java.util.List;
/**
 * This class represents the Queen on a chess game
 * @author Willie
 *
 */
public class Queen implements Piece {

	@Override
	public List<Coordinate> getCaptureSlotsPositions(Coordinate position,
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
