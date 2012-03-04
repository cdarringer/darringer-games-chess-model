package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Location.Unknown;
import static com.darringer.games.chess.model.Piece.BlackKnight;
import static com.darringer.games.chess.model.Piece.WhiteKnight;

import java.util.HashSet;
import java.util.Set;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.Color;
import com.darringer.games.chess.model.Location;
import com.darringer.games.chess.model.Piece;

/**
 * Implementation of {@link PieceLogic} that captures a knight's
 * behavior
 * 
 * @author cdarringer
 *
 * @see com.darringer.games.chess.logic.PieceLogic
 */
public class KnightLogic extends AbstractPieceLogic {

	public KnightLogic(Color color) {
		super(color);
	}
	
	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPossibleMoves(ChessModel, Location)
	 */	
	@Override
	public Set<ChessMove> getPossibleMoves(ChessModel model, Location location) {
		Set<ChessMove> moves = new HashSet<ChessMove>();
		
		// verify there is a knight at this location?
		
		// left 1 up 2
		moves = getPossibleKnightMove(model, location, moves, -1, 2);
		
		// right 2 up 1
		moves = getPossibleKnightMove(model, location, moves, 1, 2);
		
		// right 2 up 1
		moves = getPossibleKnightMove(model, location, moves, 2, 1);
		
		// right 2 down 1
		moves = getPossibleKnightMove(model, location, moves, 2, -1);
		
		// right 1 down 2
		moves = getPossibleKnightMove(model, location, moves, 1, -2);
		
		// left 1 down 2
		moves = getPossibleKnightMove(model, location, moves, -1, -2);
		
		// left 2 down 1
		moves = getPossibleKnightMove(model, location, moves, -2, -1);
		
		// left 2 up 1
		moves = getPossibleKnightMove(model, location, moves, -2, 1);
		
		// we are done
		return moves;
	}	

	/**
	 * 
	 * @param model
	 * @param location
	 * @param movesSoFar
	 * @param deltaX
	 * @param deltaY
	 * @return
	 */
	protected Set<ChessMove> getPossibleKnightMove(ChessModel model, Location location, Set<ChessMove> movesSoFar, int deltaX, int deltaY) {
		Location possibleLocation = location.getRelativeLocation(deltaX, deltaY);
		if (possibleLocation != Unknown) {
			if ((model.isLocationEmpty(possibleLocation)) || 
				(model.isLocationOccupiedByColor(possibleLocation, getOtherColor())))
			{
				movesSoFar.add(new ChessMove(getPiece(), location, possibleLocation));
			}
		}
		return movesSoFar;
	}
	
	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPiece()
	 */
	@Override
	public Piece getPiece() {
		return getColor() == White ? WhiteKnight : BlackKnight;
	}
}
