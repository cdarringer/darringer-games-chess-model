package com.darringer.games.chess.model;

import static com.darringer.games.chess.model.Color.White;

/**
 * Model representing castling availability. 
 * 
 * @author cdarringer
 * 
 * @see com.darringer.games.chess.model.ChessModel
 *
 */
public class CastlingAvailability {
	private boolean isWhiteCanCastleKingSide;
	private boolean isWhiteCanCastleQueenSide;
	private boolean isBlackCanCastleKingSide;
	private boolean isBlackCanCastleQueenSide;
	
	/**
	 * Default constructor
	 */
	public CastlingAvailability() {
		this.isWhiteCanCastleKingSide = true;
		this.isWhiteCanCastleQueenSide = true;
		this.isBlackCanCastleKingSide = true;
		this.isBlackCanCastleQueenSide = true;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param castlingAvailability
	 */
	public CastlingAvailability(CastlingAvailability castlingAvailability) {
		this.isWhiteCanCastleKingSide = castlingAvailability.isWhiteCanCastleKingSide;
		this.isWhiteCanCastleQueenSide = castlingAvailability.isWhiteCanCastleQueenSide;
		this.isBlackCanCastleKingSide = castlingAvailability.isBlackCanCastleKingSide;
		this.isBlackCanCastleQueenSide = castlingAvailability.isBlackCanCastleQueenSide;
	}
	
	public boolean isCanCastleKingSide(Color color) {
		if (color == White) {
			return this.isWhiteCanCastleKingSide;
		} else {
			return this.isBlackCanCastleKingSide;
		}
	}
	
	public boolean isCanCastleQueenSide(Color color) {
		if (color == White) {
			return this.isWhiteCanCastleQueenSide;
		} else {
			return this.isBlackCanCastleQueenSide;
		}		
	}
	
	public boolean isWhiteCanCastleKingSide() {
		return isWhiteCanCastleKingSide;
	}

	public void setWhiteCanCastleKingSide(boolean isWhiteCanCastleKingSide) {
		this.isWhiteCanCastleKingSide = isWhiteCanCastleKingSide;
	}

	public boolean isWhiteCanCastleQueenSide() {
		return isWhiteCanCastleQueenSide;
	}

	public void setWhiteCanCastleQueenSide(boolean isWhiteCanCastleQueenSide) {
		this.isWhiteCanCastleQueenSide = isWhiteCanCastleQueenSide;
	}

	public boolean isBlackCanCastleKingSide() {
		return isBlackCanCastleKingSide;
	}

	public void setBlackCanCastleKingSide(boolean isBlackCanCastleKingSide) {
		this.isBlackCanCastleKingSide = isBlackCanCastleKingSide;
	}

	public boolean isBlackCanCastleQueenSide() {
		return isBlackCanCastleQueenSide;
	}

	public void setBlackCanCastleQueenSide(boolean isBlackCanCastleQueenSide) {
		this.isBlackCanCastleQueenSide = isBlackCanCastleQueenSide;
	}

	@Override
	public String toString() {
		StringBuilder castlingAvailabilityString = new StringBuilder();
		
		if (this.isWhiteCanCastleKingSide) {
			castlingAvailabilityString.append("K");
		}
		if (this.isWhiteCanCastleQueenSide) {
			castlingAvailabilityString.append("Q");
		}
		if (this.isBlackCanCastleKingSide) {
			castlingAvailabilityString.append("k");
		}
		if (this.isBlackCanCastleQueenSide) {
			castlingAvailabilityString.append("q");
		}
		String result = castlingAvailabilityString.toString();
		if (result.equals("")) {
			return "-";
		} else {
			return result;
		}
	}	
}
