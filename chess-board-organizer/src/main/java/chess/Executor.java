package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chess.board.Board;
import chess.board.Slot;
import chess.piece.Bishop;
import chess.piece.Coordinate;
import chess.piece.King;
import chess.piece.Knight;
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
		pool.add(new Knight());
		pool.add(new Knight());
		pool.add(new Bishop());
		pool.add(new Bishop());
		boardSize=new Coordinate("7;7");
		maxNumberOfRetries=40000;
		boards=new ArrayList<Board>();
		
	}
	
	public void start()
	{
		int successCount=0;
		Map<String, Slot> occupiedSlots;
		List<String> availableSlots;
		//to avoid checking a slot for a specific piece more than once
		Map<String, Slot> occupiedSlotsPerPiece;
		Board board;
		for(int retry=1; retry<=maxNumberOfRetries; retry++)
		{

			board = new Board(boardSize.getX(),boardSize.getY());
			occupiedSlots = board.getOccupiedSlots();
			availableSlots = board.getAvailableCoordinatesList();
			for(Piece piece: pool)
			{
				occupiedSlotsPerPiece=board.getOccupiedSlots(piece.getClass().getName());
				Collections.shuffle(availableSlots);
				for(String coordinate: availableSlots)
				{
					Coordinate proposedPosition = new Coordinate(coordinate);
					//if the available coordinate has been occupied before by the same type of piece
					//for example, on a previous execution then ignore that coordinate
					if(matchPreviousCombinationOfCoordinates(
							piece.getClass().getName(), 
							occupiedSlotsPerPiece,
							coordinate))
					{
						continue;
					}
					if(checkNewPieceOnBoard(piece, proposedPosition, board))
					{
						board.addPiece(piece, proposedPosition);
						break;
					}
						
				}
			}
			if(occupiedSlots.size()==pool.size())
			{
				log.info("*************Retry " + retry);
				successCount++;
				board.print();
				boards.add(board);
			}//else no combination has been found
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
	
	/**
	 * Checks two conditions:<br>
	 * <ol>
	 * <li>That the already placed pieces are not contained in the capture slot coordinates of the new piece.
	 * <li>that the new piece coordinate is not contained in the capture slots coordinates of the pieces 
	 * already placed in the board
	 * @param piece the new piece to be added
	 * @param proposedPosition the new piece coordinates
	 * @param board the board
	 * @return true if the above conditions are met, false otherwise
	 */
	private boolean checkNewPieceOnBoard(Piece piece, Coordinate proposedPosition, Board board)
	{
		 Map<String,Slot> occupiedSlots=board.getOccupiedSlots();
		 Collection<Slot> slots=occupiedSlots.values();
		 List<Coordinate> capturedSlotsNewPiece = piece.getCaptureSlotsPositions(proposedPosition, board.getSize());
		 for(Slot slot: slots)
		 {
			 //Check if the new piece captured slots coordinates contains the occupied slot coordinate
			 //and check that the occupied piece capture slot coordinate does not contain the new position
			 if(capturedSlotsNewPiece.contains(slot.getCoordinate()) || 
						//TODO cache this on a piece class attribute
					 slot.getPiece().getCaptureSlotsPositions(slot.getCoordinate(), board.getSize()).contains(proposedPosition))
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
