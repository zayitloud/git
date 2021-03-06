package chess.piece;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Rook on a chess game
 * @author Willie
 *
 */
public class Rook extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1180417715473658852L;

	@Override
	public List<Coordinate> calculateCapturePositions(Coordinate position,
			Coordinate size) {
		List<Coordinate> coordinates= new ArrayList<Coordinate>();
		for(int x=1; x<=size.getX(); x++)
		{
			if(x!=position.getX())
			{
				coordinates.add(new Coordinate(x,position.getY()));
			}
		}
		for(int y=1; y<=size.getY(); y++)
		{
			if(y!=position.getY())
			{
				coordinates.add(new Coordinate(position.getX(),y));
			}
		}
		
		return coordinates;
	}

	@Override
	public String getAbbreviatedName() {
		return "R";
	}

}
