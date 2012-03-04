package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.*;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.BlackKing;
import static com.darringer.games.chess.model.Piece.WhiteKing;

import java.util.HashSet;
import java.util.Set;

import com.darringer.games.chess.model.CastlingAvailability;
import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.ChessMoveCastleKingSide;
import com.darringer.games.chess.model.ChessMoveCastleQueenSide;
import com.darringer.games.chess.model.Color;
import com.darringer.games.chess.model.Location;
import com.darringer.games.chess.model.Piece;

/**
 * Implementation of {@link PieceLogic} that captures a king's
 * behavior.  Since king's initiate castling moves in this game,
 * some of that behavior is also captured here.
 * 
 * @author cdarringer
 *
 * @see com.darringer.games.chess.logic.PieceLogic
 */
public class KingLogic extends AbstractPieceLogic {

	private GameLogic logic;
	
	public KingLogic(Color color) {
		super(color);
		logic = new GameLogic();
	}
	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPossibleMoves(ChessModel, Location)
	 */
	@Override
	public Set<ChessMove> getPossibleMoves(ChessModel model, Location location) {
		Set<ChessMove> moves = new HashSet<ChessMove>();
		
		// verify there is a king at this location?
		
		// up direction
		moves = getPossibleMoveInDirection(model, location, moves, 0, 1);
		
		// up right options
		moves = getPossibleMoveInDirection(model, location, moves, 1, 1);
		
		// right direction
		moves = getPossibleMoveInDirection(model, location, moves, 1, 0);
		
		// down right options
		moves = getPossibleMoveInDirection(model, location, moves, 1, -1);
		
		// down direction
		moves = getPossibleMoveInDirection(model, location, moves, 0, -1);		

		// down left options
		moves = getPossibleMoveInDirection(model, location, moves, -1, -1);

		// left options
		moves = getPossibleMoveInDirection(model, location, moves, -1, 0);
		
		// up left options
		moves = getPossibleMoveInDirection(model, location, moves, -1, 1);
		
		// castling options
		moves = getPossibleCastlingMoves(model, location, moves);
		
		// we are done
		return moves;
	}	
	
	/**
	 * 
	 * @param model
	 * @param location
	 * @param movesSoFar
	 * @param dx
	 * @param dy
	 * @return
	 */
	protected Set<ChessMove> getPossibleMoveInDirection(ChessModel model, Location location, Set<ChessMove> movesSoFar, int dx, int dy) {
		Location possibleLocation = location.getRelativeLocation(dx, dy);
		if (possibleLocation != Unknown) {
			// this is a valid board position
			if (model.isLocationEmpty(possibleLocation)) {
				// empty space -> possible move
				movesSoFar.add(new ChessMove(getPiece(), location, possibleLocation));
			} else if (model.isLocationOccupiedByColor(possibleLocation, getOtherColor())) {
				// opponent's piece -> possible move
				movesSoFar.add(new ChessMove(getPiece(), location, possibleLocation));
			} 
		}
		return movesSoFar;
	}
	
	/**
	 * Kings can initiate castling moves in this game.
	 * Check for temporary and permanent restrictions on castling availability.
	 * 
	 * @param model
	 * @param location
	 * @param movesSoFar
	 * @return
	 */
	protected Set<ChessMove> getPossibleCastlingMoves(ChessModel model, Location location,  Set<ChessMove> movesSoFar) {
		CastlingAvailability castlingAvailability = model.getCastlingAvailability();
		if ((location == E1) && (getColor() == White)) {
			// this is the white king in the starting position
			if (castlingAvailability.isWhiteCanCastleKingSide()) {
				if (model.isLocationEmpty(F1) && model.isLocationEmpty(G1)) {
					if (!isInCheckAtLocations(model, E1, F1, G1)) {
						ChessMove move = new ChessMoveCastleKingSide(getPiece(), E1, G1);
						movesSoFar.add(move);
					}
				}
			}
			if (castlingAvailability.isWhiteCanCastleQueenSide()) {
				if (model.isLocationEmpty(B1) && model.isLocationEmpty(C1) && model.isLocationEmpty(D1)) {
					if (!isInCheckAtLocations(model, B1, C1, D1, E1)) {
						ChessMove move = new ChessMoveCastleQueenSide(getPiece(), E1, C1);
						movesSoFar.add(move);
					}
				}
			}
		} else if ((location == E8) && (getColor() == Black)) {
			// this is the black king in the starting position
			if (castlingAvailability.isBlackCanCastleKingSide()) {
				if (model.isLocationEmpty(F8) && model.isLocationEmpty(G8)) {
					if (!isInCheckAtLocations(model, E8, F8, G8)) {
						ChessMove move = new ChessMoveCastleKingSide(getPiece(), E8, G8);
						movesSoFar.add(move);
					}
				}				
			}
			if (castlingAvailability.isBlackCanCastleQueenSide()) {
				if (model.isLocationEmpty(B8) && model.isLocationEmpty(C8) && model.isLocationEmpty(D8)) {
					if (!isInCheckAtLocations(model, B8, C8, D8, E8)) {
						ChessMove move = new ChessMoveCastleQueenSide(getPiece(), E8, C8);
						movesSoFar.add(move);
					}
				}				
			}			
		}
		return movesSoFar;
	}
	
	/**
	 * 
	 * @param model
	 * @param locations
	 * @return
	 */
	protected boolean isInCheckAtLocations(ChessModel model, Location... locations) {
		for (Location currentLocation : locations) {
			if (logic.isLocationReachableByColor(model, currentLocation, getOtherColor())) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPiece()
	 */
	@Override
	public Piece getPiece() {
		return getColor() == White ? WhiteKing : BlackKing;
	}
}
