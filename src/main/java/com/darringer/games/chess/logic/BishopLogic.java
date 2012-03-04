package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Piece.BlackBishop;
import static com.darringer.games.chess.model.Piece.WhiteBishop;

import java.util.HashSet;
import java.util.Set;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.Color;
import com.darringer.games.chess.model.Location;
import com.darringer.games.chess.model.Piece;

/**
 * Implementation of {@link PieceLogic} that captures a bishop's
 * behavior
 * 
 * @author cdarringer
 *
 * @see com.darringer.games.chess.logic.PieceLogic
 */
public class BishopLogic extends AbstractPieceLogic {

	/**
	 * 
	 * @param color
	 */
	public BishopLogic(Color color) {
		super(color);
	}
	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPossibleMoves(ChessModel, Location)
	 */
	@Override
	public Set<ChessMove> getPossibleMoves(ChessModel model, Location location) {
		Set<ChessMove> moves = new HashSet<ChessMove>();
		
		// up right options
		moves = getPossibleMovesInDirection(model, location, moves, 1, 1);
		
		// down right options
		moves = getPossibleMovesInDirection(model, location, moves, 1, -1);
		
		// up left options
		moves = getPossibleMovesInDirection(model, location, moves, -1, 1);
		
		// down left options
		moves = getPossibleMovesInDirection(model, location, moves, -1, -1);
		
		// we are done
		return moves;
	}	

	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPiece()
	 */
	@Override
	public Piece getPiece() {
		return getColor() == White ? WhiteBishop : BlackBishop;
	}
}
