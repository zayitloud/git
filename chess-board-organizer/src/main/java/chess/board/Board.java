package chess.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	 * Initializes a chess board as a matrix of size <code>m x n</code> passed in as parameters
	 * @param m number of horizontal lines
	 * @param n number of vertical lines
	 */
	public Board(int m, int n)
	{
		Slot slot;
		String key;
		occupiedSlotsMap=new HashMap<String, Slot>();
		availableSlotsList= new ArrayList<Slot>();
		for(int i=1; i<=m; i++)
		{
			for(int j=1; j<=n;j++)
			{
				slot=new Slot(new Coordinate(i, j),null);
				key=String.valueOf(i) + String.valueOf(j);
				availableSlotsList.add(slot);
				slots.put(key,slot);
			}
		}
	}
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
	 * To avoid converting the availableSlotsMap to a list every time this 
	 * list needs to be accessed
	 */
	private List<Slot> availableSlotsList;
	
	/**
	 * All the board slots (available or not)<br/>
	 * the String key is based on the concatenation of the coordinate mn 
	 */
	private Map<String,Slot> slots;
	
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
	 * @return the List of slots available for a give class name, null if the list is empty
	 */
	public List<Slot> getOccupiedSlots(String clasz)
	{
		if(occupiedSlotsByPieceType!=null)
		{
			return (List<Slot>) occupiedSlotsByPieceType.get(clasz).values();
		}
		return null;
	}
	
	/**
	 * Returns the list of occupied slots 
	 * @return the list of occupied slots
	 */
	public List<Slot> getOccupiedSlots()
	{
		return availableSlotsList;
	}
}
