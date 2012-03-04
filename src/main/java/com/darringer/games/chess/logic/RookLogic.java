package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Piece.BlackRook;
import static com.darringer.games.chess.model.Piece.WhiteRook;

import java.util.HashSet;
import java.util.Set;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.Color;
import com.darringer.games.chess.model.Location;
import com.darringer.games.chess.model.Piece;

/**
 * Implementation of {@link PieceLogic} that captures a rook's
 * behavior.
 * 
 * @author cdarringer
 *
 * @see com.darringer.games.chess.logic.PieceLogic
 */
public class RookLogic extends AbstractPieceLogic {

	public RookLogic(Color color) {
		super(color);
	}
	
	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPossibleMoves(ChessModel, Location)
	 */	
	@Override
	public Set<ChessMove> getPossibleMoves(ChessModel model, Location location) {
		Set<ChessMove> moves = new HashSet<ChessMove>();
		
		// verify there is a rook at this location?
		
		// up options
		moves = getPossibleMovesInDirection(model, location, moves, 0, 1);
		
		// down options
		moves = getPossibleMovesInDirection(model, location, moves, 0, -1);
		
		// left options
		moves = getPossibleMovesInDirection(model, location, moves, -1, 0);
		
		// right options
		moves = getPossibleMovesInDirection(model, location, moves, 1, 0);
		
		// we are done
		return moves;
	}	
	
	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPiece()
	 */
	@Override
	public Piece getPiece() {
		return getColor() == White ? WhiteRook : BlackRook;
	}
}
