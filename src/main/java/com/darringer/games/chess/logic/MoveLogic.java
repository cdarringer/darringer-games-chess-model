package com.darringer.games.chess.logic;

import static com.darringer.games.chess.model.Color.Black;
import static com.darringer.games.chess.model.Color.White;
import static com.darringer.games.chess.model.GameState.*;
import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;

import com.darringer.games.chess.model.CastlingAvailability;
import com.darringer.games.chess.model.ChessModel;
import com.darringer.games.chess.model.ChessMove;
import com.darringer.games.chess.model.Piece;

/**
 * Control logic for the application of a {@link ChessMove} to a 
 * {@link ChessModel), being sure to update the game state with 
 * the various move side effects.
 * 
 * @author cdarringer
 *
 * @see com.darringer.games.chess.model.ChessModel
 * @see com.darringer.games.chess.model.ChessMove
 * @see com.darringer.games.chess.model.ChessMoveType
 * 
 */
public class MoveLogic {

	
	/**
	 * Applies a {@link ChessMove} to a {@link ChessModel}.
	 * Updates the following items in the model state:
	 * <ol>
	 *   <li>The moved piece<li>
	 *   <li>En passant move handling and target square<li>
	 *   <li>The active color<li>
	 *   <li>Castling availability<li>
	 *   <li>Move counts<li>
	 * </ol>
	 * 
	 * @param model
	 * @param move
	 * @param piece
	 * @return
	 */
	public ChessModel applyMoveToModel(ChessModel model, ChessMove move) {
		// move the piece assuming regular move
		model.setPieceAtLocation(move.getTo(), move.getPiece());
		model.setPieceAtLocation(move.getFrom(), None);

		// handling for "special" moves
		switch (move.getType()) {
		case CastleKingSide:
			if (move.getColor() == White) {
				// white castle king side - we also need to move the rook
				model.setPieceAtLocation(H1, None);
				model.setPieceAtLocation(F1, WhiteRook);
			} else {
				// black castle king side - we also need to move the rook
				model.setPieceAtLocation(H8, None);
				model.setPieceAtLocation(F8, BlackRook);
			}
			break;

		case CastleQueenSide:
			if (move.getColor() == White) {
				// white castle queen side - we also need to move the rook
				model.setPieceAtLocation(A1, None);
				model.setPieceAtLocation(D1, WhiteRook);
			} else {
				// black castle queen side - we also need to move the rook
				model.setPieceAtLocation(A8, None);
				model.setPieceAtLocation(D8, BlackRook);
			}
			break;
			
		case EnPassantCapture:
			// this was an en passant capture - remove captured piece
			model.setPieceAtLocation(move.getEnPassantCaptureLocation(), None);			
			break;
			
		case PawnPromotion:
			// the pawn is promoted to a different piece
			model.setPieceAtLocation(move.getTo(), move.getPawnPromotionPiece());
			break;
			
		default:
			break;
		}
		
		// update the en passant location
		model.setEnPassant(move.getEnPassantLocation());			
		
		// update castling availability
		model = updateCastlingAvailabilityFromModelAndMove(model, move);

		// update the active color
		model.setActiveColor(move.getColor() == White ? Black : White);
		
		// update the full move number
		if (move.getColor() == Black) {
			model.setFullmoveNumber(model.getFullmoveNumber() + 1);
		}
		
		// TODO: update the half move clock
		model.setHalfmoveClock(0);
		
		// state
		if (move.isBlackInCheckMate()) {
			model.setState(BlackInCheckMate);
		} else if (move.isWhiteInCheckMate()) {
			model.setState(WhiteInCheckMate);
		} else {
			model.setState(OK);
		}
		
		// score, node count left alone
		
		// model state is up to date
		return model;		
	}
	

	
	/**
	 * Get the en passant location in the case that a pawn moved forward
	 * two squares.
	 * 
	 * @param model
	 * @param move
	 * @return
	 */
	/*
	public Location getEnPassantFromModelAndMove(ChessModel model, ChessMove move) {
		if ((move.getPiece() != BlackPawn) && (move.getPiece() != WhitePawn)) {
			return Unknown;
		} else {
			// its a pawn - did it move forward two squares?
			int y1 = move.getFrom().getY();
			int y2 = move.getTo().getY();
			if (Math.abs(y2 - y1) == 1) {
				// forward one or diagonal capture
				return Unknown;
			} else {
				// forward two, set enPassant to the square "behind"
				int x1 = move.getFrom().getX();
				if (y2 > y1) {
					// pawn moved up
					return Location.get(x1, y2 - 1);
				} else {
					// pawn moved down
					return Location.get(x1, y1 - 1);
				}
			}
		}
	}
	*/
	
	/**
	 * 
	 * @param model
	 * @param move
	 * @return
	 */
	public ChessModel updateCastlingAvailabilityFromModelAndMove(ChessModel model, ChessMove move) {
		CastlingAvailability castling = model.getCastlingAvailability();
		Piece piece = move.getPiece();
		if (piece == WhiteKing) {
			castling.setWhiteCanCastleKingSide(false);
			castling.setWhiteCanCastleQueenSide(false);
		} else if (piece == BlackKing) {
			castling.setBlackCanCastleKingSide(false);
			castling.setBlackCanCastleQueenSide(false);
		} else if (piece == WhiteRook) {
			if (move.getFrom() == A1) {
				castling.setWhiteCanCastleQueenSide(false);
			} else if (move.getFrom() == H1) {
				castling.setWhiteCanCastleKingSide(false);
			}
		} else if (piece == BlackRook) {
			if (move.getFrom() == A8) {
				castling.setBlackCanCastleQueenSide(false);
			} else if (move.getFrom() == H8) {
				castling.setBlackCanCastleKingSide(false);
			}
		}
		return model;
	}

	
}
