package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Piece.BlackQueen;
import static com.darringer.games.chess.model.Piece.WhiteQueen;

import java.util.HashSet;
import java.util.Set;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.Color;
import com.darringer.games.chess.model.Location;
import com.darringer.games.chess.model.Piece;

/**
 * Implementation of {@link PieceLogic} that captures a queen's
 * behavior.
 * 
 * @author cdarringer
 *
 * @see com.darringer.games.chess.logic.PieceLogic
 */
public class QueenLogic extends AbstractPieceLogic {

	public QueenLogic(Color color) {
		super(color);
	}
	
	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPossibleMoves(ChessModel, Location)
	 */
	@Override
	public Set<ChessMove> getPossibleMoves(ChessModel model, Location location) {
		Set<ChessMove> moves = new HashSet<ChessMove>();
		
		// verify there is a queen at this location?
		
		// up direction
		moves = getPossibleMovesInDirection(model, location, moves, 0, 1);
		
		// up right options
		moves = getPossibleMovesInDirection(model, location, moves, 1, 1);
		
		// right direction
		moves = getPossibleMovesInDirection(model, location, moves, 1, 0);
		
		// down right options
		moves = getPossibleMovesInDirection(model, location, moves, 1, -1);
		
		// down direction
		moves = getPossibleMovesInDirection(model, location, moves, 0, -1);		

		// down left options
		moves = getPossibleMovesInDirection(model, location, moves, -1, -1);

		// left options
		moves = getPossibleMovesInDirection(model, location, moves, -1, 0);
		
		// up left options
		moves = getPossibleMovesInDirection(model, location, moves, -1, 1);
		
		// we are done
		return moves;
	}	

	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPiece()
	 */
	@Override
	public Piece getPiece() {
		return getColor() == White ? WhiteQueen : BlackQueen;
	}
}
