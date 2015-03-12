package chess.piece;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the King on a chess game
 * @author 30032751
 *
 */
public class King extends Piece {


	/**
	 * 
	 */
	private static final long serialVersionUID = 773299825828296303L;

	@Override
	public List<Coordinate> calculateCapturePositions(Coordinate position,
			Coordinate size) {
		List<Coordinate> coordinates= new ArrayList<Coordinate>();
		for(int x=position.getX()-1; x<=position.getX()+1; x++)
		{
			for(int y=position.getY()-1;y<=position.getY()+1;y++)
			{
				if(x<1 || x>size.getX() ||  
						y<1 || y>size.getY() ||
						(x==position.getX() &&  y==position.getY()))
				{
					continue;
				}
				coordinates.add(new Coordinate(x, y));
			}
		}
		
		return coordinates;
	}

	@Override
	public String getAbbreviatedName() {
		return "K";
	}

}
