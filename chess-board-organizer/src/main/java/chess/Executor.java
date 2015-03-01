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
import chess.piece.Queen;
import chess.piece.Rook;

/**
 * This class is the executor of the main application logic which is allocating
 * the resources necessary to find all possible Pieces combination on a chess board
 * @author Willie 
 *
 */
public class Executor {

	/**
	 * The number of times the Executor will look for the combination of pieces on the chess board
	 */
	private int maxNumberOfRetries;
	
	/**
	 * The container of pieces to be placed in the board
	 */
	private List<Piece> pool;
	
	/**
	 * To store the boards configuration per run
	 */
	private List<Board> boards;
	
	/**
	 * The size of the chess board
	 */
	private Coordinate boardSize;
	
	private Logger log= LoggerFactory.getLogger(Executor.class);
	
	private String args[];
	/**
	 * Default constructor
	 */
	public Executor()
	{
		this(new String[]{"6","6","2","1","2","2","1","10000"});		
	}
	/**
	 * Constructor of the Executor class which takes as parameter an array of strings which <b>must</b> contain
	 * the following parameters in the indicated order:
	 * <ol>
	 * <li>M size of the board
	 * <li>N size of the board
	 * <li>Number of Kings
	 * <li>Number of Queens
	 * <li>Number of Bishop
	 * <li>Number of Knights
	 * <li>Number of Rooks (may be 0)
	 * <li>Number of retries: the number of times the routine is going to look for possible piece combinations (optional
	 * 0 or empty if desired). 
	 * @param args the array of strings containing the parameters defined above
	 * @throws RuntimeException in case the parameters are incorrect or missing
	 */
	public Executor(String args[])
	{
		if(args.length<7 || args.length>8)
		{
			throw new RuntimeException("Incorrect number of parameters");
		}
		this.args=args;
		boards=new ArrayList<Board>();
		boardSize= new Coordinate(args[0]+";"+args[1]);
		pool = new ArrayList<Piece>();
		try {
			fillPool(Integer.parseInt(args[2]), King.class.getCanonicalName());
			fillPool(Integer.parseInt(args[3]), Queen.class.getCanonicalName());
			fillPool(Integer.parseInt(args[4]), Bishop.class.getCanonicalName());
			fillPool(Integer.parseInt(args[5]), Knight.class.getCanonicalName());
			fillPool(Integer.parseInt(args[6]), Rook.class.getCanonicalName());
			if(args.length==8)
			{
				maxNumberOfRetries=Integer.parseInt(args[7]);
			}else{
				maxNumberOfRetries=5000;
			}
			
		} catch (NumberFormatException | InstantiationException
				| IllegalAccessException | ClassNotFoundException e) {
			log.error("Error creating the Executor",e);
			throw new RuntimeException("Error creating the executor",e);
		}
		
	}

	/**
	 * Creates the number of instances defined in className and adds them to the Pieces pool
	 * @param quantity the number of instances to create
	 * @param className the qualified name of the class to be instantiated 
	 * @throws InstantiationException in case there is an error instantiating the class
	 * @throws IllegalAccessException in case there is an error instantiating the class
	 * @throws ClassNotFoundException in case there is an error instantiating the class
	 */
	private void fillPool(int quantity, String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		if(pool==null){
			pool= new ArrayList<Piece>();
		}
		for(int i=0; i<quantity;i++)
		{
			pool.add((Piece) Class.forName(className).newInstance());
		}
		
	}
	/**
	 * This method searches for all the piece combinations of piece positions possible on a chess board
	 * based on 3 parameters:
	 * <ol>
	 * <li>The type of pieces to be placed on the chess board
	 * <li>The size of the chess board
	 * <li>The maximum number of times the routine will be executed to obtain the possible combinations.
	 * </ol> 
	 */
	public void start()
	{
		printExecutionParameters();
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
		printExecutionParameters();
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
	
	/**
	 * prints in the logs the execution parameters sent to the Executor
	 */
	private void printExecutionParameters()
	{
		log.info("Execution parameters:");
		log.info("Board size: " + args[0]+"x"+args[1]);
		log.info("#Kings: " + args[2]);
		log.info("#Queens: " + args[3]);
		log.info("#Bishops: " + args[4]);
		log.info("#Knights: " + args[5]);
		log.info("#Rooks: " + args[6]);
		if(args.length==8)
		{
			log.info("Number of retries: " + args[7]);
		}else{
			log.info("Number of retries: "+ maxNumberOfRetries+ " (default - not provided)");
			
		}
		
	}
	
	/**
	 * Main method which is the entry point to execute the routine to calculate the number of position combinations
	 * on a chess board without threatening each other. This method takes as parameter an array of strings which <b>must</b> contain
	 * the following parameters in the indicated order:
	 * <ol>
	 * <li>M size of the board
	 * <li>N size of the board
	 * <li>Number of Queens
	 * <li>Number of Kings
	 * <li>Number of Bishop
	 * <li>Number of Knights
	 * <li>Number of Rooks (may be 0)
	 * <li>Number of retries: the number of times the routine is going to look for possible piece combinations (optional
	 * 0 or empty if desired). 
	 * @param args
	 */
	public static void main(String args[])
	{
		new Executor(args).start();
//		new Executor().start();
	}
	
}
