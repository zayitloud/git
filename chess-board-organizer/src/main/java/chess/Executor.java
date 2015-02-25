package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chess.board.Board;
import chess.board.Slot;
import chess.piece.King;
import chess.piece.Piece;

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
	
	private Board board;
	
	public Executor()
	{
		pool = new ArrayList<Piece>();
		pool.add(new King());
		pool.add(new King());
		board = new Board(3,3);
		
	}
	
	public void start()
	{
		Map<String, Slot> occupiedSlots = board.getOccupiedSlots();
		Map<String, Slot> availableSlots = board.getAvailableSlots();
		//to avoid checking a slot for a specific piece more than once
		Map<String,Map<String,Slot>> availableSlotsPerPiece;
		for(Piece piece: pool)
		{
			
			//if(board.getOccupiedSlots(piece.getClass().getName()).containsKey(board.get))
		}
		
	}
	
}
