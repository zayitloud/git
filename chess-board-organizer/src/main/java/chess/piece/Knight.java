package chess.piece;

import java.util.ArrayList;
import java.util.List;
/**
 * This class represents a Knight on chess game
 * @author Willie
 *
 */
public class Knight extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4689956977945566840L;

	@Override
	public List<Coordinate> calculateCapturePositions(Coordinate position,
			Coordinate size) {
		List<Coordinate> coordinates= new ArrayList<Coordinate>();
		for(int x=position.getX()-2; x<=position.getX()+2; x+=2)
		{
			if(x!=position.getX() && x>=1 && x<=size.getX())
			{
				if(position.getY()>1 && position.getY()<size.getY())
				{
					coordinates.add(new Coordinate(x,position.getY()-1));
					coordinates.add(new Coordinate(x,position.getY()+1));
				}else
				if(position.getY()==1)
				{
					coordinates.add(new Coordinate(x,position.getY()+1));
				}else
				if(position.getY()==size.getY())
				{
					coordinates.add(new Coordinate(x,position.getY()-1));
				}
			}
		}		
		for(int y=position.getY()-2; y<=position.getY()+2; y+=2)
		{
			if(y!=position.getY() && y>=1 && y<=size.getY())
			{
				if(position.getX()>1 && position.getX()<size.getX())
				{
					coordinates.add(new Coordinate(position.getX()-1,y));
					coordinates.add(new Coordinate(position.getX()+1,y));
				}else
				if(position.getX()==1)
				{
					coordinates.add(new Coordinate(position.getX()+1,y));
				}else
				if(position.getX()==size.getX())
				{
					coordinates.add(new Coordinate(position.getX()-1,y));
				}
			}
		}		
		return coordinates;
	}

	@Override
	public String getAbbreviatedName() {
		return "N";
	}

}
