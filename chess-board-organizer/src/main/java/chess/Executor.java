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
	 * To store the boards configuration per execution
	 */
	private List<Board> boards;
	
	/**
	 * The size of the chess board
	 */
	private Coordinate boardSize;
	
	private Logger log= LoggerFactory.getLogger(Executor.class);
	
	/**
	 * Execution parameters
	 */
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
		Board board;
		for(int retry=1; retry<=maxNumberOfRetries; retry++)
		{

			board = new Board(boardSize.getX(),boardSize.getY());
			occupiedSlots = board.getOccupiedSlots();
			availableSlots = board.getAvailableCoordinatesList();
			for(Piece piece: pool)
			{
				occupiedSlots=board.getOccupiedSlots();
				Collections.shuffle(availableSlots);
				for(String coordinate: availableSlots)
				{
					Coordinate proposedPosition = new Coordinate(coordinate);
					//if the available coordinate has been occupied before by the same type of piece
					//for example, on a previous execution then ignore that coordinate
					if(matchPreviousCombinationOfCoordinates(
							piece.getClass().getName(), 
							occupiedSlots,
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
			if(board.getOccupiedSlots().size()==pool.size())
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
	 * To achieve this every position  on the current arrangements is looked up in the previous boards arrangements to make sure the position exists in previous boards
	 * and that the type of piece in the same in the current and previous boards
	 * @param pieceType the type of the new piece that will be added to the board
	 * @param currentCombination the arrangement of pieces in the current board
	 * @param newPosition the last position being added to the current positions the coordinate where the new piece will be added
	 * @return true if the arrangement matches any of the previous boards arrangements (the same position and the same piece type), false otherwise
	 */
	private boolean matchPreviousCombinationOfCoordinates(String pieceType, Map<String,Slot> currentCombination, String newPosition)
	{
		//if it is the first piece to be added to the board return false 
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
			previousCombination = board.getOccupiedSlots();
			//if the combinations are not the same size we still don't know if they match
			//in which case false is returned
			if(currentCombination.size()+1==previousCombination.size())
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
						
						log.debug("Duplicate combination found");
						log.debug(currentCombination.toString() + newPosition + "/" + pieceType);
						log.debug(previousCombination.toString());
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
	 * <li>Number of Kings
	 * <li>Number of Queens
	 * <li>Number of Bishop
	 * <li>Number of Knights
	 * <li>Number of Rooks (may be 0)
	 * <li>Number of retries: the number of times the routine is going to look for possible piece combinations (optional
	 * 0 or empty if desired). 
	 * @param args the array of parameters
	 */
	public static void main(String args[])
	{
		new Executor(args).start();
//		new Executor().start();
	}
	
}
