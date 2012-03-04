package com.darringer.games.chess.model;

import static com.darringer.games.chess.model.ChessMoveType.CastleKingSide;

/**
 * Extension of {@link ChessMove} to differentiate king side castle
 * moves from regular moves.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.ChessMove
 *
 */
public class ChessMoveCastleKingSide extends ChessMove {

	public ChessMoveCastleKingSide(Piece piece, Location fromLocation, Location toLocation) {
		super(piece, fromLocation, toLocation);
	}
	
	
	@Override
	public ChessMoveType getType() {
		return CastleKingSide;
	}
	
}
