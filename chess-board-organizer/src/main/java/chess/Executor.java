package chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chess.board.Board;
import chess.board.Slot;
import chess.piece.Coordinate;
import chess.piece.King;
import chess.piece.Piece;
import chess.piece.Rook;

/**
 * This class is the executor of the main application logic which is allocating
 * the resources necessary to find all possible Pieces combination on a chess board
 * @author Willie 
 *
 */
public class Executor {

	private int maxNumberOfRetries;
	
	/**
	 * The container of pieces to be placed in the board
	 */
	private List<Piece> pool;
	
	/**
	 * To store the boards configuration per run
	 */
	private List<Board> boards;
	
	private Coordinate boardSize;
	
	Logger log= LoggerFactory.getLogger(Executor.class);
	
	public Executor()
	{
		pool = new ArrayList<Piece>();
		pool.add(new King());
		pool.add(new King());
		pool.add(new Rook());
		boardSize=new Coordinate("3;3");
		maxNumberOfRetries=10;
		boards=new ArrayList<Board>();
		
	}
	
	public void start()
	{
		int successCount=0;
		Map<String, Slot> occupiedSlots;
		List<String> availableSlots;
		//to avoid checking a slot for a specific piece more than once
		Map<String, Slot> occupiedSlotsPerPiece;
		List<Coordinate> captureSlots;
		boolean captureSlotsAreFree=true;
		Board board, previousBoard=null;
		for(int retry=1; retry<=maxNumberOfRetries; retry++)
		{

			board = new Board(boardSize.getX(),boardSize.getY());
			occupiedSlots = board.getOccupiedSlots();
			availableSlots = board.getAvailableCoordinatesList();
			captureSlotsAreFree=true;
			for(Piece piece: pool)
			{
				occupiedSlotsPerPiece=board.getOccupiedSlots(piece.getClass().getName());
				Collections.shuffle(availableSlots);
				for(String coordinate: availableSlots)
				{
					Coordinate proposedPosition = new Coordinate(coordinate);
					//if the available coordinate has been occupied before by the same type of piece
					//for example, on a previous execution then ignore that coordinate
//					if(occupiedSlotsPerPiece!=null && occupiedSlotsPerPiece.containsKey(coordinate))
					if(matchPreviousCombinationOfCoordinates(
							piece.getClass().getName(), 
							occupiedSlotsPerPiece,
							coordinate))
					{
						continue;
					}
					//get the slots the piece can move to to check if they are available
					//if any of the slots is occupied by another piece then the available slot cannot be used by this piece type
					captureSlots = piece.getCaptureSlotsPositions(proposedPosition, boardSize);
					for(Coordinate captureSlotCoordinate: captureSlots)
					{
						if(occupiedSlots.containsKey(captureSlotCoordinate.toString()))
						{
							captureSlotsAreFree=false;
							break;
						}
					}
					if(captureSlotsAreFree)
					{
						board.addPiece(piece, proposedPosition);
						break;
					}
					captureSlotsAreFree=true;
						
				}
			}
			log.info("*************Retry " + retry);
			if(occupiedSlots.size()==pool.size())
			{
				successCount++;
				board.print();
				boards.add(board);
			}else{
				log.info("No combination has been found, trying again");
			}
			previousBoard=board;
		}
		log.info("Success combinations found: " + successCount);
		log.info("Number of calculations: " + maxNumberOfRetries);
		
	}
	
	/**
	 * Checks whether the current arrangement of positions (in coordinates) matches any of the arrangements of previous boards<br/>
	 * To achieve this every position  on the current arrangements is looked up in the previous boards arrangements.
	 * @param pieceType 
	 * @param currentCombination the current pieces positions
	 * @param newPosition the last position being added to the current positions
	 * @return true if the arrangement matches any of the previous boards arrangements 
	 */
	private boolean matchPreviousCombinationOfCoordinates(String pieceType, Map<String,Slot> currentCombination, String newPosition)
	{
		if(currentCombination==null)
		{
			return false;
		}
		Map<String, Slot> previousCombination;
		boolean matchesCombination=true;
		//the slot position for a given piece type is compared with 
		//slot positions on previous boards to avoid repeating positions
		for(Board board:boards)
		{
			matchesCombination=true;
			previousCombination = board.getOccupiedSlots(pieceType);
			//if the combinations are not the same size we still don't know
			if(currentCombination.size()+1==previousCombination.size())
			{
				for(String coordinates: currentCombination.keySet())
				{
					if(!previousCombination.containsKey(coordinates))
					{
						//if the previous positions does not contain any of the  positions
						//it is not the same combination
						matchesCombination=false;
						break;
					}
				}
				if(matchesCombination)
				{
					if(previousCombination.containsKey(newPosition))
					{
						return true;
					}
				}
			}
			
		}
		return false;
	}
	
	private boolean areCaptureSlotsFree(Piece piece, Coordinate boardSize, Coordinate proposedPosition, Map<String,Slot> occupiedSlots)
	{
		List<Coordinate> captureSlots = piece.getCaptureSlotsPositions(proposedPosition, boardSize);
		for(Coordinate captureSlotCoordinate: captureSlots)
		{
			if(occupiedSlots.containsKey(captureSlotCoordinate.toString()))
			{
				return false;
			}
		}
		return true;
	}
	
	public static void main(String args[])
	{
		new Executor().start();
	}
	
}
