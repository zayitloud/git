package chess.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	 * Stores the occupied slots in the board for a given Piece type<br>
	 * From left to right, the first string contains the qualified class name<br/>
	 * the second string contains the concatenated coordinate x;y
	 */
	private Map<String,Map<String,Slot>> occupiedSlotsByPieceType;
	/**
	 * Stores the occupied slots in the board <br>
	 * The string part of the Map stores the concatenated coordinate x;y
	 * 
	 */
	private Map<String,Slot> occupiedSlotsMap;
	
	/**
	 * A map containing the coordinates of the slots available to place a piece in the board
	 * the key is a concatenated string of the actual coordinate in the form x;y
	 */
	private Map<String,Slot> availableSlots;
	
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
		availableSlots= new HashMap<String, Slot>();
		occupiedSlotsByPieceType= new HashMap<String,Map<String,Slot>>();
		slots=new HashMap<String, Slot>();
		availableCoordinatesList = new ArrayList<String>();
		for(int i=1; i<=m; i++)
		{
			for(int j=1; j<=n;j++)
			{
				slot=new Slot(new Coordinate(i, j),null);
				key=String.valueOf(i) + ";" + String.valueOf(j);
				availableSlots.put(key,slot);
				slots.put(key,slot);
				availableCoordinatesList.add(key);
			}
		}
	}
	
	/**
	 * Initializes a chess board as a matrix of size <code>m x n</code> passed in as parameters but it also takes a
	 * map which contains coordinates (key represented as x;y) that will be unavailable to place pieces on this board
	 * 
	 * @param m number of horizontal lines
	 * @param n number of vertical lines
	 * @param occupiedSlotsByPieceType the slots occupied per piece type (this is useful for previous executions) 
	 */
	public Board(int m, int n, Map<String,Map<String,Slot>> occupiedSlotsByPieceType)
	{
		this(m,n);
		this.occupiedSlotsByPieceType.putAll(occupiedSlotsByPieceType);
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
		if(!occupiedSlotsByPieceType.containsKey(slot.getPiece().getClass().getName()))
		{
			occupiedSlotsByPieceType.put(slot.getPiece().getClass().getName(), new HashMap<String,Slot>());
		}
		occupiedSlotsByPieceType.get(slot.getPiece().getClass().getName()).put(strCoordinate,slot);
		availableSlots.remove(strCoordinate);
		availableCoordinatesList.remove(strCoordinate);
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
	 * @return a map containing as a key a string with the coordinate of the slot (in the form <code>x;y</code>) and the actual slot
	 */
	public Map<String, Slot> getOccupiedSlots()
	{
		return occupiedSlotsMap;
	}
	
	/**
	 * Returns a map containing the coordinates and the slots available to place a piece<br/>
	 * the coordinate is a concatenated string of the form x;y
	 * @return the map containing the available slots
	 */
	public Map<String, Slot> getAvailableSlots()
	{
		return availableSlots;
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

	/**
	 * Prints on the output log the values of the control maps used by the class to place pieces on the board<br/>
	 * it also calls the method <code>print()</code>
	 * 
	 */
	public void printInternalValues()
	{
		print();
		log.info("Printing availableSlots");
		printMap(availableSlots);
		log.info("Printing occupiedSlotsMap");
		printMap(occupiedSlotsMap);
		Set<String> pieceTypes=occupiedSlotsByPieceType.keySet();
		if(pieceTypes.size()>0)
		{
			log.info("Printing occupiedSlotsByPieceType");
			for(String pieceType:pieceTypes)
			{
				log.info("Printing " + pieceType);
				printMap(occupiedSlotsByPieceType.get(pieceType));
			}
		}
		
		
	}
	
	/**
	 * Utility method to print on the output log the contents of a map by extracting the keys
	 * and the printing the value associated with that key 
	 * @param map the map to be printed on the logs
	 */
	private void printMap(Map<String,Slot> map)
	{
		Set<String> keys=map.keySet();
		Slot slot;
		for(String key: keys)
		{
			slot=map.get(key);
			log.info(key + (!slot.isAvailable() ? slot.getPiece().getAbbreviatedName() : " "));
		}
		
	}

	public Map<String, Map<String, Slot>> getOccupiedSlotsByPieceType() {
		return occupiedSlotsByPieceType;
	}
	
	
}
