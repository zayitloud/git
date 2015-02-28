package chess.piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Bishop in a chess game
 * @author Willie
 *
 */
public class Bishop implements Piece {

	@Override
	public List<Coordinate> getCaptureSlotsPositions(Coordinate position,
			Coordinate size) {
		List<Coordinate> coordinates= new ArrayList<Coordinate>();
		int xCoord=position.getX()-1;
		int yCoord=position.getY()-1;
		while(xCoord>=1 && yCoord>=1)
		{
			coordinates.add(new Coordinate(xCoord,yCoord));
			xCoord--;
			yCoord--;
		}
		xCoord=position.getX()+1;
		yCoord=position.getY()-1;
		while(xCoord<=size.getX() && yCoord>=1)
		{
			coordinates.add(new Coordinate(xCoord,yCoord));
			xCoord++;
			yCoord--;
		}
		xCoord=position.getX()-1;
		yCoord=position.getY()+1;
		while(xCoord>=1 && yCoord<=size.getY())
		{
			coordinates.add(new Coordinate(xCoord,yCoord));
			xCoord--;
			yCoord++;
		}
		xCoord=position.getX()+1;
		yCoord=position.getY()+1;
		while(xCoord<=size.getX() && yCoord<=size.getY())
		{
			coordinates.add(new Coordinate(xCoord,yCoord));
			xCoord++;
			yCoord++;
		}
		return coordinates;
	}

	@Override
	public String getAbbreviatedName() {
		return "B";
	}

}
