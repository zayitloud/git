package chess.piece;

import chess.Board;

/**
 * Define the chess piece methods common to all pieces to be placed on a chess board 
 * @author Willie
 *
 */
public interface Piece {
	void checkCaptureSpaces(Board board);
}
