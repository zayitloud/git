package chess;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import chess.board.Board;
import chess.board.Slot;

/**
 * This class started out as a Thread but the implementation has been changed to a Callable class
 * invoked during the check to find out if the current combination on a board has already being found on
 * a previous execution. To avoid having a time consuming task checking one by one sequentially (which it certainly is when there are tens of thousand 
 * combinations already found), now this is done in parallel
 * with the support of java Executors
 * @author williez
 * @see Executors
 */
public class CheckerThread implements Callable<Boolean> {

	String pieceType;
	Map<String,Slot> currentCombination;
	String newPosition;
	List<Board> boards;
	
	
	public CheckerThread(String pieceType,
			Map<String, Slot> currentCombination, String newPosition,
			List<Board> boards) {
		super();
		this.pieceType = pieceType;
		this.currentCombination = currentCombination;
		this.newPosition = newPosition;
		this.boards = boards;
	}


	@Override
	public Boolean call() throws Exception {
		
		//if it is the first piece to be added to the board return false 
		if(currentCombination==null)
		{
			return false;
		}
		Map<String, Slot> previousCombination;

		int currentCombinationSize=currentCombination.size()+1;
		boolean matchesCombination=true;
		//the slot position for a given piece type is compared with 
		//slot positions on previous boards to avoid repeating positions
		for(Board board:boards)
		{
			matchesCombination=true;
			previousCombination = board.getOccupiedSlots();
			//if the combinations are not the same size we still don't know if they match
			//in which case false is returned
			if(currentCombinationSize==previousCombination.size())
			{
				for(String coordinates: currentCombination.keySet())
				{
					
					//if the previous positions does not contain any of the  positions
					//it is not the same combination OR
					//the piece on the current board position combination is not the same type as the piece
					//in the previous board position combination then the combination is different so
					//there's no need to look further					
					if(!previousCombination.containsKey(coordinates) ||
							!currentCombination.get(coordinates).getPiece().getClass().getName().equals(
									previousCombination.get(coordinates).getPiece().getClass().getName()))
					{
						matchesCombination=false;
						break;
					}
				}
				//if current combination matches any the previous combination of coordinates then
				//we check if the position where the new piece will be added matches also the position and the 
				//piece type on the previous combination 
				if(matchesCombination)
				{
					if(previousCombination.containsKey(newPosition) && pieceType.equals(
							previousCombination.get(newPosition).getPiece().getClass().getName()))
					{
						
						return true;
					}
				}
			}
			
		}
		return false;

	}
}
