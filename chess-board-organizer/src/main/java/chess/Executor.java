package chess;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	
	private int maxNumberOfParallelCheckers;
	
	/**
	 * Execution parameters
	 */
	private String args[];

	private long accumulatedTimeInMillis;

	private ExecutorService es;
	
	private final short DIRECTION_X=0;
	private final short DIRECTION_Y=1;
	/**
	 * Default constructor
	 */
	public Executor()
	{
//		this(new String[]{"6","6","2","1","2","2","1","700000"});		
//		this(new String[]{"3","3","2","0","0","0","1","100"});		
		this(new String[]{"4","4","0","0","0","4","2","500"});		
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
			//Parallel execution parameters
			es = Executors.newFixedThreadPool(4);
			maxNumberOfParallelCheckers=6;
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
		long startRunningTime=Calendar.getInstance().getTimeInMillis();
		printExecutionParameters();
		int successCount=0;
		Map<String, Slot> occupiedSlots;
		List<String> availableSlots;
		Board board;
		int occupiedSlotsSize=0;
		for(int retry=1; retry<=maxNumberOfRetries; retry++)
		{

			board = new Board(boardSize.getX(),boardSize.getY());
			occupiedSlots = board.getOccupiedSlots();
			availableSlots = board.getAvailableCoordinatesList();
			for(Piece piece: pool)
			{
				occupiedSlots=board.getOccupiedSlots();
				occupiedSlotsSize=occupiedSlots.size();	
				Collections.shuffle(availableSlots);
				for(String coordinate: availableSlots)
				{
					Coordinate proposedPosition = new Coordinate(coordinate);
					//if the available coordinate has been occupied before by the same type of piece
					//for example, on a previous execution then ignore that coordinate
					long initMillis= Calendar.getInstance().getTimeInMillis();
					if(!checkNewPieceOnBoard(piece, proposedPosition, board))
					{
						continue;
					}
					if(!(occupiedSlotsSize+1==pool.size() && matchPreviousCombinationOfCoordinates(
							piece.getClass().getName(), 
							occupiedSlots,
							coordinate)))
					{
						accumulatedTimeInMillis+=Calendar.getInstance().getTimeInMillis() - initMillis;
						board.addPiece(piece, proposedPosition);
						break;
					}
					accumulatedTimeInMillis+=Calendar.getInstance().getTimeInMillis() - initMillis;
				}
			}
			if(board.getOccupiedSlots().size()==pool.size())
			{
				log.info("*************Retry " + retry);
				successCount++;
				board.print();
				List<Board> list = getAlternateBoards(board);
//				log.info("****Alternate boards");
				for(Board bo: list)
				{
					log.info("-------");
					bo.print();
					successCount++;
				}
				boards.add(board);
				boards.addAll(list);
			}//else no combination has been found
		}
		es.shutdown();
		printExecutionParameters();
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
		df.applyPattern("#####0.00##");
		log.info("Success combinations found: " + successCount);
		log.info("Number of calculations: " + maxNumberOfRetries);
		log.info("Accumulated time in seconds " + df.format(accumulatedTimeInMillis/1000d));
		log.info("Accumulated time in minutes " + df.format(accumulatedTimeInMillis/(1000d*60)));
		long totalRunningTime=Calendar.getInstance().getTimeInMillis()-startRunningTime;
		log.info("Total running time in seconds " + df.format(totalRunningTime/1000d));
		log.info("Total running time in minutes " + df.format(totalRunningTime/(1000d*60)));
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
		if(boards.size()==0)
		{
			return false;
		}
		List<CheckerThread> list=new ArrayList<CheckerThread>();
		List<Future<Boolean>> results=null;
		int boardSize=boards.size();
		int maxNumberOfCheckers=getNumberOfCheckers(boardSize);
		int section=Math.round(boardSize/maxNumberOfCheckers);
		int previousPosition=0;
		for (int i=0; i<maxNumberOfCheckers;i++)
		{
			list.add(new CheckerThread(pieceType, currentCombination, newPosition, boards.subList(previousPosition,section)));
			previousPosition=section;
			section+=section;
			if(section>boardSize)
			{
				section=boardSize;
			}
		}
		try {
			results = es.invokeAll(list);
			for (Future<Boolean> result: results)
			{
				if(result.get())
				{
					return true;
				}
			}
		} catch (InterruptedException e) {
			log.error("Error executing parallel process" ,e);
		} catch (ExecutionException e) {
			log.error("Error executing parallel process" ,e);
		}
		return false;
	}
	
	/**
	 * Return a number of parallel checkers dependent on the size of the list to be processed
	 * @param size the size of the list to be processed
	 * @return the number of parallel checkers
	 */
	private int getNumberOfCheckers(int size) {
		if(size<10)
		{
			return 1;
		}
		if(size<50)
		{
			return 2;
		}
		if(size<1000)
		{
			return 4;
		}
		if(size<10000)
		{
			return 6;
		}else{
			return maxNumberOfParallelCheckers;
		}
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
	 * Returns all the possible boards combinations for a given board
	 * @param board the board that will be used as a base to calculate the new configurations
	 * @return the list of board configurations, an empty list if no configurations are found
	 */
	private List<Board> getAlternateBoards(Board board)
	{
		List<Board> list= new ArrayList<Board>();
		Board auxBoard=getAlternateBoard(board, DIRECTION_X);
		if(auxBoard!=null)
		{
			list.add(auxBoard);
		}
		auxBoard=getAlternateBoard(board,DIRECTION_Y);
		if(auxBoard!=null)
		{
			list.add(auxBoard);
			auxBoard=getAlternateBoard(auxBoard, DIRECTION_X);
			if(auxBoard!=null)
			{
				list.add(auxBoard);
			}
		}
		return list;
	}
	
	private Board getAlternateBoard(Board board, short direction)
	{
		Board alternateBoard= new Board(board.getSize().getX(),board.getSize().getY());
		Map<String, Slot> occupiedSlots = board.getOccupiedSlots();
		Collection<Slot> values = occupiedSlots.values();
//		List<Board> list= new ArrayList<Board>();
//		for(Slot slot:values)
//		{
//			Slot pivotSlot= slot;
//			break;
//		}
		int newCoordinate=0, boardSize=0;
		Coordinate coordinate;
		if(direction==DIRECTION_X)
		{
			boardSize=board.getSize().getX();
		}else{
			boardSize=board.getSize().getY();
		}
		for(Slot slot:values)
		{
			if(direction==DIRECTION_X)
			{
				newCoordinate=slot.getCoordinate().getX()+1;
			}else{
				newCoordinate=slot.getCoordinate().getY()+1;
			}
			if(newCoordinate>boardSize)
			{
				newCoordinate=1;
			}
			Piece piece=null;
			try {
				piece=slot.getPiece().getClass().newInstance();
				if(direction==DIRECTION_X)
				{
					coordinate=new Coordinate(newCoordinate,slot.getCoordinate().getY());
				}else{
					coordinate=new Coordinate(slot.getCoordinate().getX(),newCoordinate);
				}
				if(checkNewPieceOnBoard(piece, coordinate, alternateBoard))
				{
					alternateBoard.addPiece(piece, coordinate);
				}else{
					return null;
				}
			} catch (InstantiationException | IllegalAccessException e) {
				log.error("Error checking for alternate boards",e);
				return null;
			}
		}
		return alternateBoard;
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
//		new Executor(args).start();
		new Executor().start();
	}
	
}
