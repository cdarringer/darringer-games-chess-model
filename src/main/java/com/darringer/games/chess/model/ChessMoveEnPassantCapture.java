package com.darringer.games.chess.model;

import static com.darringer.games.chess.model.ChessMoveType.EnPassantCapture;

/**
 * Extension of {@link ChessMove} to differentiate en passant capture
 * moves from regular moves.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.ChessMove 
 * 
 */
public class ChessMoveEnPassantCapture extends ChessMove {
	public ChessMoveEnPassantCapture(Piece piece, Location fromLocation, Location toLocation) {
		super(piece, fromLocation, toLocation);
	}
		
	@Override
	public ChessMoveType getType() {
		return EnPassantCapture;
	}
	
	@Override
	public Location getEnPassantCaptureLocation() {
		int x = to.getX();
		int y = from.getY();
		return Location.get(x, y);
	}
}
