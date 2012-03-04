package com.darringer.games.chess.model;

import static com.darringer.games.chess.model.ChessMoveType.CastleQueenSide;

/**
 * Extension of {@link ChessMove} to differentiate queen side castle
 * moves from regular moves.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.ChessMove
 * 
 */
public class ChessMoveCastleQueenSide extends ChessMove {
	
	public ChessMoveCastleQueenSide(Piece piece, Location fromLocation, Location toLocation) {
		super(piece, fromLocation, toLocation);
	}
	
	
	@Override
	public ChessMoveType getType() {
		return CastleQueenSide;
	}
}
