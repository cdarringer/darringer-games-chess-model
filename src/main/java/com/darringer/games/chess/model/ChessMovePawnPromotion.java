package com.darringer.games.chess.model;

import static com.darringer.games.chess.model.ChessMoveType.PawnPromotion;

/**
 * Extension of {@link ChessMove} to differentiate pawn promotion 
 * moves from regular moves.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.ChessMove
 */
public class ChessMovePawnPromotion extends ChessMove {
	
	protected Piece pawnPromotionPiece;
	
	public ChessMovePawnPromotion(Piece piece, Location fromLocation, Location toLocation, Piece pawnPromotionPiece) {
		super(piece, fromLocation, toLocation);
		this.pawnPromotionPiece = pawnPromotionPiece;
	}
		
	@Override
	public ChessMoveType getType() {
		return PawnPromotion;
	}
	
	public Piece getPawnPromotionPiece() {
		return pawnPromotionPiece;
	}
}
