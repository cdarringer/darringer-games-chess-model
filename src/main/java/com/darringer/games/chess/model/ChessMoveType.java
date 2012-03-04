package com.darringer.games.chess.model;

/**
 * In addition to regular moves, there are a handful of other 
 * move types that require unique handling.  We use the types 
 * defined in this enumeration to differentiate these move types.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.ChessMove
 *
 */
public enum ChessMoveType {
	Regular, EnPassantCapture, CastleKingSide, CastleQueenSide, PawnPromotion;
}
