package chess;

import java.io.Serializable;
import java.util.List;

import chess.board.Board;
import chess.piece.Piece;

/**
 * This class stores a list of boards and a pool of pieces. Its main purpose
 * is to be used to serialize the information about a given execution.
 * @author williez
 *
 */
public class Execution implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5345593229481706186L;
	/**
	 * The container of pieces to be placed in the board
	 */
	private List<Piece> pool;
	
	/**
	 * To store the boards configuration per execution
	 */
	private List<Board> boards;

	public List<Piece> getPool() {
		return pool;
	}

	public List<Board> getBoards() {
		return boards;
	}

	public Execution(List<Piece> pool, List<Board> boards) {
		super();
		this.pool = pool;
		this.boards = boards;
	}
}
