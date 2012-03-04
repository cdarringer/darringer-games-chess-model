package com.darringer.games.chess.logic;

import java.util.Set;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.Location;
import com.darringer.games.chess.model.Piece;

/**
 * Interface for the functions that all pieces are expected
 * to implement.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.ChessMove
 * @see com.darringer.games.chess.model.Piece
 *
 */
public interface PieceLogic {

	/**
	 * Return the unique set of possible moves made possible by a
	 * piece associated with the given {@link PieceLogic} at the 
	 * passed in {@link Location}.
	 * <p />
	 * In all cases we assume the underlying model actually has a piece
	 * associated with the given logic at the passed in location
	 * (this is not something we assert at runtime)
	 * 
	 * @param model
	 * @param location
	 * @return
	 */
	Set<ChessMove> getPossibleMoves(ChessModel model, Location location);
	
	/**
	 * Return the {@link Piece} associated with the underlying piece
	 * logic implementation
	 * 
	 * @return
	 */
	Piece getPiece();
	
}
