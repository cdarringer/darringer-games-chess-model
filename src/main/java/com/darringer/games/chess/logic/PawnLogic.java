package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.Location.Unknown;
import static com.darringer.games.chess.model.Piece.*;

import java.util.HashSet;
import java.util.Set;

import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.ChessMoveEnPassantCapture;
import com.darringer.games.chess.model.ChessMovePawnPromotion;
import com.darringer.games.chess.model.Color;
import com.darringer.games.chess.model.Location;
import com.darringer.games.chess.model.Piece;

/**
 * Implementation of {@link PieceLogic} that captures a pawn's
 * behavior.  In addition to basic move logic, we have addition rules
 * for en passant capture and pawn promotion here.
 * 
 * @author cdarringer
 *
 * @see com.darringer.games.chess.logic.PieceLogic 
 * 
 */
public class PawnLogic extends AbstractPieceLogic {
	
	public PawnLogic(Color color) {
		super(color);
	}

	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPossibleMoves(ChessModel, Location)
	 */
	@Override
	public Set<ChessMove> getPossibleMoves(ChessModel model, Location location) {
		Set<ChessMove> moves = new HashSet<ChessMove>();
		int dir = getDirectionMultiplier();
		
		// verify there is a pawn at this location?
		
		// move forward one
		Location forward1Location = location.getRelativeLocation(0, dir * 1);
		if (forward1Location != Unknown) {
			if (model.isLocationEmpty(forward1Location)) {
				moves = addPawnMoveWithPromotionCheck(moves, location, forward1Location);
				
				// move forward two if in starting position
				if (isStartingRow(location)) {
					Location forward2Location = location.getRelativeLocation(0, dir * 2);
					if (forward2Location != Unknown) {
						if (model.isLocationEmpty(forward2Location)) {
							Location enPassantLocation = location.getRelativeLocation(0, dir * 1);
							ChessMove newMove = new ChessMove(getPiece(), location, forward2Location);
							newMove.setEnPassantLocation(enPassantLocation);
							moves.add(newMove);
						}
					}				
				}
			}
		}
					
		// capture diagonal left
		Location diagLeftLocation = location.getRelativeLocation(-1, dir * 1);
		if (diagLeftLocation != Unknown) {
			if (model.isLocationOccupiedByColor(diagLeftLocation, getOtherColor())) {
				// regular capture
				moves = addPawnMoveWithPromotionCheck(moves, location, diagLeftLocation);
			} else if (model.isLocationEmpty(diagLeftLocation)) { 
				if (diagLeftLocation == model.getEnPassant()) {
					// en passant capture
					moves.add(new ChessMoveEnPassantCapture(getPiece(), location, diagLeftLocation));					
				}
			}
		}
		
		// capture diagonal right
		Location diagRightLocation = location.getRelativeLocation(1, dir * 1);
		if (diagRightLocation != Unknown) {
			if (model.isLocationOccupiedByColor(diagRightLocation, getOtherColor())) {
				// regular capture
				moves = addPawnMoveWithPromotionCheck(moves, location, diagRightLocation);	
			} else if (model.isLocationEmpty(diagRightLocation)) {
				if (diagRightLocation == model.getEnPassant()) {
					// en passant capture
					moves.add(new ChessMoveEnPassantCapture(getPiece(), location, diagRightLocation));
				}
			}
		}		
				
		// we are done
		return moves;
	}
	
	/**
	 * Add the given move to the set of possible pawn move with the 
	 * extra step of checking for pawn promotion.  Pawns must 
	 * be promoted to Queens, Bishops, Rooks, or Knights when they
	 * move into the promotion rank.
	 * 
	 * @param currentMoves
	 * @param fromLocation
	 * @param toLocation
	 * @return
	 */
	private Set<ChessMove> addPawnMoveWithPromotionCheck(Set<ChessMove> currentMoves, Location fromLocation, Location toLocation) {
		if (!isPromotionRank(toLocation)) {
			// standard case, no promotion
			ChessMove newMove = new ChessMove(getPiece(), fromLocation, toLocation);
			currentMoves.add(newMove);
		} else {
			// the pawn must be promoted in this move
			Piece queenPromotionPiece = (getColor() == White ? WhiteQueen : BlackQueen);
			currentMoves.add(new ChessMovePawnPromotion(getPiece(), fromLocation, toLocation, queenPromotionPiece));
			
			Piece bishopPromotionPiece = (getColor() == White ? WhiteBishop : BlackBishop);
			currentMoves.add(new ChessMovePawnPromotion(getPiece(), fromLocation, toLocation, bishopPromotionPiece));
			
			Piece rookPromotionPiece = (getColor() == White ? WhiteRook : BlackRook);
			currentMoves.add(new ChessMovePawnPromotion(getPiece(), fromLocation, toLocation, rookPromotionPiece));
			
			Piece knightPromotionPiece = (getColor() == White ? WhiteKnight : BlackKnight);
			currentMoves.add(new ChessMovePawnPromotion(getPiece(), fromLocation, toLocation, knightPromotionPiece));
		} 
		return currentMoves;
	}
	
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	private boolean isStartingRow(Location location) {
		int y = location.getY();
		if (getColor() == White) {
			return y == 1;
		} else {
			return y == 6;
		}
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	private boolean isPromotionRank(Location location) {
		int y = location.getY();
		if (getColor() == White) {
			return y == 7;
		} else {
			return y == 0;
		}		
	}
	
	
	/**
	 * @see com.darringer.games.chess.logic.PieceLogic#getPiece()
	 */
	@Override
	public Piece getPiece() {
		return getColor() == White ? WhitePawn : BlackPawn;
	}
}
