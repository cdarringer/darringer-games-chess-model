package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Piece.None;

import java.util.HashSet;
import java.util.Set;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.Color;
import com.darringer.games.chess.model.Location;
import com.darringer.games.chess.model.Piece;

/**
 * No-op implementation of {@link PieceLogic}, useful for testing
 * purposes.
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.logic.PieceLogic
 *
 */
public class NoneLogic extends AbstractPieceLogic {
		
	public NoneLogic(Color color) { 
		super(color);
	} 
	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPossibleMoves(ChessModel, Location)
	 */
	@Override
	public Set<ChessMove> getPossibleMoves(ChessModel model, Location location) {
		Set<ChessMove> possibleMoves = new HashSet<ChessMove>();
		return possibleMoves;
	}

	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPiece()
	 */
	@Override
	public Piece getPiece() {
		return None;
	}
	
}
