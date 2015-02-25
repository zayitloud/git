package chess.board;

import java.util.HashMap;
import java.util.Map;

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
	 * Stores the occupied slots in the board for a given Piece type<br>
	 * From left to right, the first string contains the qualified class name<br/>
	 * the second string contains the concatenated coordinate xy
	 */
	private Map<String,Map<String,Slot>> occupiedSlotsByPieceType;
	/**
	 * Stores the occupied slots in the board <br>
	 * The string part of the Map stores the concatenated coordinate xy
	 * 
	 */
	private Map<String,Slot> occupiedSlotsMap;
	
	/**
	 * A map containing the coordinates of the slots availables to place a piece in the board
	 * the key is a concatenated string of the actual coordinate in the form xy
	 */
	private Map<String,Slot> availableSlots;
	
	/**
	 * All the board slots (available or not)<br/>
	 * the String key is based on the concatenation of the coordinate mn 
	 */
	private Map<String,Slot> slots;
	
	/**
	 * Initializes a chess board as a matrix of size <code>m x n</code> passed in as parameters
	 * @param m number of horizontal lines
	 * @param n number of vertical lines
	 */
	public Board(int m, int n)
	{
		Slot slot;
		String key;
		occupiedSlotsMap=new HashMap<String, Slot>();
		availableSlots= new HashMap<String, Slot>();
		for(int i=1; i<=m; i++)
		{
			for(int j=1; j<=n;j++)
			{
				slot=new Slot(new Coordinate(i, j),null);
				key=String.valueOf(i) + String.valueOf(j);
				availableSlots.put(key,slot);
				slots.put(key,slot);
			}
		}
	}
	
	/**
	 * Initializes a chess board as a matrix of size <code>m x n</code> passed in as parameters but it also takes a
	 * map which contains coordinates (key represented as xy) that will be unavailable to place pieces on this board
	 * 
	 * @param m number of horizontal lines
	 * @param n number of vertical lines
	 * @param occupiedSlots coordinates and slots where pieces may not be placed
	 */
	public Board(int m, int n, Map<String,Slot> occupiedSlots)
	{
		this(m,n);
		occupiedSlots.putAll(occupiedSlots);
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
		occupiedSlotsMap.put(strCoordinate,slot);
		occupiedSlotsByPieceType.get(slot.getPiece().getClass().getName()).put(strCoordinate,slot);
	}

	/**
	 * Returns the list of available slots for the given class qualified name
	 * @param clasz the class qualified name
	 * @return the map containing the coordinate and the slots available for a give class name, null if the map  is empty
	 */
	public Map<String, Slot> getOccupiedSlots(String clasz)
	{
		if(occupiedSlotsByPieceType!=null)
		{
			return occupiedSlotsByPieceType.get(clasz);
		}
		return null;
	}
	
	/**
	 * Returns the list of occupied slots 
	 * @return a map containing as a key a string with the coordinate of the slot (in the form <code>xy</code>) and the actual slot
	 */
	public Map<String, Slot> getOccupiedSlots()
	{
		return occupiedSlotsMap;
	}
	
	/**
	 * Returns a map containing the coordinates and the slots available to place a piece<br/>
	 * the coordinate is a concatenated string of the form xy
	 * @return the map containing the available slots
	 */
	public Map<String, Slot> getAvailableSlots()
	{
		return availableSlots;
	}
}
