package chess.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chess.piece.Coordinate;
import chess.piece.Piece;

/**
 * Represents the chess board. Its main function is to store the information of the
 * state of the board at any given time 
 * @author Willie   
 *
 */
public class Board {

	
	/**
	 * Stores the occupied slots in the board <br>
	 * The string part of the Map stores the concatenated coordinate x;y
	 * 
	 */
	private Map<String,Slot> occupiedSlotsMap;
	
	/**
	 * All the board slots (available or not)<br/>
	 * the String key is based on the concatenation of the coordinate m;n 
	 */
	private Map<String,Slot> slots;
	
	private List<String> availableCoordinatesList;
	
	public List<String> getAvailableCoordinatesList() {
		return availableCoordinatesList;
	}

	private Logger log = LoggerFactory.getLogger(Board.class);
	
	private Coordinate size;
	
	public Coordinate getSize() {
		return size;
	}

	/**
	 * Initializes a chess board as a matrix of size <code>m x n</code> passed in as parameters
	 * @param m number of horizontal lines
	 * @param n number of vertical lines
	 */
	public Board(int m, int n)
	{
		size=new Coordinate(m, n);
		Slot slot;
		String key;
		occupiedSlotsMap=new HashMap<String, Slot>();
		slots=new HashMap<String, Slot>();
		availableCoordinatesList = new ArrayList<String>();
		for(int i=1; i<=m; i++)
		{
			for(int j=1; j<=n;j++)
			{
				slot=new Slot(new Coordinate(i, j),null);
				key=String.valueOf(i) + ";" + String.valueOf(j);
				slots.put(key.intern(),slot);
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
		Slot slot=slots.get(coordinate.toString());
		if(slot.isAvailable())
		{
			slot.setPiece(piece);
			addOccupiedSlot(slot);
		}else{
			throw new RuntimeException("Slot on coordinate ("+coordinate.getX()+","+coordinate.getY()+") is already taken");
		}
	}
	
	/**
	 * Adds  the slot passed as parameter to the maps that store occupied Slots in the general occupied map
	 * and the specific Piece type map 
	 * @param slot the slot to be added to the maps of occupied slots
	 */
	private void addOccupiedSlot(Slot slot) {
		String strCoordinate = slot.getCoordinate().toString();
		occupiedSlotsMap.put(strCoordinate.intern(),slot);
		availableCoordinatesList.remove(strCoordinate);
	}

	/**
	 * Returns the list of occupied slots 
	 * @return a map containing as a key a string with the coordinate of the slot (in the form <code>x;y</code>) and the actual slot
	 */
	public Map<String, Slot> getOccupiedSlots()
	{
		return occupiedSlotsMap;
	}
	
	/**
	 * Prints the board through the log output
	 */
	public void print()
	{
		String coordinate;
		Slot slot;
		StringBuffer strLine= new StringBuffer();
		for(int i=1; i<=size.getX(); i++)
		{
			strLine= new StringBuffer();
			strLine.append("|");
			for(int j=1; j<=size.getY(); j++)
			{
				coordinate=i+";"+j;
				slot=slots.get(coordinate);
				strLine.append((!slot.isAvailable() ? slot.getPiece().getAbbreviatedName() : " ") + "|");
			}
			log.info(strLine.toString()); 
		}
	}	
}
