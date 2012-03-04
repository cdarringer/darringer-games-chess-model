package com.darringer.games.chess.model;

import static com.darringer.games.chess.model.Location.*;
import static com.darringer.games.chess.model.Piece.*;
import static com.darringer.games.chess.model.ChessMoveType.*;

/**
 * Model representing the move of a {@link Piece} from one
 * {@link Location} to another {@link Location}.
 * 
 * @author cdarringer
 *
 */
public class ChessMove {
	protected Piece piece;
	protected Location from;
	protected Location to;
	protected Location enPassantLocation;
	protected boolean isWhiteInCheckMate;
	protected boolean isBlackInCheckMate;

	/**
	 * 
	 * @param piece
	 * @param fromLocation
	 * @param toLocation
	 */
	public ChessMove(Piece piece, Location fromLocation, Location toLocation) {
		this.piece = piece;
		this.from = fromLocation;
		this.to = toLocation;
		this.enPassantLocation = Unknown;
		this.isWhiteInCheckMate = false;
		this.isBlackInCheckMate = false;
	}
	
	/**
	 * 
	 * @param move
	 */
	public ChessMove(ChessMove move) {
		this(move.getPiece(), move.getFrom(), move.getTo());
	}
	
	public ChessMoveType getType() {
		return Regular;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public Location getFrom() {
		return from;
	}

	public void setFrom(Location from) {
		this.from = from;
	}

	public Location getTo() {
		return to;
	}

	public void setTo(Location to) {
		this.to = to;
	}

	public Location getEnPassantLocation() {
		return enPassantLocation;
	}

	public void setEnPassantLocation(Location enPassantLocation) {
		this.enPassantLocation = enPassantLocation;
	}

	public Color getColor() {
		return piece.getColor();
	}

	public boolean isWhiteInCheckMate() {
		return isWhiteInCheckMate;
	}

	public void setWhiteInCheckMate(boolean isWhiteInCheckMate) {
		this.isWhiteInCheckMate = isWhiteInCheckMate;
	}

	public boolean isBlackInCheckMate() {
		return isBlackInCheckMate;
	}

	public void setBlackInCheckMate(boolean isBlackInCheckMate) {
		this.isBlackInCheckMate = isBlackInCheckMate;
	}
	
	public Location getEnPassantCaptureLocation() {
		return Unknown;
	}
	
	public Piece getPawnPromotionPiece() {
		return None;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ChessMove) {
			ChessMove otherObj = (ChessMove)obj;
			result = ((otherObj.getPiece() == this.getPiece()) && 
					  (otherObj.getFrom() == this.getFrom()) && 
					  (otherObj.getTo() == this.getTo()) &&
					  (otherObj.getType() == this.getType()) && 
					  (otherObj.getPawnPromotionPiece() == this.getPawnPromotionPiece()));
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		return piece.hashCode() * 
			from.hashCode() * 
			to.hashCode() * 
			getType().hashCode() * 
			getPawnPromotionPiece().hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s->%s", piece, from, to);
	}
}
