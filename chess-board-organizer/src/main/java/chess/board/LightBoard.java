package chess.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chess.piece.Coordinate;
import chess.piece.Piece;

public class LightBoard {
	/**
	 * Stores the occupied slots in the board <br>
	 * The string part of the Map stores the concatenated coordinate x;y
	 * 
	 */
	private Map<String,String> occupiedSlotsMap;
	
	private List<String> availableCoordinatesList;
	
	public List<String> getAvailableCoordinatesList() {
		return availableCoordinatesList;
	}

	private Logger log = LoggerFactory.getLogger(LightBoard.class);
	
	private Coordinate size;
	
	public Coordinate getSize() {
		return size;
	}

	/**
	 * Initializes a chess board as a matrix of size <code>m x n</code> passed in as parameters
	 * @param m number of horizontal lines
	 * @param n number of vertical lines
	 */
	public LightBoard(int m, int n)
	{
		size=new Coordinate(m, n);
		String key;
		occupiedSlotsMap=new HashMap<String, String>();
		availableCoordinatesList = new ArrayList<String>();
		for(int i=1; i<=m; i++)
		{
			for(int j=1; j<=n;j++)
			{
				key=String.valueOf(i) + ";" + String.valueOf(j);
				availableCoordinatesList.add(key.intern());
			}
		}
	}
	
	/**
	 * Adds a piece in the corresponding coordinate
	 * @param piece
	 * @param coordinate
	 */
	public void addPiece(Piece piece, Coordinate coordinate)
	{
		String strCoordinate = coordinate.toString();
		occupiedSlotsMap.put(strCoordinate.intern(),piece.getAbbreviatedName());
		availableCoordinatesList.remove(strCoordinate);
	}
	
	/**
	 * Returns the list of occupied slots 
	 * @return a map containing as a key a string with the coordinate of the slot (in the form <code>x;y</code>) and the actual slot
	 */
	public Map<String, String> getOccupiedSlots()
	{
		return occupiedSlotsMap;
	}
	
	/**
	 * Prints the board through the log output
	 */
	public void print()
	{
		String coordinate;
		String slot;
		StringBuffer strLine= new StringBuffer();
		for(int i=1; i<=size.getX(); i++)
		{
			strLine= new StringBuffer();
			strLine.append("|");
			for(int j=1; j<=size.getY(); j++)
			{
				coordinate=i+";"+j;
				slot=occupiedSlotsMap.get(coordinate);
				strLine.append((!StringUtils.isBlank(slot)  ? slot : " ") + "|");
			}
			log.info(strLine.toString()); 
		}
	}	
}
