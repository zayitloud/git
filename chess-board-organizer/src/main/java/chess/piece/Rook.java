package chess.piece;

import java.util.ArrayList;
import java.util.List;

public class Rook implements Piece {

	@Override
	public List<Coordinate> getCaptureSlotsPositions(Coordinate position,
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
