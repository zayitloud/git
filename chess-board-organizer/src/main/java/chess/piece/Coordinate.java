package chess.piece;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a coordinate <code>(x,y)</code> to indicate a position
 * @author 30032751
 *
 */
public class Coordinate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7980218538079990943L;
	private int x;
	private int y;

	/**
	 * Constructor that takes as parameter a coordinate represented by a string which must comply
	 * with the following structure: <code>x;y</code> 
	 * @param xy the coordinate with the  structure <code>x;y</code>
	 */
	public Coordinate(String xy)
	{
		if(StringUtils.isBlank(xy))
		{
			throw new RuntimeException("Invalid coordinate");
		}
		String[] coord=xy.split(";");
		if(coord.length!=2)
		{
			throw new RuntimeException("Invalid coordinate");
		}else{
			try{
				this.x = Integer.parseInt(coord[0]);
				this.y=Integer.parseInt(coord[1]);
			}catch (NumberFormatException e)
			{
				throw new RuntimeException("Invalid coordinate");
			}
		}
		
	}
	/**
	 * Constructor where the x and y coordinates must be provided
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Coordinate(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return String.valueOf(x)+ ";" + String.valueOf(y);
	}
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Coordinate)
		{
			return ((Coordinate)obj).getX()==getX() && ((Coordinate)obj).getY()==getY();
		}else{
			return false;
		}
	}
	
	
}
