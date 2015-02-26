package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chess.board.Board;
import chess.board.Slot;
import chess.piece.Coordinate;
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
	
	private String boardSize;
	
	public Executor()
	{
		pool = new ArrayList<Piece>();
		pool.add(new King());
		pool.add(new King());
		board = new Board(3,3);
		boardSize="3;3";
		
	}
	
	public void start()
	{
		Map<String, Slot> occupiedSlots = board.getOccupiedSlots();
		Map<String, Slot> availableSlots = board.getAvailableSlots();
		//to avoid checking a slot for a specific piece more than once
		Map<String, Slot> occupiedSlotsPerPiece;
		List<Coordinate> captureSlots;
		boolean captureSlotsAreFree=true;
		for(Piece piece: pool)
		{
			occupiedSlotsPerPiece=board.getOccupiedSlots(piece.getClass().getName());
			for(String coordinate: availableSlots.keySet())
			{
				Coordinate proposedPosition = new Coordinate(coordinate);
				//if the available coordinate has been occupied before by the same type of piece
				//for example, on a previous execution then ignore that coordinate
				if(occupiedSlotsPerPiece!=null && occupiedSlotsPerPiece.containsKey(coordinate))
				{
					continue;
				}
				//get the slots the piece can move to to verify if they are available
				//if any of the slots is occupied by another piece then the available slot cannot be used by this piece type
				captureSlots = piece.getCaptureSlotsPositions(proposedPosition, new Coordinate(boardSize));
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
					
			}
		}
		board.printInternalValues();
		
	}
	
	public static void main(String args[])
	{
		new Executor().start();
	}
	
}
