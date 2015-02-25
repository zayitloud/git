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
		availableSlotsMap=new HashMap<String, Slot>();
		availableSlotsList= new ArrayList<Slot>();
		for(int i=1; i<=m; i++)
		{
			for(int j=1; j<=n;j++)
			{
				slot=new Slot(new Coordinate(i, j),null);
				availableSlotsMap.put(String.valueOf(i) + String.valueOf(j), slot);
				availableSlotsList.add(slot);
			}
		}
	}
	/**
	 * Stores all the available slots in the board for a given Piece type<br>
	 * The concatenated part of the Map contains the qualified class name
	 */
	private Map<String,List<Slot>> availableSlotsByPieceType;
	/**
	 * Stores all the available slots in the board <br>
	 * The string part of the Map stores the concatenated coordinate xy
	 * 
	 */
	private Map<String,Slot> availableSlotsMap;
	
	/**
	 * To avoid converting the availableSlotsMap to a list every time this 
	 * list needs to be accessed
	 */
	private List<Slot> availableSlotsList;
	
	public void addPiece(Piece piece, Coordinate coordinate)
	{
		
	}
	
	/**
	 * Returns the list of available slots for the given class qualified name
	 * @param clasz the class qualified name
	 * @return the List of slots available for a give class name, null if the list is empty
	 */
	public List<Slot> getAvailableSlots(String clasz)
	{
		if(availableSlotsByPieceType!=null)
		{
			return availableSlotsByPieceType.get(clasz);
		}
		return null;
	}
	
	/**
	 * Returns the list of available slots without taking into account 
	 * the Piece type
	 * @return 
	 */
	public List<Slot> getAvailableSlots()
	{
		return availableSlotsList;
	}
}
